package org.tongji.programming.DTO.cqhttp.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetGroupInfoRequest {
    @JsonProperty("group_id")
    private long groupId;

    @JsonProperty("no_cahce")
    private boolean noCache;
}
