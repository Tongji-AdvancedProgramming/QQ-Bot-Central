package org.tongji.programming.controller;

import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tongji.programming.dto.APIDataResponse;
import org.tongji.programming.dto.APIResponse;
import org.tongji.programming.mapper.StudentMapper;

@RestController
@RequestMapping("student")
public class StudentController {

    @Autowired
    StudentMapper studentMapper;

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


}
