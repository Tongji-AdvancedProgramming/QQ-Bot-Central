package org.tongji.programming.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 班级Pojo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
