package org.tongji.programming.pojo;

import lombok.Builder;
import lombok.Data;

/**
 * 班级Pojo
 */
@Data
@Builder
public class Course {
    /**
     * 课号
     */
    private String id;

    /**
     * 课程名称
     */
    private String name;
}
