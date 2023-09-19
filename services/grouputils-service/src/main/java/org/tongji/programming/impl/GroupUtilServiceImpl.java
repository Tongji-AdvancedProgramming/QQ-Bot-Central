package org.tongji.programming.impl;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.tongji.programming.DTO.cqhttp.requestEvent.GroupRequestEvent;
import org.tongji.programming.DTO.cqhttp.requestEvent.GroupRequestEventResponse;
import org.tongji.programming.helper.JSONHelper;
import org.tongji.programming.pojo.Student;
import org.tongji.programming.service.CourseService;
import org.tongji.programming.service.GroupUtilService;
import org.tongji.programming.service.StudentService;

import java.util.regex.Pattern;

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
            var response = GroupRequestEventResponse.builder()
                    .approve(false).reason("加群验证消息格式错误：请按照格式填写验证信息。")
                    .build();
            var mapper = JSONHelper.getLossyMapper();
            var respJson = mapper.writeValueAsString(response);
            log.info("处理例程拒绝：{}", respJson);
            return respJson;
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
}
