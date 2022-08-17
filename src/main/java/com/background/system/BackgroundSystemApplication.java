package com.background.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.background.system.mapper")
public class BackgroundSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackgroundSystemApplication.class, args);
        System.out.println("～～～project start～～～");
    }

}
