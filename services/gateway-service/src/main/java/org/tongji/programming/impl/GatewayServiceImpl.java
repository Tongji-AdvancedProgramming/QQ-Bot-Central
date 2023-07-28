package org.tongji.programming.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.tongji.programming.DTO.cqhttp.UniversalReport;
import org.tongji.programming.service.GatewayService;
import org.tongji.programming.service.MessageService;

@DubboService
public class GatewayServiceImpl implements GatewayService {

    @DubboReference
    MessageService messageService;

    @Override
    public String handleEvent(String eventRaw) {
        try{
            var mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            var eventPreload = mapper.readValue(eventRaw, UniversalReport.class);

            switch (eventPreload.getPostType()){
                case "message":
                    return messageService.messageEventHandler(eventRaw);
                case "request":
                case "notice":
                case "meta_event":
                case "message_sent":
                default:
                    return "{}";
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
