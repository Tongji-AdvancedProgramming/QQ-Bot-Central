package org.tongji.programming.controller;

import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.tongji.programming.dto.APIDataResponse;
import org.tongji.programming.dto.APIResponse;
import org.tongji.programming.mapper.CourseMapper;
import org.tongji.programming.mapper.StudentMapper;

@Slf4j
@RestController
@RequestMapping("course")
public class CourseController {

    @Autowired
    CourseMapper courseMapper;

    @RequestMapping(method = RequestMethod.GET)
    public APIResponse GetAll(){
        var result = courseMapper.selectAll();
        return APIDataResponse.Success(result);
    }

    /**
     * 导入学生名单
     * @param file 目前打算支持CSV、TXT
     * @return 无
     */
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public APIResponse Import(@RequestPart MultipartFile file){
        return APIResponse.Fail("5000","还没写");
    }
}
