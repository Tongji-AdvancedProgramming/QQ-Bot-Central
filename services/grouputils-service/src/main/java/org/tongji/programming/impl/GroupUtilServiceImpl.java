package org.tongji.programming.impl;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.tongji.programming.ConfigProvider;
import org.tongji.programming.DTO.cqhttp.MessageUniversalReport;
import org.tongji.programming.DTO.cqhttp.NoticeUniversalReport;
import org.tongji.programming.DTO.cqhttp.requestEvent.GroupRequestEvent;
import org.tongji.programming.DTO.cqhttp.requestEvent.GroupRequestEventResponse;
import org.tongji.programming.helper.JSONHelper;
import org.tongji.programming.http.BotGroupService;
import org.tongji.programming.mapper.Reminder;
import org.tongji.programming.mapper.ReminderMapper;
import org.tongji.programming.pojo.Student;
import org.tongji.programming.service.CheckCardService;
import org.tongji.programming.service.CourseService;
import org.tongji.programming.service.GroupUtilService;
import org.tongji.programming.service.StudentService;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static java.lang.Long.parseLong;

@Slf4j
@DubboService
public class GroupUtilServiceImpl implements GroupUtilService {

    @DubboReference
    CourseService courseService;

    @DubboReference
    StudentService studentService;

    static Pattern groupRequestCommentPattern = Pattern.compile("(.*)-(.*)-(.*)");

    @SneakyThrows
    @Override
    public String groupRequestHandler(GroupRequestEvent event) {
        if (event.getSubType().equals("invite")) {
            // 不处理邀请
            return "{}";
        }

        log.info("已进入处理例程，event：{}", event);

        // 收到加群申请，首先检查这个群是否被管理
        var course = courseService.getCourseIdFromQQGroupId(event.getGroupId());
        if (course.isEmpty()) {
            return "{}";
        }

        // 预处理Comment
        var comment = event.getComment();
        if (comment.startsWith("问题")) {
            // 问答格式
            var subStrStart = comment.indexOf("答案：");
            comment = comment.substring(subStrStart + 3);
        }

        // 谨慎处理：对于加群申请格式不对的，拒绝并给出理由
        var matcher = groupRequestCommentPattern.matcher(comment);
        if (!matcher.matches()) {
            if (configProvider.get("JoinGroup", "rejectForFormat", boolean.class)) {
                var response = GroupRequestEventResponse.builder()
                        .approve(false).reason("加群验证消息格式错误：请按照格式填写验证信息。")
                        .build();
                var mapper = JSONHelper.getLossyMapper();
                var respJson = mapper.writeValueAsString(response);
                log.info("处理例程拒绝：{}", respJson);
                return respJson;
            } else
                return "{}";
        }

        // 格式正确，尝试找出学号和姓名
        String[] requestParam = {matcher.group(1), matcher.group(2), matcher.group(3)};
        String stuNo = null;
        for (int i = 0; i < 3; i++) {
            if (StringUtils.isNumeric(requestParam[i], false)) {
                stuNo = requestParam[i];
                break;
            }
        }
        if (stuNo == null) {
            return "{}"; // 交人工判断
        }
        for (int i = 0; i < 3; i++) {
            if (requestParam[i].matches(".*\\d.*")) {
                // 不是人名，跳过
                continue;
            }
            // 假设这个是人名，检查一次
            var student = Student.builder().name(requestParam[i]).stuNo(stuNo).build();
            for (var courseId : course) {
                if (studentService.StudentValid(student) != 2) {
                    log.info("处理例程通过");
                    // 检查通过
                    var response = GroupRequestEventResponse.builder()
                            .approve(true)
                            .build();
                    var mapper = JSONHelper.getLossyMapper();
                    return mapper.writeValueAsString(response);
                }
            }
        }

        // 交还人工判断
        return "{}";
    }

    JedisPool jedisPool;

