package org.tongji.programming.pojo;

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
}
