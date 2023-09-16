package org.tongji.programming.http;

import com.dtflys.forest.annotation.Get;
import org.tongji.programming.DTO.cqhttp.APIResponse;

public interface BotGoCqHttpService {
    /**
     * 获取bot状态
     * @return Status
     */
    @Get("http://localhost:5700/get_status")
    APIResponse getStatus();
}
