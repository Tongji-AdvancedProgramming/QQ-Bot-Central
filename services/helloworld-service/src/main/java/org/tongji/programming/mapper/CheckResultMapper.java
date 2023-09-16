package org.tongji.programming.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@Repository
public interface CheckResultMapper {

    /**
     * 添加一个学生
     * @return 1表示成功，其余失败
     */
    Integer insertCheckResult(CheckResult checkResult);

    /**
     * 根据QQ号查询检查结果
     *
     * @param id QQ号
     * @return 检查结果
     */
    CheckResult selectCheckResultById(@Param("id") Long id);

    /**
     * 更新数据
     * @return 1表示成功，其余失败
     */
    Integer updateFailedTimesById(@Param("Id") Long Id,@Param("failed_times") int failed_times);

}
