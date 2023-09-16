package org.tongji.programming.dto.BotService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BotStatus {
    /**
     * 系统运行状态（一段文本）
     */
    private String status;

    /**
     * 三种状态类型：ok,warn,fail
     */
    private String type;

    /**
     * 掉线次数
     */
    private int lost;

    /**
     * 丢包率
     */
    private double losePackage;

    /**
     * 加入群数
     */
    private int groups;

    /**
     * 最后一条信息的时间
     */
    private long lastMessage;
}
