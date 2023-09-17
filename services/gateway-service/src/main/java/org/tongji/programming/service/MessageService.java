/**
 * 消息服务
 * 维护者：张尧
 */

package org.tongji.programming.service;

import org.apache.dubbo.config.annotation.DubboService;

public interface MessageService {

    String messageEventHandler(String eventRaw);

}
