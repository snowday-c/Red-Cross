package com.example.redcross;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
@MapperScan("com.example.redcross.mapper")

public class RedCrossApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedCrossApplication.class, args);
    }

}
