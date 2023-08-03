package org.tongji.programming.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "bot.group")
public class GroupLevelMapping {
    private List<Long> test = new ArrayList<>();
    private List<Long> normal = new ArrayList<>();
    private List<Long> careful = new ArrayList<>();
    private List<Long> restrict = new ArrayList<>();
}
