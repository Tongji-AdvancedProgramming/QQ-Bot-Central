package org.tongji.programming.helper;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;

/**
 * @author admin
 */
@Slf4j
public class JWTUtils {
    private static final String signKey;

    static {
        signKey = System.getenv("Bot_JwtKey");
        if (signKey == null) {
            log.error("未配置Bot_JwtKey，请检查环境变量");
            System.exit(1);
        }
    }

    /**
     * 获取token
     * @param userName 用户名
     * @return token
     */
    public static String getToken(String userName) {
        Calendar instance = Calendar.getInstance();
        //默认令牌过期时间7天
        instance.add(Calendar.DATE, 7);

        JWTCreator.Builder builder = JWT.create();
        builder.withClaim("username", userName);

        return builder.withExpiresAt(instance.getTime())
                .sign(Algorithm.HMAC256(signKey));
    }

    /**
     * 验证token合法性 成功返回token
     */
    public static boolean verify(String token) throws RuntimeException {
        if(token.isEmpty()){
            throw new RuntimeException("token不能为空");
        }

        JWTVerifier build = JWT.require(Algorithm.HMAC256(signKey)).build();
        return build.verify(token) != null;
    }
}
