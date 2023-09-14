package org.tongji.programming.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tongji.programming.pojo.QQGroup;

import java.util.List;

@Mapper
@Repository
public interface QQGroupMapper {
    /**
     * 根据课号筛选QQ群
     *
     * @param courseId 课号
     * @return 群号
     */
    List<QQGroup> selectByCourseId(@Param("courseId") String courseId);

    /**
     * 根据群号获取
     * @param id 群号
     * @return 群
     */
    QQGroup selectById(@Param("id") String id);
}
