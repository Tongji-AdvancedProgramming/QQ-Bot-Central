package org.tongji.programming.dto.StudentImportService;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentImportExcelClass {
    private String name;
    private String id;
    private String code;
    private int studentNum;

    @JsonProperty("class")
    private String clazz; // 'class' is a reserved keyword in Java, so using 'clazz' instead
    private String school;
    private String teacher;
    private String time;
    private List<StudentImportExcelStudent> students;
}
