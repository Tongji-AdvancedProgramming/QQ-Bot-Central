package org.tongji.programming.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tongji.programming.pojo.QQGroup;

import java.util.List;

@Mapper
@Repository
public interface StudentLogMapper {
    int addLog(@Param("contentJson") String contentJson);
}
