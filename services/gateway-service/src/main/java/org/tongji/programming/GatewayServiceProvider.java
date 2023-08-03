package org.tongji.programming;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class GatewayServiceProvider {
    public static void main(String[] args) {

        SpringApplication.run(GatewayServiceProvider.class, args);
    }
}