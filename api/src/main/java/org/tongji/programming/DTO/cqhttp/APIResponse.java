package org.tongji.programming.DTO.cqhttp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CQHTTP的API通用返回头，详见：
 * https://docs.go-cqhttp.org/api/
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class APIResponse {
    /**
     * ok	api 调用成功;async	api 调用已经提交异步处理, 此时 retcode 为 1, 具体 api 调用是否成功无法得知
     */
    private String status;

    @JsonProperty("retcode")
    private String retCode;

    /**
     * 错误消息, 仅在 API 调用失败时有该字段
     */
    private String msg;

    /**
     * 对错误的详细解释(中文), 仅在 API 调用失败时有该字段
     */
    private String wording;

    /**
     * 具体的数据（将会被延迟反序列化）
     */
    private JSON data;
}
