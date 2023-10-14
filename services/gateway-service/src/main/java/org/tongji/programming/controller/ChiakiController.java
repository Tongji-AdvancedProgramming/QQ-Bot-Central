package org.tongji.programming.controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;
import org.tongji.programming.DTO.cqhttp.MessageUniversalReport;
import org.tongji.programming.annotations.CommandMapper.*;
import org.tongji.programming.enums.GroupLevel;
import org.tongji.programming.service.DemoService;
import org.tongji.programming.service.CheckCardService;
import org.tongji.programming.service.GroupUtilService;

@Component
@CommandController("chiaki")
public class ChiakiController {

    @DubboReference
    DemoService demoService;

    @CommandMapping("hello")
    public String hello(MessageUniversalReport event) {
        return demoService.chiakiSayHello();
    }

    @CommandMapping(value = "dj", minimumGroupLevel = GroupLevel.NORMAL)
    public String dingZhen(MessageUniversalReport event) {
        return demoService.djImage();
    }

    @CommandMapping(value = "渣哥", minimumGroupLevel = GroupLevel.CAREFUL)
    public String shenJian(MessageUniversalReport event) {
        return demoService.sjImage(event);
    }

    @DubboReference
    CheckCardService checkCardService;

    @CommandMapping("检查群名片")
    public String checkCard(MessageUniversalReport event){
        return checkCardService.checkCard(event.getGroupId(), event.getUserId(),false);
    }

    @CommandMapping("检查群名片 debug")
    public String checkCard_DebugMode(MessageUniversalReport event){
        return checkCardService.checkCard(event.getGroupId(), event.getUserId(),true);
    }

    @CommandMapping("update")
    public String updateAssistants(MessageUniversalReport event){
        return checkCardService.addAssistants();
    }

    @DubboReference
    GroupUtilService groupUtilService;

    @CommandMapping("test")
    public String test(MessageUniversalReport event){
        groupUtilService.test();
        return  null;
    }

    @CommandMapping("添加提醒")
    public String addReminder(MessageUniversalReport event){
        return groupUtilService.addReminder(event);
    };

    @CommandMapping("删除提醒")
    public String deleteReminder(MessageUniversalReport event){ return groupUtilService.deleteReminder(event); };

    @CommandMapping("查询提醒列表")
    public String getReminderList(MessageUniversalReport event){ return groupUtilService.selectAllReminder(event);};

    @CommandMapping("应用提醒")
    public String addGroupId(MessageUniversalReport event){ return groupUtilService.addGroupId(event);};

    @CommandMapping("取消应用提醒")
    public String deleteGroupId(MessageUniversalReport event){ return groupUtilService.deleteGroupId(event);};

}
