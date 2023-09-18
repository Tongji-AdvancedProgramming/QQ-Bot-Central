package org.tongji.programming;

import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.tongji.programming.pojo.Student;
import org.tongji.programming.service.StudentService;

@SpringBootTest
public class TestStudentValidate {

    @DubboReference
    StudentService studentService;

    @Test
    public void testStudentValidate(){
        //1754187-肖前-信14
        Student student = Student.builder().name("肖前").stuNo("1754187").courseId("100717").build();
        System.err.println(studentService.StudentValid(student));
    }
}
