package org.tongji.programming;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.tongji.programming.mapper")
@EnableDubbo
public class HelloWorldServiceProvider {
    public static void main(String[] args) {
        SpringApplication.run(HelloWorldServiceProvider.class, args);
    }
}
