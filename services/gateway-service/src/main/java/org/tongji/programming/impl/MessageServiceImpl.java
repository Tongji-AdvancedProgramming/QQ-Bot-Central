package org.tongji.programming.impl;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.tongji.programming.DTO.cqhttp.MessageUniversalReport;
import org.tongji.programming.annotations.CommandMapper;
import org.tongji.programming.enums.GroupLevel;
import org.tongji.programming.helper.JSONHelper;
import org.tongji.programming.service.DemoService;
import org.tongji.programming.service.MessageService;
import lombok.var;
import org.tongji.programming.service.RestrictLevelService;

@DubboService
public class MessageServiceImpl implements MessageService {

    @DubboReference
    DemoService demoService;

    @Autowired
    CommandMapper commandMapper;

    @DubboReference
    RestrictLevelService restrictLevelService;

    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Override
    public String messageEventHandler(String eventRaw) {
        try {
            logger.info(String.format("消息网关处理消息：%s", eventRaw));
            var mapper = JSONHelper.getLossyMapper();
            var event = mapper.readValue(eventRaw, MessageUniversalReport.class);

            /*
             * 从这里开始，你可以加入你的处理逻辑
             * （例如，判断某种情况发生时，调用你的service）
             *
             * 你的service应当返回已经转换为JSON的MessageUniversalResponse
             *
             * 下面的DemoService提供了一个范例
             */

//            var rawMessage = event.getRawMessage();
//            if (rawMessage.startsWith("chiaki")) {
//                if (rawMessage.startsWith("chiaki hello")) {
//                    return demoService.chiakiSayHello();
//                } else if (
//                        rawMessage.startsWith("chiaki dj")
//                ) {
//                    return demoService.djImage();
//                }
//            }
//
//            return "{}";

            return commandMapper.handleCommand(event);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String sendMessage() {
        return null;
    }
}
