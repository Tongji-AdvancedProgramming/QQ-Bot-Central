package org.tongji.programming.DTO.cqhttp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeUniversalReport {
    @JsonProperty("notice_type")
    private String noticeType;

    /**
     * 其他字段会被自动装入此处
     */
    @JsonIgnoreProperties
    private Map<String, Object> extra;
}
