package org.tongji.programming.impl;

import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;
import org.tongji.programming.DTO.cqhttp.requestEvent.GroupRequestEvent;
import org.tongji.programming.DTO.cqhttp.requestEvent.RequestEventUniversal;
import org.tongji.programming.helper.JSONHelper;
import org.tongji.programming.service.GroupUtilService;
import org.tongji.programming.service.RequestService;

@Slf4j
@Component
public class RequestServiceImpl implements RequestService {

    @DubboReference
    GroupUtilService groupUtilService;

    @Override
    public String requestEventHandler(String eventRaw) {
        try{
            var mapper = JSONHelper.getLossyMapper();
            var requestRaw = mapper.readValue(eventRaw, RequestEventUniversal.class);

            if(requestRaw.getRequestType().equals("group")){
                // 申请加群，交其他服务处理
                var request = mapper.readValue(eventRaw, GroupRequestEvent.class);
                return groupUtilService.groupRequestHandler(request);
            }

            // 暂时不支持处理
            return "{}";
        } catch (Exception e) {
            log.error("消息网关处理请求“{}”时出现异常：{}",eventRaw,e.getLocalizedMessage());
            return "{}";
        }
    }
}
