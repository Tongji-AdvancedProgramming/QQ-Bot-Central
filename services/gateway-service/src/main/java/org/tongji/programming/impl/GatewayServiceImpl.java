package org.tongji.programming.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.tongji.programming.DTO.cqhttp.UniversalReport;
import org.tongji.programming.service.GatewayService;
import org.tongji.programming.service.MessageService;
import org.tongji.programming.service.NoticeService;
import org.tongji.programming.service.RequestService;

@Slf4j
@DubboService
public class GatewayServiceImpl implements GatewayService {

    @Autowired
    MessageService messageService;

    @Autowired
    RequestService requestService;

    @Autowired
    NoticeService noticeService;

    @Override
    public String handleEvent(String eventRaw) {
        try{
            var mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            var eventPreload = mapper.readValue(eventRaw, UniversalReport.class);

            if(!eventPreload.getPostType().equals("meta_event"))
                log.info("消息网关处理消息：{}", eventRaw);

            switch (eventPreload.getPostType()){
                case "message":
                    return messageService.messageEventHandler(eventRaw);
                case "request":
                    return requestService.requestEventHandler(eventRaw);
                case "notice":
                    return noticeService.noticeEventsHandler(eventRaw);
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
