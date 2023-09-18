package org.tongji.programming;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.tongji.programming.mapper")
@EnableDubbo
public class CheckCardServiceProvider {
    public static void main(String[] args) {
        SpringApplication.run(CheckCardServiceProvider.class, args);
    }
}
