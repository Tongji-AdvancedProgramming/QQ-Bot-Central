package org.tongji.programming.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.tongji.programming.service.DemoService;
import org.tongji.programming.service.GatewayService;

@Component
@RestController
public class EventController {
    @DubboReference
    DemoService demoService;

    @DubboReference
    GatewayService gatewayService;

    /**
     * 测试存活接口
     * @param name（一段字符串）
     * @return 一段字符串
     */
    @RequestMapping("/sayHello")
    public String sayHello(String name){
        return demoService.sayHello(name);
    }

    /**
     * 备注：因为CQ会把所有信息都发到这个接口，所以我们需要手动进行JSON处理。
     * 不能享受便捷的自动序列化/反序列化了，呜呜
     * @param rawEvent （JSON内容）
     * @return 返回的JSON
     */
    @JsonIgnore
    @RequestMapping(method = RequestMethod.POST)
    public String EventHandler(
            @RequestBody String rawEvent
    ){
        return gatewayService.handleEvent(rawEvent);
    }
}
