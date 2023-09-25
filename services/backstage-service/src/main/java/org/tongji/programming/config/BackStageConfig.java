package org.tongji.programming.config;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.tongji.programming.annotations.ConfigAnnotation.*;

@Data
@Component
@BotConfig("BackStage")
public class BackStageConfig {
    @DefaultValue("(测试消息未设置)")
    private String testMessage;
}