    @Autowired
    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    private void storeMsgInfos(String msgId, String msg, String userId, int dbNum, long expirationTimeInSeconds) {
        Jedis jedis = jedisPool.getResource();
        // 选择数据库
        jedis.select(dbNum);

        // 使用哈希表存储关联数据
        jedis.hset("message-data", msgId, msg + "^##" + userId);

        jedis.expire("message-data", expirationTimeInSeconds);

        jedis.close();
    }

    private void storeSingleMsg(String msg,int dbNum){
        Jedis jedis = jedisPool.getResource();
        // 选择数据库
        jedis.select(dbNum);

        jedis.set("LatestMsg", msg);

        jedis.close();
    }

    private String getSingleMsg(int dbNum){
        Jedis jedis = jedisPool.getResource();
        // 选择数据库
        jedis.select(dbNum);

        String Msg = jedis.get("LatestMsg");

        jedis.close();
        return Msg;
    }

    private String[] getMsgInfo(String msgId, int dbNum) {
        Jedis jedis = jedisPool.getResource();
        // 选择数据库
        jedis.select(dbNum);

        // 从哈希表中获取关联数据
        String userData = jedis.hget("message-data", msgId);

        jedis.close();

        if (userData != null) {
            return userData.split("\\^##");// 第一个是消息内容，第二个是发消息者的QQ号
        } else {
            return null; // 返回null
        }
    }

    @SneakyThrows
    @Override
    public void groupMsgStore(MessageUniversalReport event) {
        String msgId = String.valueOf(event.getMessageId());
        String msg = event.getRawMessage();
        String userId = String.valueOf(event.getUserId());

        storeMsgInfos(msgId, msg, userId, 3, 120L);
    }

    @DubboReference
    CheckCardService checkCardService;

    @Autowired
    ConfigProvider configProvider;

    @SneakyThrows
    @Override
    public String groupRecallHandler(NoticeUniversalReport event) {
        if (!configProvider.get("AntiRecall", "flag", Boolean.class)) {
            return null;
        }

        String operatorId = String.valueOf(event.getOperatorId());
        String groupId = String.valueOf(event.getGroupId());
        String msgId = String.valueOf(event.getMessageId());

        var data = getMsgInfo(msgId, 3);
        //System.err.println(Arrays.toString(data));
        String userId = null;
        String msg = null;
        if (data != null) {
            msg = data[0];
            userId = data[1];
        }
        log.info("收到群（{}）的消息撤回事件——操作者：{}，消息所有者：{}，被撤回消息：{}", groupId, operatorId, userId, msg);

        if (configProvider.get("AntiRecall", "forEveryone", Boolean.class)) {
            antiRecall(operatorId, userId, msg, groupId);
        } else {
            if (userId != null && !checkCardService.isAssistants(Long.valueOf(userId))) {
                antiRecall(operatorId, userId, msg, groupId);
            }
        }

        return null;
    }

    @Resource
    BotGroupService botGroupService;

    private void antiRecall(String operatorId, String userId, String msg, String groupId) {
        if (operatorId.equals(userId)) {
            botGroupService.sendGroupMsg(Long.parseLong(groupId), String.format("[CQ:at,qq=%s] 撤回的消息是：%s", userId, msg));

            int banTime = calcTime(configProvider.get("AntiRecall", "defaultTime"));

            if (configProvider.get("AntiRecall", "randomFlag", Boolean.class)) {
                Random random = new Random();
                int max = calcTime(configProvider.get("AntiRecall", "randomUpperLimits"));
                int min = calcTime(configProvider.get("AntiRecall", "randomLowerLimits"));
                banTime = random.nextInt(max - min + 1) + min; // 生成 [min, max] 范围内的整数
            }
            System.err.println(banTime);
            botGroupService.setGroupBan(parseLong(groupId), parseLong(userId), banTime);
        }
    }

    ;

