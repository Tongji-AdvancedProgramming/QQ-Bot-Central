package org.tongji.programming.controller;

import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.tongji.programming.dto.APIDataResponse;
import org.tongji.programming.dto.APIResponse;
import org.tongji.programming.mapper.CourseMapper;
import org.tongji.programming.mapper.StudentMapper;
import org.tongji.programming.pojo.Course;

@Slf4j
@RestController
@RequestMapping("course")
public class CourseController {

    @Autowired
    CourseMapper courseMapper;

    @RequestMapping(method = RequestMethod.GET)
    public APIResponse GetAll() {
        var result = courseMapper.selectAllWithGroupId();
        return APIDataResponse.Success(result);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public APIResponse AddCourse(@RequestBody Course course) {
        try {
            courseMapper.insertCourse(course);
            return APIResponse.Success();
        } catch (Exception e) {
            return APIResponse.Fail("4000", e.getLocalizedMessage());
        }
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public APIResponse deleteGroup(@PathVariable("id") String groupId){
        try{
            courseMapper.deleteById(groupId);
            return APIResponse.Success();
        } catch (Exception e) {
            return APIResponse.Fail("4000", e.getLocalizedMessage());
        }
    }
}
