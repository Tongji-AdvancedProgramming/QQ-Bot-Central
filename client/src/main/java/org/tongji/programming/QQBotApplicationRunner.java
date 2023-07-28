package org.tongji.programming;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class QQBotApplicationRunner {
    public static void main(String[] args) {
        SpringApplication.run(QQBotApplicationRunner.class, args);
    }
}