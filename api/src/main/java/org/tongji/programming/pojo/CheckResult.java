package org.tongji.programming.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckResult {
    /**
     * 成员QQ号
     */
    public Long studentId;

    /**
     * 学生群名片
     */
    public String card;

    /**
     * 角色
     */
    public String role;

    /**
     * 检查结果
     */
    public boolean checkresult;

    /**
     * 未通过理由
     */
    public String failedreason;

    /**
     * 被检测次数
     */
    public int failedtimes;
}
