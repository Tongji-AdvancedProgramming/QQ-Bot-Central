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
public class RequestEventUniversal extends UniversalReport implements Serializable {
    @JsonProperty("request_type")
    private String requestType;
}
