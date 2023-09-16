package org.tongji.programming.dto.StudentService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tongji.programming.pojo.Student;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetStudents {
    private long totalNum;
    private List<Student> students;
}
