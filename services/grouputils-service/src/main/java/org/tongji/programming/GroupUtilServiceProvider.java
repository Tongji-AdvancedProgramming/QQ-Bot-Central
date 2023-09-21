package org.tongji.programming;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class GroupUtilServiceProvider {
    public static void main(String[] args) {
        SpringApplication.run(GroupUtilServiceProvider.class, args);
    }
}
