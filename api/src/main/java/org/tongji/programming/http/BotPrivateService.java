package org.tongji.programming.http;

import com.dtflys.forest.annotation.Get;
import com.dtflys.forest.annotation.Query;

public interface BotPrivateService {
    /**
     * 发送私聊信息
     * @param groupId 群号(必须是bot当管理员的群，一般设置为本群)
     * @param userId 对方的QQ号
     * @param message 拒绝后续加群请求
     * @param autoEscape 是否使用纯文本发送
     */
    @Get("http://host.docker.internal:5700/send_private_msg")
    void sendPrivateMsg(@Query("group_id") long groupId, @Query("user_id") long userId, @Query("message") String message, @Query("auto_escape") boolean autoEscape);
}
