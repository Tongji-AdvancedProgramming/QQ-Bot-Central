package org.tongji.programming.controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;
import org.tongji.programming.DTO.cqhttp.MessageUniversalReport;
import org.tongji.programming.annotations.CommandMapper;
import org.tongji.programming.service.HelloWorldService;

@Component
@CommandMapper.CommandController("小南")
public class HelloWorldController {

    @DubboReference
    HelloWorldService helloWorldService;

    @CommandMapper.CommandMapping("你好")
    public String hello(MessageUniversalReport event) {
        return helloWorldService.sayHelloWorld();
    }
}
