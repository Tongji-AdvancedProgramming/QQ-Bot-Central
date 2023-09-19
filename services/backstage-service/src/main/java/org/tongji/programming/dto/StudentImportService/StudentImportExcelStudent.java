package org.tongji.programming.dto.StudentImportService;

import com.google.common.base.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tongji.programming.pojo.Student;

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

    public boolean isSame(Student student) {
        if (student == null) return false;
        return Objects.equal(this.name, student.getName()) && Objects.equal(String.valueOf(this.studentId), student.getStuNo()) && Objects.equal(this.major, student.getMajor());
    }
}
