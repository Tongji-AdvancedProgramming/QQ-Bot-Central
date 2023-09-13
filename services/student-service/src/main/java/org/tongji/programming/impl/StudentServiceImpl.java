package org.tongji.programming.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.tongji.programming.pojo.Student;
import org.tongji.programming.service.StudentService;

@DubboService
public class StudentServiceImpl implements StudentService {
    @Override
    public int StudentValid(Student student) {
        return 0;
    }
}
