package org.tongji.programming.DTO.cqhttp.bot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginInfo {
    /**
     * QQ号
     */
    @JsonProperty("user_id")
    private long userId;

    /**
     * 昵称
     */
    private String nickname;
}