    private int calcTime(String time) {
        var timeList = time.split(":");

        int[] timeArray = new int[timeList.length];
        for (int i = 0; i < timeList.length; i++) {
            timeArray[i] = Integer.parseInt(timeList[i]);
        }

        int min = 60;
        int hour = min * 60;

        return timeArray[0] * hour + timeArray[1] * min + timeArray[2];
    }

    private int count=1;
    @SneakyThrows
    @Override
    public String groupRepeatHandler(MessageUniversalReport event){
        if(!configProvider.get("AntiRepeat","flag",Boolean.class))
            return null;
        else{
            Integer repeatTimes = configProvider.get("AntiRepeat","repeatTimes",Integer.class);
            var msg = event.getRawMessage();
            var userId = event.getUserId();
            Long msgId= (long) event.getMessageId();
            var groupId=event.getGroupId();
            var latestMsg=getSingleMsg(4);

            if(msg.equals(latestMsg))
                count++;
            else
                count=1;
            /*System.err.println(latestMsg+"---"+msg);
            System.err.println(count);*/
            storeSingleMsg(msg,4);
            if(count>=repeatTimes)
                antiRepeat(userId, msgId,groupId);
        }
        return null;
    }

    @Autowired
    ReminderMapper reminderMapper;
    /*@Autowired
    public void setReminderMapper(ReminderMapper reminderMapper) {
        this.reminderMapper = reminderMapper;
    }*/

    //chiaki 添加提醒 xxx 周x xx:xx:xx
    @Override
    public String addReminder(MessageUniversalReport event) {
        if(!checkCardService.isAssistants(event.getUserId()))
            return checkCardService.sendMsg("你无权使用此功能");
        var message = event.getRawMessage();
        var command = message.split(" ");
        if(command.length!=5)
            return checkCardService.sendMsg("指令格式错误，应为：'chiaki 添加提醒 xxx 周x xx:xx:xx'");

        var toRemind = command[2];
        var week = command[3];
        var time = command[4];

        Reminder reminder = Reminder.builder().build();
        reminder.setEvent(toRemind);
        reminder.setWeek(week);
        reminder.setTime(time);


        reminderMapper.insertReminder(reminder);
        String replyMsg = String.format("已将事件：'%s'加入提醒队列，将在%s %s 进行提醒",toRemind,week,time);
        return checkCardService.sendMsg(replyMsg);
    }

    //chiaki 应用提醒 x（表示第几个提醒事项）
    @Override
    public String addGroupId(MessageUniversalReport event){
        if(!checkCardService.isAssistants(event.getUserId()))
            return checkCardService.sendMsg("你无权使用此功能");
        var command = event.getRawMessage().split(" ");
        if(command.length<3)
            return checkCardService.sendMsg("格式错误，应为'chiaki 应用提醒 x（表示第几个提醒事项）'");
        var reminder = reminderMapper.selectById(Integer.parseInt(command[2]));
        var groupId = event.getGroupId();
        var groupIdInData = reminder.getGroupId();

        if(groupIdInData == null || groupIdInData.isEmpty())
            groupIdInData = String.valueOf(groupId);
        else {
            if (groupIdInData.contains(String.valueOf(groupId)))
                return checkCardService.sendMsg("无法添加，因为该提醒已经应用到此群了哦！");
            groupIdInData += "," + groupId;
        }
        reminderMapper.updateGroupId(groupIdInData, Integer.parseInt(command[2]));
        var replyMsg = String.format("已将提醒'%s'应用到本群：%s",reminder.getEvent(),groupId);
        updateReminder();
        return checkCardService.sendMsg(replyMsg);
    }

