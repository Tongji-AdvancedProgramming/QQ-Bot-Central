package org.tongji.programming.mapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reminder {
    private int id; // 序列号
    private String event; // 需要提醒的内容
    private String week; // 每周的周几提醒
    private String time; // 这一天的什么时候提醒
    private String groupId; // 应用该提醒的群号

}
