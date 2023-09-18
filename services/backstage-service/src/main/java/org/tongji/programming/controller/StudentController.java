package org.tongji.programming.controller;

import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.tongji.programming.dto.APIDataResponse;
import org.tongji.programming.dto.APIResponse;
import org.tongji.programming.dto.StudentService.GetStudents;
import org.tongji.programming.mapper.CourseMapper;
import org.tongji.programming.mapper.StudentMapper;
import org.tongji.programming.service.StudentImportService;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("student")
public class StudentController {

    @Autowired
    StudentMapper studentMapper;

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    StudentImportService studentImportService;

    @RequestMapping(method = RequestMethod.GET)
    public APIResponse GetAllStudents(
            @RequestParam(value = "page_size") long pageSize,
            @RequestParam(value = "page_num") long pageNum,
            @Nullable @RequestParam String courseId,
            @Nullable @RequestParam String classId,
            @Nullable @RequestParam String startStuNo,
            @Nullable @RequestParam String endStuNo
    ) {
        if (pageSize > 500) {
            pageSize = 500;
        }
        var result = studentMapper.selectWithPageAndFilter(pageSize, (pageNum - 1) * pageSize, courseId, classId, startStuNo, endStuNo);
        var count = studentMapper.selectWithPageAndFilterCount(courseId, classId, startStuNo, endStuNo);
        return APIDataResponse.Success(GetStudents.builder().students(result).totalNum(count).build());
    }

    /**
     * 导入学生名单
     *
     * @param file 目前打算支持CSV、TXT
     * @return 无
     */
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public APIResponse Import(@RequestPart("file") MultipartFile file, @Nullable @RequestPart("course_id") String courseId, @Nullable @RequestPart("class_id") String classId) throws IOException {

        if (file == null || file.isEmpty() || file.getSize() == 0) {
            return APIResponse.Fail("4001", "上传的文件为空或不可读取");
        }

        var type = file.getContentType();
        if (Objects.equals(type, "text/plain")) {
            if (courseId == null || classId == null) {
                return APIResponse.Fail("4001", "未填写课号或班号");
            }

            var course = courseMapper.selectById(courseId);
            if (course == null) {
                return APIResponse.Fail("4001", "课号不存在，请先添加课程");
            }

            try {
                var written = studentImportService.resolvePlainText(file.getInputStream(), courseId, classId);
                return APIDataResponse.Success(written);
            } catch (Exception e) {
                return APIResponse.Fail("4000", e.getLocalizedMessage());
            }
        } else if (Objects.equals(type, "text/csv")) {
            try {
                var written = studentImportService.resolveCsv(file.getInputStream());
                return APIDataResponse.Success(written);
            } catch (Exception e) {
                return APIResponse.Fail("4000", e.getLocalizedMessage());
            }
        } else if (Objects.equals(type, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            try {
                var result = studentImportService.resolveExcel(file.getInputStream());
                return APIResponse.Success();
            } catch (Exception e) {
                return APIResponse.Fail("4000", e.getLocalizedMessage());
            }
        }

        return APIResponse.Fail("4000", "意外的文件格式");
    }

    @Transactional
    @RequestMapping(method = RequestMethod.DELETE)
    public APIResponse Delete(@RequestParam("id") List<String> ids, @RequestParam("cid") List<String> courseIds) {
        if (ids.size() != courseIds.size()) {
            return APIResponse.Fail("4001", "非法调用（参数数量不相等）");
        }

        for (int i = 0; i < ids.size(); i++) {
            studentMapper.deleteById(ids.get(i), courseIds.get(i));
        }

        return APIResponse.Success();
    }


}
