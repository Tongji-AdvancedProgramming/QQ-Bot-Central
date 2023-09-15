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
    }
}
