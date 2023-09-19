package org.tongji.programming.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QQGroup {
    /**
     * 群号
     */
    private String id;

    /**
     * 备注
     */
    private String note;
}
