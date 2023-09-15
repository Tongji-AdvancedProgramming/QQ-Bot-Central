package org.tongji.programming.dto.GroupService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidateGroupResult {
    private String message;
    private long botId;
    private String nickname;
}
