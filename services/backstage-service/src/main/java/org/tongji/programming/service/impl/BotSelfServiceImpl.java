package org.tongji.programming.service.impl;

import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tongji.programming.DTO.cqhttp.bot.LoginInfo;
import org.tongji.programming.dto.APIResponse;
import org.tongji.programming.http.BotSelfHttpService;
import org.tongji.programming.service.BotSelfService;

import javax.annotation.Resource;

@Component
public class BotSelfServiceImpl implements BotSelfService {
    private LoginInfo loginInfoCache;

    @Resource
    BotSelfHttpService botSelfHttpService;

    @Override
    public LoginInfo getSelfId(boolean noCache) {
        if(noCache || loginInfoCache==null){
            try{
                var response = botSelfHttpService.getLoginInfo();
                if(!response.getStatus().equals("ok")){
                    throw new RuntimeException();
                }
                loginInfoCache = response.getData().toJavaObject(LoginInfo.class);
            }catch (Exception e){
                loginInfoCache = null;
            }
        }
        return loginInfoCache;
    }

    @Override
    public LoginInfo getSelfId() {
        return getSelfId(false);
    }
}
