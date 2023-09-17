package org.tongji.programming.service;

import org.apache.dubbo.config.annotation.DubboService;
import org.tongji.programming.DTO.cqhttp.requestEvent.GroupRequestEvent;

@DubboService
public interface GroupUtilService {
    String groupRequestHandler(GroupRequestEvent event);
}
