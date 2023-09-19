package org.tongji.programming.pojo;

import com.google.common.base.Objects;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 学生的Pojo
 * 主键是StuNo和ClassId
 */
@Data
@Builder
public class Student implements Serializable {
    /**
     * 学号
     */
    private String stuNo;

    /**
     * 专业（2字）
     */
    private String major;

    /**
     * 姓名
     */
    private String name;

    /**
     * 课号
     */
    private String courseId;

    /**
     * 班号
     */
    private String classId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equal(stuNo, student.stuNo) && Objects.equal(major, student.major) && Objects.equal(name, student.name) && Objects.equal(courseId, student.courseId) && Objects.equal(classId, student.classId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(stuNo, major, name, courseId, classId);
    }
}
