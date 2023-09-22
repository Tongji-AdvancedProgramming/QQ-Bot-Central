package org.tongji.programming.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;
import org.tongji.programming.DTO.cqhttp.NoticeUniversalReport;
import org.tongji.programming.helper.JSONHelper;
import org.tongji.programming.service.GroupUtilService;
import org.tongji.programming.service.NoticeService;
@Slf4j
@Component
public class NoticeServiceImpl implements NoticeService {

    @DubboReference
    GroupUtilService groupUtilService;
    @Override
    public String noticeEventsHandler(String eventRaw) {
        try {
            var mapper = JSONHelper.getLossyMapper();
            var noticeRaw = mapper.readValue(eventRaw, NoticeUniversalReport.class);

            //消息撤回处理
            if(noticeRaw.getNoticeType().equals("group_recall")){
                var response = groupUtilService.groupRecallHandler(noticeRaw);
                log.info("外部例程返回：{}", response);
                return response;
            }
        } catch (JsonProcessingException e) {
            log.error("消息网关处理请求“{}”时出现异常：{}", eventRaw, e.getLocalizedMessage());
            return "{}";
        }

        return null;
    }
}
