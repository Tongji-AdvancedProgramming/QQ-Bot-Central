package org.tongji.programming.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.tongji.programming.ConfigProvider;
import org.tongji.programming.DTO.cqhttp.MessageUniversalReport;
import org.tongji.programming.DTO.cqhttp.MessageUniversalResponse;
import org.tongji.programming.helper.JSONHelper;
import org.tongji.programming.service.DemoService;

import java.util.regex.Pattern;

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

    @Autowired
    ConfigProvider configProvider;

    @Override
    public String chiakiSayHello() {
        var mapper = JSONHelper.getLossyMapper();
        var response = new MessageUniversalResponse();

        System.err.println(configProvider.get("AntiRecall","forEveryone"));
        //response.setReply("你向着名为绝望的希望微笑。");
        //response.setAtSender(true);
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
        try {
            return mapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    static Pattern sjPattern = Pattern.compile("chiaki 渣哥 (.*)");

    @Override
    public String sjImage(MessageUniversalReport event) {
        var message = event.getRawMessage();
        var matcher = sjPattern.matcher(message);
        if (matcher.find()) {
            String index = matcher.group(1);
            boolean noCache = false;
            if(index.endsWith("nocache")){
                index = index.substring(0,index.length()-7);
                noCache = true;
            }
            var mapper = JSONHelper.getLossyMapper();
            var response = new MessageUniversalResponse();
            if(index.equals("随机") || noCache)
                response.setReply(String.format("[CQ:image,file=https://shenjian.cinea.cc/get?index=%s,cache=0]", index));
            else
                response.setReply(String.format("[CQ:image,file=https://shenjian.cinea.cc/get?index=%s,cache=1]", index));
            try {
                return mapper.writeValueAsString(response);
            } catch (JsonProcessingException e) {
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        } else {
            return "{}";
        }
    }
}
