package org.tongji.programming.service;

import org.tongji.programming.DTO.cqhttp.MessageUniversalReport;

public interface DemoService {

    String sayHello(String name);

    String chiakiSayHello();

    String djImage();

    String sjImage(MessageUniversalReport event);

}
