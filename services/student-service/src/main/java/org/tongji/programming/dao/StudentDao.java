package org.tongji.programming.dao;

import org.tongji.programming.pojo.Student;

import java.util.List;

public interface StudentDao {
    /**
     * 查询表是否存在
     * @return 是否存在
     */
    Boolean tableExists();

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
     * @return 学生列表
     */
    List<Student> selectAll();

    /**
     * 根据学号和课号查询
     * @param id 学号
     * @param classNo 课号
     * @return 学生
     */
    Student selectById(Integer id, Integer classNo);

    /**
     * 根据学号和课号删除学生
     * @param id 学号
     * @param classNo 课号
     * @return 0表示成功，其余失败
     */
    Integer deleteById(Integer id, Integer classNo);

    /**
     * 删除某个班级的所有学生
     * @return 0表示成功，其余失败
     */
    Integer deleteByClassNo(Integer classNo);
}
