package org.tongji.programming.service;

import org.apache.dubbo.config.annotation.DubboService;

import java.util.List;

@DubboService
public interface CourseService {

    /**
     * 由QQ群号获取课程编号
     * @param QQGroupId QQ群号
     * @return 课程编号，用于和 StudentService 交互时使用；如果不存在，size为0。
     */
    public List<String> getCourseIdFromQQGroupId(String QQGroupId);

}

