package org.tongji.programming.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.tongji.programming.pojo.Course;

import java.util.List;

@Mapper
@Repository
public interface CourseMapper {
    /**
     * 建表
     * @return 0表示成功，其余失败
     */
    Integer createTable();

    /**
     * 删表（高危操作，非测试用途禁止调用）
     * @return 0表示成功，其余失败
     */
    Integer deleteTable();

    /**
     * 查询全部
     * @return 课程列表
     */
    List<Course> selectAll();

    /**
     * 根据课号查询
     * @param id 课程编号
     * @return 学生
     */
    Course selectById(String id);

    /**
     * 根据课号删除课程
     * @param id 课程编号
     * @return 0表示成功，其余失败
     */
    Integer deleteById(String id);
}
