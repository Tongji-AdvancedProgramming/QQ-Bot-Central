package org.tongji.programming.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.var;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class APIDataResponse extends APIResponse{
    private Object data;

    public static APIDataResponse Success(Object data){
        APIDataResponse resp = APIDataResponse.builder().data(data).build();
        resp.setMsg("成功");
        resp.setCode("10000");
        return resp;
    }
}
