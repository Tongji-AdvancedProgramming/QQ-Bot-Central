package org.tongji.programming.dto.StudentImportService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tongji.programming.pojo.Student;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentImportResult {
    /**
     * 一个随机字符串，标识本次导入的解析结果
     */
    private String randomId;

    /**
     * 解析完成时的时间
     */
    private LocalDate resolvedTime;

    /**
     * 所有成功解析的学生
     */
    private int resolved;

    /**
     * 所有成功导入的学生
     */
    private List<Student> insertList;

    /**
     * 重复的学生
     */
    private int repeated;

    /**
     * 删去的学生
     * 设计笔记：什么情况下会删除？因转专业、退课等，被删去的学生，检查发现Excel中已无此人，可以删除
     */
    private List<Student> deleteList;

    /**
     * 同上，信息有更新时加入。
     */
    private List<Student> updateList;
}
