package org.tongji.programming.dto.StudentImportService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentImportResult {
    /**
     * 所有成功解析的学生
     */
    private int resolved;

    /**
     * 所有成功导入的学生
     */
    private int imported;

    /**
     * 重复的学生
     */
    private int repeated;

    /**
     * 删去的学生
     * 设计笔记：什么情况下会删除？因转专业、退课等，被删去的学生，检查发现Excel中已无此人，可以删除
     */
    private int removed;

    /**
     * 同上，信息有更新时加入。
     */
    private int updated;
}
