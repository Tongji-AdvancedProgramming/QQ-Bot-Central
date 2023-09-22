package org.tongji.programming.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "bot-config")
public class ConfigProviderConfiguration {
    private RedisProviderConfiguration redis = new RedisProviderConfiguration();
    private String file;
}
