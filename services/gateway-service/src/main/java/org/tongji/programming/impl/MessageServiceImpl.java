package org.tongji.programming.impl;

import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tongji.programming.DTO.cqhttp.MessageUniversalReport;
import org.tongji.programming.annotations.CommandMapper;
import org.tongji.programming.helper.JSONHelper;
import org.tongji.programming.service.DemoService;
import org.tongji.programming.service.GroupUtilService;
import org.tongji.programming.service.MessageService;
import lombok.var;
import org.tongji.programming.service.RestrictLevelService;

@Component
public class MessageServiceImpl implements MessageService {

    @DubboReference
    DemoService demoService;

    @DubboReference
    GroupUtilService groupUtilService;

    @Autowired
    CommandMapper commandMapper;

    @Autowired
    RestrictLevelService restrictLevelService;

    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Override
    public String messageEventHandler(String eventRaw) {
        try {
            var mapper = JSONHelper.getLossyMapper();
            var event = mapper.readValue(eventRaw, MessageUniversalReport.class);

            groupUtilService.groupMsgStore(event);
            groupUtilService.groupRepeatHandler(event);
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
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
