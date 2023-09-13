package org.tongji.programming;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.tongji.programming.mapper.StudentMapper;
import org.tongji.programming.pojo.Student;

import javax.sql.DataSource;
import java.io.Console;
import java.sql.Connection;
import java.sql.SQLException;
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
    @Test
    public void testStudent(){
        List<Student> userLogins = studentMapper.selectAll();
        System.out.println(userLogins);
    }
}
