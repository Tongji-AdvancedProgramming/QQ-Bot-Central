package org.tongji.programming.DTO.cqhttp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 所有上报都将包含下面的有效通用数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UniversalReport {
    /**
     * 事件发生的unix时间戳
     */
    private long time;

    /**
     * 收到事件的机器人的 QQ 号
     */
    @JsonProperty("self_id")
    private long selfId;

    /**
     * 表示该上报的类型, 消息, 消息发送, 请求, 通知, 或元事件
     */
    @JsonProperty("post_type")
    private String postType;
}
