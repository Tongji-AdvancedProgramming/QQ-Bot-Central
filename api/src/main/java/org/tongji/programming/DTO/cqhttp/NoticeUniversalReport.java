package org.tongji.programming.DTO.cqhttp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class NoticeUniversalReport implements Serializable {
    @JsonProperty("notice_type")
    private String noticeType;

    @JsonProperty("group_id")
    private Long groupId;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("message_id")
    private Long messageId;

    @JsonProperty("operator_id")
    private Long operatorId;

    /**
     * 其他字段会被自动装入此处
     */
    @JsonIgnoreProperties
    private Map<String, Object> extra;
}
