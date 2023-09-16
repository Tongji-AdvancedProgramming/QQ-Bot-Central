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
import org.tongji.programming.config.CheckCardConfig;
import org.tongji.programming.config.MajorList;
import org.tongji.programming.mapper.CheckResultMapper;
import org.tongji.programming.pojo.CheckResult;
import org.tongji.programming.helper.JSONHelper;
import org.tongji.programming.pojo.Student;
import org.tongji.programming.service.CourseService;
import org.tongji.programming.service.HelloWorldService;
import org.tongji.programming.service.StudentService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@DubboService
public class HelloWorldServiceImpl implements HelloWorldService {
    @Override
    public String sayHelloWorld() {
        var mapper = JSONHelper.getLossyMapper();
        var response = new MessageUniversalResponse();
        response.setReply("你好！");
        response.setAtSender(true);
        try{
            return mapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Resource
    private HelloWorldService helloWorldService;
    @Override
    public APIResponse getList(Long groupId) {
        var res=helloWorldService.getList(groupId);
        return res;
    }

    @Autowired
    CheckResultMapper checkResultMapper;
    @Override
    public List<CheckResult> handlelist(APIResponse response) {
        var data=response.getData();

        //List<?> listRaw = data.toJavaObject(List.class);
        //List<Integer> list = ListHelper.safeConvertToList(listRaw, Integer.class);

        List<CheckResult> resultList = new ArrayList<>();
        JSONArray jsonArray = JSON.parseArray(String.valueOf(data));
        for (int i = 0; i < jsonArray.size(); i++) {
            CheckResult result = new CheckResult();
            JSONObject obj = jsonArray.getJSONObject(i);
            //System.out.println(obj);
            Long userId=Long.parseLong(obj.get("user_id").toString());
            result.card = obj.get("card").toString();
            result.studentId = userId;
            result.checkresult = false;
            result.failedreason = null;

            org.tongji.programming.mapper.CheckResult dbResult = checkResultMapper.selectCheckResultById(userId);

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

    Student student = Student.builder().build();

    @Override
    public String checkCard(Long groupId) {
        var studentlist = handlelist(getList(groupId));
        //System.out.println(studentlist);
        //var courseId= courseService.getCourseIdFromQQGroupId(String.valueOf(groupId));
        String[] majorlist = {"测绘","软工","计科","光电","微电子"};
        for(CheckResult result : studentlist){
            org.tongji.programming.mapper.CheckResult checkResult = org.tongji.programming.mapper.CheckResult.builder().build();
            checkResult.setCard(result.getCard());
            checkResult.setId(result.getStudentId());
            checkResult.setFailedTimes(result.getFailedtimes());
            checkResultMapper.insertCheckResult(checkResult);
            String[] studentinfolist = result.getCard().split("-");
            if (studentinfolist.length < 3) {
                result.failedreason = "名片未用-分为三项";
                result.failedtimes += 1;
            }
            else if(Long.parseLong(studentinfolist[0]) <= 2000000 || Long.parseLong(studentinfolist[0]) >= 2400000){
                result.failedreason = "学号格式不正确";
                result.failedtimes += 1;
            }
            else if(!Arrays.toString(majorlist).contains(studentinfolist[1])){
                result.failedreason = "专业名称不正确";
                result.failedtimes += 1;
            }
            else{
                student.setName(studentinfolist[2]);
                student.setMajor(studentinfolist[1]);
                student.setClassId(null);
                student.setCourseId("100717");
                student.setStuNo(studentinfolist[0]);
                //System.out.println(student);
                //未实现：查询学生信息是否正确


                result.checkresult = true;
            }
        }
        System.out.println(studentlist);

        var mapper = JSONHelper.getLossyMapper();
        var response = new MessageUniversalResponse();

        StringBuilder responsemsg = new StringBuilder();
        responsemsg.append("请以下同学改正群名片(超过三次将被踢出群)：\n");
        for(CheckResult result : studentlist){
            if(!result.isCheckresult()){
                responsemsg.append(String.format("[CQ:at,qq=%s] %s 提醒次数：%d\n",result.getStudentId(),result.getFailedreason(),result.getFailedtimes()));;
            }
        }
        System.out.println(responsemsg);
        response.setReply(String.valueOf(responsemsg));

        //try{
            return null;
            //return mapper.writeValueAsString(response);
        //}
        /*catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }*/
    }



}
