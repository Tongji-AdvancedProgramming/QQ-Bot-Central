package org.tongji.programming.DTO.cqhttp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageSender {
    @JsonProperty("user_id")
    private int userId;

    private String nickname;

    private String sex;

    private int age;

    @JsonProperty("group_id")
    private int groupId;

    private String card;

    private String area;

    /**
     * 成员等级
     */
    private String level;

    /**
     * 角色, owner 或 admin 或 member
     */
    private String role;

    private String title;
}
