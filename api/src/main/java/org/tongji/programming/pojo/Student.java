package org.tongji.programming.pojo;

import lombok.Data;

/**
 * 学生的Pojo
 * 主键是StuNo和ClassId
 */
@Data
public class Student {
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
}
