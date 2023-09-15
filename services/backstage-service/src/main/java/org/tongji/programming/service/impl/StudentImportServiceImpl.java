package org.tongji.programming.service.impl;

import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.tongji.programming.mapper.StudentMapper;
import org.tongji.programming.pojo.Student;
import org.tongji.programming.service.StudentImportService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.regex.Pattern;

@Component
public class StudentImportServiceImpl implements StudentImportService {

    @Autowired
    StudentMapper studentMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int resolvePlainText(InputStream fileStream, String course, String classId) throws IOException, RuntimeException {
        var reader = new BufferedReader(new InputStreamReader(fileStream));
        var pattern = Pattern.compile("(\\d{7})\\s(.*)");

        var students = new LinkedList<Student>();

        while (reader.ready()) {
            var line = reader.readLine();
            line = line.trim();

            var matcher = pattern.matcher(line);
            if (!matcher.matches()) {
                throw new RuntimeException(String.format("解析行“%s”时遇到异常：匹配失败", line));
            }

            var stuNo = matcher.group(1);
            var name = matcher.group(2);

            var student = Student.builder().stuNo(stuNo).name(name).major("").courseId(course).classId(classId).build();
            students.add(student);
        }

        return studentMapper.insertStudents(students);
    }

    // TODO
    @Override
    public int resolveCsv(InputStream fileStream) {
        return 0;
    }
}
