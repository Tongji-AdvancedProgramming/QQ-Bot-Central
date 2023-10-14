package org.tongji.programming.impl;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.tongji.programming.service.GroupUtilService;

@Component
public class GroupUtilServiceStartupRunner implements CommandLineRunner {

    @DubboReference
    GroupUtilService groupUtilService;

    @Override
    public void run(String... args) {
        System.err.println("StartUp!");
        groupUtilService.reminderHandler();
    }
}
