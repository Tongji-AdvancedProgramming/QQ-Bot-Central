package org.tongji.programming.DTO.cqhttp.group;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 群成员信息
 * <a href="https://docs.go-cqhttp.org/api/#%E8%8E%B7%E5%8F%96%E7%BE%A4%E6%88%90%E5%91%98%E4%BF%A1%E6%81%AF">查看文档</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupMemberInfo {
    private long groupId;
    private long userId;
    private String nickname;
    private String card;
    private String sex;
    private int age;
    private String area;
    private int joinTime;
    private int lastSentTime;
    private String level;
    private String role;
    private boolean unfriendly;
    private String title;
    private long title_expire_time;
    private boolean cardChangeable;
    private long shutUpTimestamp;
}
