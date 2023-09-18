package org.tongji.programming.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.tongji.programming.DTO.cqhttp.APIResponse;
import org.tongji.programming.DTO.cqhttp.MessageUniversalResponse;
import org.tongji.programming.mapper.Assistants;
import org.tongji.programming.mapper.AssistantsMapper;
import org.tongji.programming.mapper.CheckResultMapper;
import org.tongji.programming.pojo.CheckResult;
import org.tongji.programming.helper.JSONHelper;
import org.tongji.programming.pojo.Student;
import org.tongji.programming.service.HelloWorldService;
import org.tongji.programming.service.StudentService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import java.util.regex.*;

@DubboService
public class HelloWorldServiceImpl implements HelloWorldService {
    @Override
    public String sendMsg(String msg) {
        var mapper = JSONHelper.getLossyMapper();
        var response = new MessageUniversalResponse();
        response.setReply(msg);
        response.setAtSender(false);
        try {
            return mapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Resource
    private HelloWorldService helloWorldService;

    @Override
    public APIResponse getList(Long groupId) {
        return helloWorldService.getList(groupId);
    }

    @Autowired
    CheckResultMapper checkResultMapper;

    @Override
    public List<CheckResult> handlelist(APIResponse response) {
        var data = response.getData();

        //List<?> listRaw = data.toJavaObject(List.class);
        //List<Integer> list = ListHelper.safeConvertToList(listRaw, Integer.class);

        List<CheckResult> resultList = new ArrayList<>();
        JSONArray jsonArray = JSON.parseArray(String.valueOf(data));

        //List<org.tongji.programming.mapper.CheckResult> dbAll = checkResultMapper.selectAll();
        //System.out.println(dbAll);

        for (int i = 0; i < jsonArray.size(); i++) {
            CheckResult result = new CheckResult();
            JSONObject obj = jsonArray.getJSONObject(i);
            //System.out.println(obj);
            Long userId = Long.parseLong(obj.get("user_id").toString());
            result.card = obj.get("card").toString();
            result.studentId = userId;
            result.checkresult = false;
            result.failedreason = null;

            org.tongji.programming.mapper.CheckResult dbResult = checkResultMapper.selectCheckResultById(userId);
            //System.out.println(dbResult);
            if (dbResult != null) {
                // 如果查询到数据，将 failedtimes 设置为数据库中记录的数据
                result.setFailedtimes(dbResult.getFailedTimes());
            } else {
                // 如果没有查询到数据，将 failedtimes 设置为 0
                result.setFailedtimes(0);
            }
            resultList.add(result);
            //System.out.println(result);
            //System.out.println(resultList);
        }
        return resultList;
    }

    @DubboReference
    StudentService studentService;

    @Autowired
    AssistantsMapper assistantsMapper;

    @Override
    public String addAssistants(){
        var list = handlelist(getList(773114957L));
        //System.err.println(list);
        for(CheckResult item : list){
            Assistants assistants = Assistants.builder().build();
            assistants.setName(item.getCard());
            assistants.setId(item.getStudentId());
            assistantsMapper.insertAssistants(assistants);
        }

        return sendMsg("已更新！");
    }

    @Override
    public boolean isAssistants(Long userId){
        var selectResult = assistantsMapper.selectById(userId);
        //System.err.println(selectResult!=null);
        return selectResult != null;
    }
    
    @Override
    public String checkCard(Long groupId,Long userId) {
        if(!isAssistants(userId)){
            return sendMsg("你无权使用此功能");
        }

        var studentlist = handlelist(getList(groupId));
        StringBuilder responsemsg = new StringBuilder();
        responsemsg.append("请以下同学改正群名片(提醒第三次将被踢出群)：\n");
        //System.out.println(studentlist);
        //var courseId= courseService.getCourseIdFromQQGroupId(String.valueOf(groupId));
        String[] majorlist = {"测绘", "软工", "计科", "光电", "微电子","电气","电信","通信","AI","自动化",
                "信安","大数据","计拔","应物","信管","工力","数学","新能源","文管","汽车"};
        for (CheckResult result : studentlist) {
            boolean checkpassflag = false;
            boolean assistantsflag = false;
            //System.err.println(isAssistants(result.studentId));
            //System.err.println(result.studentId);
            Student student = Student.builder().build();
            org.tongji.programming.mapper.CheckResult checkResult = org.tongji.programming.mapper.CheckResult.builder().build();
            checkResult.setCard(result.getCard());
            checkResult.setId(result.getStudentId());
            checkResult.setFailedTimes(result.getFailedtimes());
            checkResult.setFailedReason(result.failedreason);
            checkResultMapper.insertCheckResult(checkResult);

            String[] studentinfolist = result.getCard().split("-");
            //System.err.println(Arrays.toString(studentinfolist));
            if(Arrays.asList(studentinfolist).contains("助教")||Arrays.asList(studentinfolist).contains("围观")){
                if(!isAssistants(result.studentId)){
                    result.failedreason = "你是什么助教？";
                    result.failedtimes += 1;
                    checkResultMapper.updateFailedById(result.getStudentId(), result.getFailedtimes(),result.failedreason);
                    responsemsg.append(String.format("[CQ:at,qq=%s] %s 提醒次数：%d\n", result.getStudentId(), result.getFailedreason(), result.getFailedtimes()));
                }
                else {
                    checkpassflag = true;
                    assistantsflag = true;
                }
            }else if(Arrays.asList(studentinfolist).contains("沈坚")){
                if(result.studentId==278787983){
                    checkpassflag = true;
                    assistantsflag = true;
                }
                else{
                    result.failedreason = "大胆！敢冒充渣哥？";
                    result.failedtimes += 2;
                    checkResultMapper.updateFailedById(result.getStudentId(), result.getFailedtimes(),result.failedreason);
                    responsemsg.append(String.format("[CQ:at,qq=%s] %s 提醒次数：%d\n", result.getStudentId(), result.getFailedreason(), result.getFailedtimes()));
                }
            }else if(Arrays.asList(studentinfolist).contains("bot") || Arrays.asList(studentinfolist).contains("Bot")){
                if(!isAssistants(result.studentId)){
                    result.failedreason = "你是什么bot？";
                    result.failedtimes += 1;
                    checkResultMapper.updateFailedById(result.getStudentId(), result.getFailedtimes(),result.failedreason);
                    responsemsg.append(String.format("[CQ:at,qq=%s] %s 提醒次数：%d\n", result.getStudentId(), result.getFailedreason(), result.getFailedtimes()));
                }
                else {
                    checkpassflag = true;
                    assistantsflag = true;
                }
            } else if (studentinfolist.length < 3) {
                result.failedreason = "名片未用-分为三项";
                result.failedtimes += 1;
                checkResultMapper.updateFailedById(result.getStudentId(), result.getFailedtimes(),result.failedreason);
                responsemsg.append(String.format("[CQ:at,qq=%s] %s 提醒次数：%d\n", result.getStudentId(), result.getFailedreason(), result.getFailedtimes()));
            } else if (Long.parseLong(studentinfolist[0]) <= 1000000 || Long.parseLong(studentinfolist[0]) >= 2400000) {
                result.failedreason = "学号格式不正确";
                result.failedtimes += 1;
                checkResultMapper.updateFailedById(result.getStudentId(), result.getFailedtimes(),result.failedreason);
                responsemsg.append(String.format("[CQ:at,qq=%s] %s 提醒次数：%d\n", result.getStudentId(), result.getFailedreason(), result.getFailedtimes()));
            } else if(studentinfolist[1].contains(String.valueOf("0")) || studentinfolist[1].contains(String.valueOf("1")) || studentinfolist[1].contains(String.valueOf("2"))){
                Pattern pattern = Pattern.compile("^([1-9]|1[0-9]|20)$");
                Matcher matcher = pattern.matcher(studentinfolist[1]);
                if (matcher.matches()) {
                    // 匹配成功
                    int majorNumber = Integer.parseInt(studentinfolist[1].substring(1)); // 获取数字部分并转为整数
                    if (majorNumber >= 1 && majorNumber <= 20) {
                        // 数字在1到20之间
                        checkpassflag = true;
                    } else {
                        // 数字不在1到20之间
                        result.failedreason = "班级格式不正确";
                        result.failedtimes += 1;
                        checkResultMapper.updateFailedById(result.getStudentId(), result.getFailedtimes(),result.failedreason);
                        responsemsg.append(String.format("[CQ:at,qq=%s] %s 提醒次数：%d\n", result.getStudentId(), result.getFailedreason(), result.getFailedtimes()));
                    }
                } else {
                    // 匹配失败
                }
            } else if (!Arrays.toString(majorlist).contains(studentinfolist[1])) {
                result.failedreason = "专业名称不正确";
                result.failedtimes += 1;
                checkResultMapper.updateFailedById(result.getStudentId(), result.getFailedtimes(),result.failedreason);
                responsemsg.append(String.format("[CQ:at,qq=%s] %s 提醒次数：%d\n", result.getStudentId(), result.getFailedreason(), result.getFailedtimes()));
            }else{
                checkpassflag = true;
            }
            if(checkpassflag) {
                if(!assistantsflag) {
                    student.setName(studentinfolist[2]);
                    student.setMajor(studentinfolist[1]);
                    student.setClassId(null);
                    student.setCourseId("100717");
                    student.setStuNo(studentinfolist[0]);
                    //System.out.println(student);
                    //未实现：查询学生信息是否正确

                    var isvalid = studentService.StudentValid(student);
                    //System.err.println(isvalid);
                    //System.err.println(student);

                    if (isvalid == 1) {
                        result.failedreason = "学生信息无法匹配";
                        result.failedtimes += 1;
                        checkResultMapper.updateFailedById(result.getStudentId(), result.getFailedtimes(),result.failedreason);
                        responsemsg.append(String.format("[CQ:at,qq=%s] %s 提醒次数：%d\n", result.getStudentId(), result.getFailedreason(), result.getFailedtimes()));
                    } else if (isvalid == 2) {
                        result.failedreason = "找不到学生信息";
                        result.failedtimes += 1;
                        checkResultMapper.updateFailedById(result.getStudentId(), result.getFailedtimes(),result.failedreason);
                        responsemsg.append(String.format("[CQ:at,qq=%s] %s 提醒次数：%d\n", result.getStudentId(), result.getFailedreason(), result.getFailedtimes()));
                    } else {
                        result.setFailedtimes(0);
                        checkResultMapper.updateFailedById(result.getStudentId(), result.getFailedtimes(),result.failedreason);
                        result.checkresult = true;
                        //System.err.println(result);
                        //System.out.println(studentlist);
                    }
                }
                else{
                    result.setFailedtimes(0);
                    checkResultMapper.updateFailedById(result.getStudentId(), result.getFailedtimes(),result.failedreason);
                    result.checkresult = true;
                    //System.err.println(result);
                }
            }
            if(result.getFailedtimes()>=3){
                kickMember(groupId,result.getStudentId());
                checkResultMapper.deleteById(result.getStudentId());
            }
        }

        var mapper = JSONHelper.getLossyMapper();
        var response = new MessageUniversalResponse();


        //List<org.tongji.programming.mapper.CheckResult> dbAll = checkResultMapper.selectAll();
        //System.out.println(dbAll);

        System.out.println(responsemsg);
        response.setReply(String.valueOf(responsemsg));

        //try{
        return sendMsg(String.valueOf(responsemsg));
        //return mapper.writeValueAsString(response);
        //}
        /*catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }*/
    }

    @Override
    public void kickMember(Long groupId, long studentId) {
        helloWorldService.kickMember(groupId,studentId);
        System.out.printf("从群聊：%d中 踢出群成员ID：%d%n",groupId,studentId);
    }
}
