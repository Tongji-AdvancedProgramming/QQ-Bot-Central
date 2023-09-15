package org.tongji.programming.http;

import com.dtflys.forest.annotation.*;
import org.tongji.programming.DTO.cqhttp.APIResponse;
import org.tongji.programming.DTO.cqhttp.group.GetGroupInfoRequest;
import org.tongji.programming.DTO.cqhttp.group.GetGroupInfoResponse;

public interface BotGroupService {
    /**
     * 获取群消息，如果机器人尚未加入群, group_create_time, group_level, max_member_count 和 member_count 将会为0
     * @param request 请求体
     * @return 响应
     */
    @Get("http://localhost:5700/get_group_info")
    APIResponse getGroupInfo(@Query("group_id") long groupId, @Query("no_cache") boolean noCache);
}
