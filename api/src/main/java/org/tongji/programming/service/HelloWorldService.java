package org.tongji.programming.service;

import com.dtflys.forest.annotation.Get;
import com.dtflys.forest.annotation.Query;
import org.tongji.programming.DTO.cqhttp.APIResponse;
import org.tongji.programming.pojo.CheckResult;

import java.util.List;

public interface HelloWorldService {
    String sendMsg(String msg);

    @Get("http://127.0.0.1:5700/get_group_member_list")
    APIResponse getList(@Query("group_id") Long groupId);

    List<CheckResult> handlelist(APIResponse response);

    String addAssistants();

    boolean isAssistants(Long userId);

    String checkCard(Long groupId, Long userId);

    @Get("http://127.0.0.1:5700/set_group_kick")
    void kickMember(@Query("group_id") Long groupId,@Query("user_id") long studentId);

}
