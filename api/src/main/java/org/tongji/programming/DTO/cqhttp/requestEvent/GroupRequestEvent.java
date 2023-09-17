package org.tongji.programming.DTO.cqhttp.requestEvent;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.tongji.programming.DTO.cqhttp.UniversalReport;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class GroupRequestEvent extends RequestEventUniversal implements Serializable {
    @JsonProperty("sub_type")
    private String subType;

    @JsonProperty("group_id")
    private String groupId;

    @JsonProperty("user_id")
    private String userId;

    /**
     * 验证消息
     */
    private String comment;

    private String flag;
}