    //chiaki 取消应用提醒 x（表示第几个提醒事项）
    @Override
    public String deleteGroupId(MessageUniversalReport event) {
        if (!checkCardService.isAssistants(event.getUserId()))
            return checkCardService.sendMsg("你无权使用此功能");
        var command = event.getRawMessage().split(" ");
        if (command.length < 3)
            return checkCardService.sendMsg("格式错误，应为'chiaki 取消应用提醒 x（表示第几个提醒事项）'");
        var reminder = reminderMapper.selectById(Integer.parseInt(command[2]));
        var groupId = event.getGroupId();
        var groupIdInData = reminder.getGroupId();
        if (groupIdInData == null || !groupIdInData.contains(String.valueOf(groupId)))
            return checkCardService.sendMsg("该提醒本身就没有在此群启用哦！");
        else if (groupIdInData.contains(groupId + ","))
            groupIdInData = groupIdInData.replace(groupId + ",", "");
        else if(groupIdInData.contains("," + groupId))
            groupIdInData = groupIdInData.replace("," + groupId, "");
        else
            groupIdInData = groupIdInData.replace(groupId + "", "");
        reminderMapper.updateGroupId(groupIdInData, Integer.parseInt(command[2]));
        var replyMsg = String.format("已将提醒'%s'取消应用到本群：%s", reminder.getEvent(), groupId);
        updateReminder();
        return checkCardService.sendMsg(replyMsg);
    }

    //chiaki 删除提醒 x
    @Override
    public String deleteReminder(MessageUniversalReport event) {
        if(!checkCardService.isAssistants(event.getUserId()))
            return checkCardService.sendMsg("你无权使用此功能");
        var message = event.getRawMessage();
        var command = message.split(" ");
        if(command.length!=3)
            return checkCardService.sendMsg("指令格式错误，应为：'chiaki 删除提醒 x'");

        var id = command[2];
        var toRemind = reminderMapper.selectById(Integer.parseInt(id)).getEvent();
        String replyMsg = String.format("已将事件：'%s'从提醒队列删除",toRemind);
        reminderMapper.deleteById(Integer.parseInt(id));
        updateReminder();
        return checkCardService.sendMsg(replyMsg);
    }

    @Override
    public String selectAllReminder(MessageUniversalReport event){
        var allReminder = reminderMapper.selectAll();
        System.err.println(allReminder);
        StringBuilder replyMsg = new StringBuilder("当前提醒列表中有以下事件：\n");
        int No = 1;
        for (Reminder reminder:allReminder){
            replyMsg.append(String.format("%d.%s %s %s 在群聊：%s 中应用\n",No,reminder.getEvent(),reminder.getWeek(),reminder.getTime(),reminder.getGroupId()));
            No++;
        }
        return checkCardService.sendMsg(String.valueOf(replyMsg));
    }

    public List<ScheduledFuture<?>> scheduledFutures = new ArrayList<>();
    @Override
    public String reminderHandler(){
        int numberOfTasks = getNumberOfReminder();

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(numberOfTasks);

        for (int i = 1; i <= numberOfTasks; i++) {
            var reminder = reminderMapper.selectById(i);
            var event = reminder.getEvent();
            var week = reminder.getWeek();
            var time = reminder.getTime();
            var groupIdList = reminder.getGroupId().split(",");

            for(String groupId:groupIdList) {
                if(groupId==null || groupId.isEmpty())
                    return null;
                Runnable task = createTaskForTodoItem(i, event, Long.parseLong(groupId));
                long initialDelay = calcReminderStartingTime(week, time);
                long period = 7 * 24 * 60 * 60;
                //long period = 10;
                System.err.println("对群：("+groupId+")添加待办事项：" + event + ",将于" + initialDelay + "秒后进行第一次提醒，该事项的循环周期为：" + period + "秒");
                ScheduledFuture<?> future = executorService.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.SECONDS);
                scheduledFutures.add(future);
            }
        }

