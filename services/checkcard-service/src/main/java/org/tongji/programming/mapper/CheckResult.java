package org.tongji.programming.mapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Property;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckResult {
    private long id;
    private String card;
    private int failedTimes;
    private String failedReason;
}
