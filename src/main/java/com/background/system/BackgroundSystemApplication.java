package com.background.system;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.background.system.mapper")
@EnableEncryptableProperties
@EnableScheduling
public class BackgroundSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackgroundSystemApplication.class, args);
    }

}
