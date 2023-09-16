package org.tongji.programming.impl;

import lombok.var;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tongji.programming.mapper.StudentMapper;
import org.tongji.programming.pojo.Student;
import org.tongji.programming.service.StudentService;

@Component
@DubboService
public class StudentServiceImpl implements StudentService {

    StudentMapper studentMapper;

    @Autowired
    public void setStudentMapper(StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
    }

    @Override
    public int StudentValid(Student student) {
        var studentEntity = studentMapper.selectById(student.getStuNo(),student.getCourseId());

        if(studentEntity==null){
            return 2;
        }

        if(!studentEntity.getName().equals(student.getName())){
            return 1;
        }

        return 0;
    }
}
