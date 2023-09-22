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
import org.tongji.programming.ConfigProvider;
import org.tongji.programming.DTO.cqhttp.MessageUniversalReport;
import org.tongji.programming.DTO.cqhttp.NoticeUniversalReport;
import org.tongji.programming.DTO.cqhttp.requestEvent.GroupRequestEvent;
import org.tongji.programming.DTO.cqhttp.requestEvent.GroupRequestEventResponse;
import org.tongji.programming.helper.JSONHelper;
import org.tongji.programming.http.BotGroupService;
import org.tongji.programming.pojo.Student;
import org.tongji.programming.service.CheckCardService;
import org.tongji.programming.service.CourseService;
import org.tongji.programming.service.GroupUtilService;
import org.tongji.programming.service.StudentService;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.regex.Pattern;

import static java.lang.Long.parseLong;

import java.util.Random;

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

    private String[] getMsgInfo(String msgId, int dbNum) {
        Jedis jedis = jedisPool.getResource();
        // 选择数据库
        jedis.select(dbNum);

        // 从哈希表中获取关联数据
        String userData = jedis.hget("message-data", msgId);

        jedis.close();

        if (userData != null) {
            return userData.split("^##");// 第一个是消息内容，第二个是发消息者的QQ号
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
        if (!configProvider.get("AntiRecall", "flag", boolean.class)) {
            return null;
        }

        String operatorId = String.valueOf(event.getOperatorId());
        String groupId = String.valueOf(event.getGroupId());
        String msgId = String.valueOf(event.getMessageId());

        var data = getMsgInfo(msgId, 3);
        String userId = null;
        String msg = null;
        if (data != null) {
            msg = data[0];
            userId = data[1];
        }
        log.info("收到群（{}）的消息撤回事件——操作者：{}，消息所有者：{}，被撤回消息：{}", groupId, operatorId, userId, msg);

        if (configProvider.get("AntiRecall", "forEveryone", boolean.class)) {
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

            if (configProvider.get("AntiRecall", "randomFlag", boolean.class)) {
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

    @Override
    public void test() {
        Config conf = ConfigFactory.load("ServiceSetting");

        Config settings = conf.getConfig("RecallSetting");
        Config random = settings.getConfig("random");
        var time = calcTime(random.getString("upperLimits"));
        System.err.println(time);

    }
}
