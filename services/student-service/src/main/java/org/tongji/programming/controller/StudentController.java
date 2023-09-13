package org.tongji.programming.controller;

import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.tongji.programming.dto.APIDataResponse;
import org.tongji.programming.dto.APIResponse;
import org.tongji.programming.mapper.CourseMapper;
import org.tongji.programming.mapper.StudentMapper;

@RestController
@RequestMapping("student")
public class StudentController {

    @Autowired
    StudentMapper studentMapper;

    @Autowired
    CourseMapper courseMapper;

    @RequestMapping(method = RequestMethod.GET)
    public APIResponse GetAllStudents(
            @RequestParam(value = "page_size") long pageSize,
            @RequestParam(value = "page_num") long pageNum
    ) {
        if (pageSize > 500) {
            pageSize = 500;
        }
        var result = studentMapper.selectWithPage(pageSize, pageNum);
        return APIDataResponse.Success(result);
    }

    /**
     * 导入学生名单
     *
     * @param file 目前打算支持CSV、TXT
     * @return 无
     */
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public APIResponse Import(@RequestPart("file") MultipartFile file, @RequestPart("course_id") String courseId) {
        var course = courseMapper.selectById(courseId);
        if (course == null) {
            return APIResponse.Fail("4001", "课号不存在，请先添加课程");
        }

        if(file==null || file.isEmpty() || file.getSize()==0){
            return APIResponse.Fail("4001", "上传的文件为空或不可读取");
        }

        var type = file.getContentType();

        return APIResponse.Fail("5000", "还没写");
    }


}
