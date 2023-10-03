package org.tongji.programming.service;

import lombok.SneakyThrows;
import org.apache.dubbo.config.annotation.DubboService;
import org.tongji.programming.DTO.cqhttp.MessageUniversalReport;
import org.tongji.programming.DTO.cqhttp.NoticeUniversalReport;
import org.tongji.programming.DTO.cqhttp.requestEvent.GroupRequestEvent;

@DubboService
public interface GroupUtilService {
    String groupRequestHandler(GroupRequestEvent event);

    void groupMsgStore(MessageUniversalReport event);

    String groupRecallHandler(NoticeUniversalReport event);

    @SneakyThrows
    String groupRepeatHandler(MessageUniversalReport event);

    void test();
}
