package org.tongji.programming.http;

import com.dtflys.forest.annotation.Get;
import org.tongji.programming.DTO.cqhttp.APIResponse;

public interface BotSelfHttpService {
    @Get("http://host.docker.internal:5700/get_login_info")
    APIResponse getLoginInfo();
}
