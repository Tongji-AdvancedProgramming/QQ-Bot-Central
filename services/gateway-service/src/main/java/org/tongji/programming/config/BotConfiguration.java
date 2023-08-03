package org.tongji.programming.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "bot")
public class BotConfiguration {
    private PeopleRole role;
    private GroupLevelMapping group;
}

