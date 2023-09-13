package org.tongji.programming.controller;

import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.web.bind.annotation.*;
import org.tongji.programming.dto.APIDataResponse;
import org.tongji.programming.dto.APIResponse;
import org.tongji.programming.helper.JWTUtils;

@Slf4j
@RestController
@RequestMapping("user")
public class UserController {
    public static final String adminPassword;

    static {
        adminPassword = System.getenv("Bot_adminPass");
        if (adminPassword == null) {
            log.error("未配置Bot_adminPass，请检查环境变量");
            System.exit(1);
        }
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public APIResponse Login(@RequestParam String userName, @RequestParam String passWord){
        if(userName.equals("admin") && passWord.equals(adminPassword)){
            var token = JWTUtils.getToken("admin");
            return APIDataResponse.Success(token);
        }
        return APIResponse.Fail("4000","用户名或密码错误");
    }
}
