package org.tongji.programming.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tongji.programming.dto.CourseService.CourseWithGroup;
import org.tongji.programming.pojo.Course;

import java.util.List;

@Mapper
@Repository
public interface CourseMapper {
    /**
     * 查询全部
     * @return 课程列表
     */
    List<Course> selectAll();

    /**
     * 查询全部（包含QQ群）
     * @return 课程列表
     */
    List<CourseWithGroup> selectAllWithGroupId();

    /**
     * 根据课号查询
     * @param id 课程编号
     * @return 课程
     */
    Course selectById(String id);

    /**
     * 根据课号删除课程
     * @param id 课程编号
     * @return 0表示成功，其余失败
     */
    Integer deleteById(String id);

    /**
     * 添加一个课程
     * @return 1表示成功，其余失败
     */
    Integer insertCourse(@Param("course") Course course);
}
