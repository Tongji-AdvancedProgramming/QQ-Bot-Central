package org.tongji.programming.impl;

import lombok.var;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tongji.programming.mapper.CourseMapper;
import org.tongji.programming.mapper.QQGroupMapper;
import org.tongji.programming.service.CourseService;

import java.util.List;

@Component
@DubboService
public class CourseServiceImpl implements CourseService {

    CourseMapper courseMapper;

    @Autowired
    public void setCourseMapper(CourseMapper courseMapper) {
        this.courseMapper = courseMapper;
    }

    @Override
    public List<String> getCourseIdFromQQGroupId(String QQGroupId) {
        return courseMapper.selectCourseIdByGroupId(QQGroupId);
    }
}
