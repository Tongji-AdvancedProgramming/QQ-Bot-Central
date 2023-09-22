package org.tongji.programming.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "bot-config.redis")
public class RedisProviderConfiguration {
    private int db = 0;
    private String prefix = "";
}
