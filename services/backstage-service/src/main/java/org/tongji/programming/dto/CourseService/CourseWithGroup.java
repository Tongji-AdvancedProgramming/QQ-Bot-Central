package org.tongji.programming.dto.CourseService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseWithGroup {
    /**
     * 课号
     */
    private String id;

    /**
     * 课程名称
     */
    private String name;

    /**
     * 群号列表
     */
    private List<String> groups;
}
