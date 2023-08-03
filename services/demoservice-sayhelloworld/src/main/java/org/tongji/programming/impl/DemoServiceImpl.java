package org.tongji.programming.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tongji.programming.DTO.cqhttp.MessageUniversalResponse;
import org.tongji.programming.helper.JSONHelper;
import org.tongji.programming.service.DemoService;

@DubboService
public class DemoServiceImpl implements DemoService {

    final Logger logger = LoggerFactory.getLogger(DemoServiceImpl.class);  // Logger不强制添加，但是如果没有你debug会很累（笑）

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
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public String djImage() {
        var mapper = JSONHelper.getLossyMapper();
        var response = new MessageUniversalResponse();
        response.setReply("[CQ:image,file=https://dingzhen.cinea.cc/get,cache=0]");
        try{
            return mapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
