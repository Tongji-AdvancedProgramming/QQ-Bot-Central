package org.tongji.programming.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Data
@Configuration
@ConfigurationProperties(prefix = "bot-config")
@EnableScheduling
public class ConfigProviderConfiguration {
    private RedisProviderConfiguration redis = new RedisProviderConfiguration();
    private String file;
}
