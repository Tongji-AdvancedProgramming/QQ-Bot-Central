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
public class GetGroupInfoResponse {
    @JsonProperty("group_id")
    private long groupId;

    @JsonProperty("group_name")
    private String groupName;

    @JsonProperty("group_memo")
    private String groupMemo;

    @JsonProperty("group_create_time")
    private long groupCreateTime;

    @JsonProperty("group_level")
    private long groupLevel;

    @JsonProperty("member_count")
    private int memberCount;

    @JsonProperty("max_member_count")
    private int maxMemberCount;
}
