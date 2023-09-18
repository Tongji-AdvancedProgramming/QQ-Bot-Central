package org.tongji.programming.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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
    Integer updateFailedById(@Param("id") Long Id,@Param("failedTimes") int failed_times,@Param("failedReason") String failedReason);

    /**
     * 获取所有数据
     *
     * @return 检查结果
     */
    List<CheckResult> selectAll();

    /**
     * 根据QQ号删除学生信息
     *
     */
    void deleteById(@Param("id") Long id);

}
