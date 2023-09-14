package org.tongji.programming.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
     * 关联课号
     */
    private String courseId;

    /**
     * 备注
     */
    private String note;
}
