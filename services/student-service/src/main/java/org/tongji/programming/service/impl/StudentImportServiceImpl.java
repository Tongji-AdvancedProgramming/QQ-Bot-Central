package org.tongji.programming.service.impl;

import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tongji.programming.mapper.StudentMapper;
import org.tongji.programming.service.StudentImportService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Component
public class StudentImportServiceImpl implements StudentImportService {

    @Autowired
    StudentMapper studentMapper;

    @Override
    public int resolvePlainText(InputStream fileStream) throws IOException {
        var reader = new BufferedReader(new InputStreamReader(fileStream));

        while(reader.ready()){
            var line = reader.readLine();
            line = line.trim();

        }

        return 0;
    }

    // TODO
    @Override
    public int resolveCsv(InputStream fileStream) {
        return 0;
    }
}
