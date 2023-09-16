package org.tongji.programming;

import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;

@SpringBootTest(classes = { BackstageServiceProvider.class })
@Configuration
public class TestPassword {

    @Autowired
    private StringEncryptor encryptor;

    @Test
    public void tester() {
        System.err.println("password --> " + encryptor.encrypt("把密码放在这里，运行测试就能看见加密的密文"));
    }
}
