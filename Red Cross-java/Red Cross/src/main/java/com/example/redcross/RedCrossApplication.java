package com.example.redcross;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class RedCrossApplication {

    public static void main(String[] args) {
        // 创建日志目录
        File logDir = new File("d:/DeskTop/Red Cross/logs");
        if (!logDir.exists()) {
            boolean created = logDir.mkdirs();
            if (created) {
                System.out.println("日志目录创建成功: " + logDir.getAbsolutePath());
            } else {
                System.out.println("日志目录创建失败: " + logDir.getAbsolutePath());
            }
        }
        
        SpringApplication.run(RedCrossApplication.class, args);
    }
}
