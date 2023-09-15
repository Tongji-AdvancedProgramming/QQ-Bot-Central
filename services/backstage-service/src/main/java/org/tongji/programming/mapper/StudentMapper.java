package org.tongji.programming.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tongji.programming.pojo.Student;

import java.util.List;

@Mapper
@Repository
public interface StudentMapper {
    /**
     * 查询全部
     * @return 学生列表
     */
    List<Student> selectAll();

    /**
     * 分页查询
     * @param offset 已经计算好的offset
     * @param pageSize 页面大小
     * @return 学生列表
     */
    List<Student> selectWithPage(@Param("pageSize") long pageSize, @Param("offset") long offset);

    /**
     * 分页查询
     * @param offset 已经计算好的offset
     * @param pageSize 页面大小
     * @param courseId 课号
     * @return 学生列表
     */
    List<Student> selectWithPagByCourse(@Param("pageSize") long pageSize, @Param("offset") long offset, @Param("courseId") String courseId);

    /**
     * 根据学号和课号查询
     * @param id 学号
     * @param courseId 课号
     * @return 学生
     */
    Student selectById(@Param("id") String id, @Param("courseId") String courseId);

    /**
     * 根据学号和课号删除学生
     * @param id 学号
     * @param courseId 课号
     * @return 大于0表示成功，其余失败
     */
    Integer deleteById(@Param("id") String id, @Param("courseId") String courseId);

    /**
     * 删除某个班级的所有学生
     * @return 1表示成功，其余失败
     */
    Integer deleteByClassNo(@Param("courseId") String courseId);

    /**
     * 添加一个学生
     * @return 1表示成功，其余失败
     */
    Integer insertStudent(@Param("student") Student student);

    /**
     * 添加多个学生
     * @return 等于len表示成功，其余失败
     */
    Integer insertStudents(@Param("student") List<Student> student);
}
