package org.tongji.programming.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class APIResponse{
    private String code;
    private String msg;

    public static APIResponse Success(){
        return new APIResponse("10000","成功");
    }

    public static APIResponse Fail(String code,String msg){
        return new APIResponse(code,msg);
    }
}
