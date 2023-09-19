package org.tongji.programming.service;

import org.tongji.programming.DTO.cqhttp.APIResponse;
import org.tongji.programming.pojo.CheckResult;

import java.util.List;

public interface CheckCardService {
    String sendMsg(String msg);

    List<CheckResult> handlelist(APIResponse response);

    String addAssistants();

    boolean isAssistants(Long userId);

    String checkCard(Long groupId, Long userId, boolean debug);
}
