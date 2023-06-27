package com.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example", "com.example.controllers"})
@MapperScan("com.example.mapper")
public class FileBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(FileBackendApplication.class, args);
    }
}
