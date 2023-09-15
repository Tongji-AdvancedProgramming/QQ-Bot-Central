package org.tongji.programming.service;

import org.tongji.programming.DTO.cqhttp.bot.LoginInfo;

/**
 * 服务类：Bot自身相关服务
 */
public interface BotSelfService {
    /**
     * 获取当前登录的bot QQ号；若未登录，则返回0
     * @param noCache 停用缓存
     * @return QQ号
     */
    public LoginInfo getSelfId(boolean noCache);

    /**
     * 获取当前登录的bot QQ号；若未登录，则返回0
     * @return QQ号
     */
    public LoginInfo getSelfId();
}
