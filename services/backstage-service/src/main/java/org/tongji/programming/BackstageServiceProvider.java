package org.tongji.programming;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDubbo
@SpringBootApplication
public class BackstageServiceProvider {
    public static void main(String[] args) {
        SpringApplication.run(BackstageServiceProvider.class, args);
    }
}
