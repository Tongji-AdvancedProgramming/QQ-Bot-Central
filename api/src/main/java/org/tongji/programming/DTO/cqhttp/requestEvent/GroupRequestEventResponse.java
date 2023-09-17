package org.tongji.programming.DTO.cqhttp.requestEvent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupRequestEventResponse {
    /**
     * 允许留空，表示不处理。
     */
    @Nullable
    private Boolean approve;

    @Nullable
    private String reason;
}
