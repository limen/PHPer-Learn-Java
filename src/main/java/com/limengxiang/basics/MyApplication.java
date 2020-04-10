package com.limengxiang.basics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.limengxiang.basics.*")
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class);
    }
}
