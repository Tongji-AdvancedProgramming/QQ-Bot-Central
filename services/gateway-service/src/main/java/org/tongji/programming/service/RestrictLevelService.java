package org.tongji.programming.service;

import org.apache.dubbo.config.annotation.DubboService;
import org.tongji.programming.DTO.cqhttp.MessageUniversalReport;
import org.tongji.programming.enums.GroupLevel;

@DubboService
public interface RestrictLevelService {
    boolean restrictTo(GroupLevel target, long groupId);

}
