package org.tongji.programming.dto.StudentImportService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentImportExcelStudent {
    private int index;
    private int studentId;
    private String name;
    private String englishName;
    private String gender;
    private int grade;
    private String school;
    private String major;
    private boolean isInternational;
}
