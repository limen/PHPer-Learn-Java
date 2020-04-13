package com.limengxiang.basics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * SpringBootApplication注解告知框架这是启动类
 * ComponentScan注解告知框架需要扫描的组件位置
 * 这里告知框架扫描controller包下面的所有组件，确保HelloController被扫描
 */

@SpringBootApplication
@ComponentScan("com.limengxiang.basics.controller")
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class);
    }
}
