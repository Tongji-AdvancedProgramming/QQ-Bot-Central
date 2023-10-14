package org.tongji.programming.mapper;

import com.dtflys.forest.annotation.Query;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ReminderMapper {
    /**
     * 添加提醒事件
     *
     */
    void insertReminder(Reminder reminder);

    /**
     * 根据QQ号删除提醒事件
     *
     * @param id 序列号
     */
    void deleteById(@Param("id") int id);
    List<Reminder> selectAll();

    /**
     * 根据QQ号查询提醒事件
     *
     * @param id 序列号
     */
    Reminder selectById(@Param("id") int id);


    /**
     * 根据QQ号查询提醒事件
     *
     * @param groupId QQ群号
     * @param id 序列号
     */
    void updateGroupId(@Param("groupId") String groupId,@Param("id") int id);
}
