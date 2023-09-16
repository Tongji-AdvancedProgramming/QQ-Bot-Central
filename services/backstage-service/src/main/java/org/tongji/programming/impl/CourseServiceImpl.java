package org.tongji.programming.impl;

import lombok.var;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tongji.programming.mapper.QQGroupMapper;
import org.tongji.programming.service.CourseService;

@Component
@DubboService
public class CourseServiceImpl implements CourseService {

    QQGroupMapper qqGroupMapper;

    @Autowired
    public void setQqGroupMapper(QQGroupMapper qqGroupMapper) {
        this.qqGroupMapper = qqGroupMapper;
    }

    @Override
    public String getCourseIdFromQQGroupId(String QQGroupId) {
        var group = qqGroupMapper.selectById(QQGroupId);
        if (group != null)
            return group.getCourseId();
        else
            return null;
    }
}
