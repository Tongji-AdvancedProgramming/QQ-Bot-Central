package org.tongji.programming.DTO.cqhttp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageSender implements Serializable {
    @JsonProperty("user_id")
    private long userId;

    private String nickname;

    private String sex;

    private int age;

    @JsonProperty("group_id")
    private long groupId;

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
