package org.tongji.programming.DTO.cqhttp.cqhttp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Status {
    @JsonProperty("app_initialized")
    private boolean appInitialized;

    @JsonProperty("app_enabled")
    private boolean appEnabled;

    @JsonProperty("plugins_good")
    private boolean pluginsGood;

    @JsonProperty("app_good")
    private boolean appGood;

    private boolean online;

    private boolean good;

    private Statistics stat;
}
