package org.tongji.programming.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboService;
import org.tongji.programming.DTO.cqhttp.MessageUniversalResponse;
import org.tongji.programming.helper.JSONHelper;
import org.tongji.programming.service.HelloWorldService;

@DubboService
public class HelloWorldServiceImpl implements HelloWorldService {
    @Override
    public String sayHelloWorld() {
        var mapper = JSONHelper.getLossyMapper();
        var response = new MessageUniversalResponse();
        response.setReply("你好！");
        response.setAtSender(true);
        try{
            return mapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
