package org.tongji.programming.http;

import com.dtflys.forest.annotation.*;
import org.tongji.programming.DTO.cqhttp.APIResponse;

public interface BotGroupService {
    /**
     * 获取群消息，如果机器人尚未加入群, group_create_time, group_level, max_member_count 和 member_count 将会为0
     *
     * @return `GroupInfo`
     */
    @Get("http://host.docker.internal:5700/get_group_info")
    APIResponse getGroupInfo(@Query("group_id") long groupId, @Query("no_cache") boolean noCache);

    /**
     * 获取群列表
     *
     * @param noCache 禁用缓存
     * @return List GroupInfo
     */
    @Get("http://host.docker.internal:5700/get_group_list")
    APIResponse getGroupList(@Query("no_cache") boolean noCache);


    /**
     * 获取群成员信息
     *
     * @param groupId 群号
     * @param userId  QQ号
     * @param noCache 是否停用缓存
     * @return GroupMemberInfo
     */
    @Get("http://host.docker.internal:5700/get_group_member_info")
    APIResponse getGroupMemberInfo(@Query("group_id") long groupId, @Query("user_id") long userId, @Query("no_cache") boolean noCache);

    /**
     * 获取群成员列表
     * @param groupId 群号
     * @param noCache 是否停用缓存
     * @return CheckResult[]?
     */
    @Get("http://host.docker.internal:5700/get_group_member_list")
    APIResponse getGroupMemberList(@Query("group_id") long groupId, @Query("no_cache") boolean noCache);

    /**
     * 踢人
     * @param groupId 群号
     * @param userId 用户QQ
     * @param rejectAddRequest 拒绝后续加群请求
     */
    @Get("http://host.docker.internal:5700/set_group_kick")
    void setGroupKick(@Query("group_id") long groupId, @Query("user_id") long userId, @Query("reject_add_request") boolean rejectAddRequest);

}
