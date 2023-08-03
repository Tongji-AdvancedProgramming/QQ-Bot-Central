package org.tongji.programming.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "bot.role")
public class PeopleRole {
    private List<Long> ta = new ArrayList<>();
    private List<Long> teacher = new ArrayList<>();
}
