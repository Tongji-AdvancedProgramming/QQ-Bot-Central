package org.tongji.programming.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboService;
import org.tongji.programming.DTO.cqhttp.MessageUniversalResponse;
import org.tongji.programming.helper.JSONHelper;
import org.tongji.programming.service.DemoService;

@DubboService
public class DemoServiceImpl implements DemoService {

    @Override
    public String sayHello(String name) {
        if (name == null) {
            return "Hello, Some one";
        }else{
            return String.format("Hello, %s", name);
        }
    }

    @Override
    public String chiakiSayHello() {
        var mapper = JSONHelper.getLossyMapper();
        var response = new MessageUniversalResponse();
        response.setReply("你向着名为绝望的希望微笑。");
        response.setAtSender(true);
        try{
            return mapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
