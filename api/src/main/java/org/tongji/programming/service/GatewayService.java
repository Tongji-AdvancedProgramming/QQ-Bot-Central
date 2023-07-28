/**
 * 消息“网关”服务
 * 维护者：张尧
 */

package org.tongji.programming.service;

/**
 * 用途：识别事件类型，选择合适的service调用
 */
public interface GatewayService {

    String handleEvent(String eventRaw);

}
