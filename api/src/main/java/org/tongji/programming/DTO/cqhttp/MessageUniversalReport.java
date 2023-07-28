package org.tongji.programming.DTO.cqhttp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MessageUniversalReport extends UniversalReport {
    /**
     * 消息类型（private, group）
     */
    @JsonProperty("message_type")
    private String messageType;

    /**
     * 表示消息的子类型（group, public）
     */
    @JsonProperty("sub_type")
    private String subType;

    /**
     * 消息 ID
     */
    @JsonProperty("message_id")
    private String messageId;

    /**
     * 发送者 QQ 号
     */
    @JsonProperty("user_id")
    private String userId;

    /**
     * CQ 码格式的消息
     */
    @JsonProperty("raw_message")
    private String rawMessage;

    /**
     * 发送者信息
     */
    private MessageSender sender;
}
