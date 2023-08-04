package org.tongji.programming.controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;
import org.tongji.programming.DTO.cqhttp.MessageUniversalReport;
import org.tongji.programming.annotations.CommandMapper.*;
import org.tongji.programming.enums.GroupLevel;
import org.tongji.programming.service.DemoService;

@Component
@CommandController("chiaki")
public class ChiakiController {

    @DubboReference
    DemoService demoService;

    @CommandMapping("hello")
    public String hello(MessageUniversalReport event){
        return demoService.chiakiSayHello();
    }

    @CommandMapping(value = "dj",minimumGroupLevel = GroupLevel.NORMAL)
    public String dingZhen(MessageUniversalReport event){
        return demoService.djImage();
    }

    @CommandMapping(value = "渣哥",minimumGroupLevel = GroupLevel.NORMAL)
    public String shenJian(MessageUniversalReport event){
        return demoService.sjImage(event);
    }


}
