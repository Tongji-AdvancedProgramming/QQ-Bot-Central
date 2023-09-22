package org.tongji.programming;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = { BackstageServiceProvider.class })
public class TestConfigProvider {
    @Autowired
    ConfigProvider configProvider;

    @Test
    public void testConfigHelloWorld(){
        configProvider.helloWorld();
    }
}
