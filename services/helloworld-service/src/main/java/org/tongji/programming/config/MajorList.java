package org.tongji.programming.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
@Data
@Configuration
@ConfigurationProperties(prefix = "checkcard.majorlist")
public class MajorList {
    public List<String> majorlist = new ArrayList<>();
}
