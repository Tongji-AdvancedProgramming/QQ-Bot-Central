package org.tongji.programming;

import lombok.var;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.tongji.programming.mapper.CourseMapper;
import org.tongji.programming.mapper.StudentMapper;
import org.tongji.programming.pojo.Course;
import org.tongji.programming.pojo.Student;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class StudentServiceTest {
    @Autowired
    DataSource dataSource;

    @Test
    void contextLoads() throws SQLException {
        System.out.println(dataSource.getClass());
        Connection connection = dataSource.getConnection();
        System.out.println(connection);

        connection.close();
    }

    @Autowired
    StudentMapper studentMapper;

    @Autowired
    CourseMapper courseMapper;

    @Test
    public void testStudent(){
        courseMapper.deleteTable();
        courseMapper.createTable();
        studentMapper.deleteTable();
        studentMapper.createTable();

        List<Student> userLogins = studentMapper.selectAll();
        System.out.println(userLogins);

        var newCourse = Course.builder().name("高程").id("1008801").build();
        var newStudents = new ArrayList<Student>();

        newStudents.add(Student.builder().name("小明").stuNo("2152010").major("信01").courseId("1008801").build());
        newStudents.add(Student.builder().name("小红").stuNo("2152011").major("信02").courseId("1008801").build());
        courseMapper.insertCourse(newCourse);
        studentMapper.insertStudents(newStudents);

        userLogins = studentMapper.selectAll();
        System.out.println(userLogins);
    }
}