        return null;
    }

    public void updateReminder(){
        for (ScheduledFuture<?> future : scheduledFutures) {
            future.cancel(false); // 取消所有待办事项
        }

        reminderHandler();
    }

    private int getNumberOfReminder(){
        var allReminder = reminderMapper.selectAll();
        int i = 0;
        for (Reminder ignored :allReminder){
            i++;
        }
        return i;
    }

    private Runnable createTaskForTodoItem(int itemId, String event,long groupId) {
        // 根据待办事项的ID创建一个任务
        return () -> {
            botGroupService.sendGroupMsg(groupId,event);
            // 在这里执行待办事项的逻辑
            System.out.println("现在执行提醒事项：" + itemId + "——" + event + "，应用在群聊：" + groupId);
        };
    }

    //week:周一---周日，time:xx:xx:xx
    private int calcReminderStartingTime(String week,String time) {
        Calendar calendar = Calendar.getInstance();
        int nowWeek = calendar.get(Calendar.DAY_OF_WEEK); // 它把周日当成第一天
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int timeSec = 60 * 60 * hour + 60 * minute + second;

        int setWeek = transWeek(week);
        int setTimeSec = calcTime(time);

        int startingTime;
        int timeDiff = setTimeSec - timeSec;
        int secondOfDay = 24 * 60 * 60;
        if(setWeek > nowWeek)
            startingTime = (setWeek - nowWeek) * secondOfDay + timeDiff;
        else if(setWeek == nowWeek){
            if(timeDiff>=0)
                startingTime = timeDiff;
            else
                startingTime = 7 * secondOfDay + timeDiff;
        }
        else
            startingTime = (7 + setWeek - nowWeek) * secondOfDay + timeDiff;

        return startingTime;
    }

    private int transWeek(String week){
        int dayInt = -1; // 默认值，如果未匹配成功

        switch (week.toLowerCase()) {
            case "周一":
                dayInt = 2;
                break;
            case "周二":
                dayInt = 3;
                break;
            case "周三":
                dayInt = 4;
                break;
            case "周四":
                dayInt = 5;
                break;
            case "周五":
                dayInt = 6;
                break;
            case "周六":
                dayInt = 7;
                break;
            case "周日":
                dayInt = 1;
                break;
        }

        return dayInt;
    }


    private void antiRepeat(Long userId,Long msgId,Long groupId){
        //System.err.println("防复读功能生效");
        var isassistants = checkCardService.isAssistants(userId);
        if(!configProvider.get("AntiRepeat","forEveryone",Boolean.class)){
            //System.err.println(isassistants);
            if(isassistants) {
                var specialReply = configProvider.get("AntiRepeat","specialReply");
                if(specialReply!=null)
                    botGroupService.sendGroupMsg(groupId,specialReply);
                return;
            }
            else{
                if(configProvider.get("AntiRepeat","recallMsgFlag",Boolean.class))
                    botGroupService.deleteMsg(groupId,msgId);
                var replyMsg=configProvider.get("AntiRepeat","replyMsg");
                //System.err.println(replyMsg);
                if(replyMsg!=null)
                    botGroupService.sendGroupMsg(groupId,replyMsg);
            }
        }
        int banTime = calcTime(configProvider.get("AntiRepeat", "defaultTime"));

        if (configProvider.get("AntiRepeat", "randomFlag", Boolean.class)) {
            Random random = new Random();
            int max = calcTime(configProvider.get("AntiRepeat", "randomUpperLimits"));
            int min = calcTime(configProvider.get("AntiRepeat", "randomLowerLimits"));
            banTime = random.nextInt(max - min + 1) + min; // 生成 [min, max] 范围内的整数
        }
        System.err.println(banTime);
        if(banTime!=0){
            //System.err.println("banTime="+banTime);
            botGroupService.setGroupBan(groupId, userId, banTime);
        }
    }

    @Override
    public void test() {
        Config conf = ConfigFactory.load("ServiceSetting");

        Config settings = conf.getConfig("RecallSetting");
        Config random = settings.getConfig("random");
        var time = calcTime(random.getString("upperLimits"));
        System.err.println(time);

    }

}
