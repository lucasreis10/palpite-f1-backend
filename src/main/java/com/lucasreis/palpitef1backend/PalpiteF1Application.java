package com.lucasreis.palpitef1backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.lucasreis.palpitef1backend")
public class PalpiteF1Application {
    public static void main(String[] args) {
        SpringApplication.run(PalpiteF1Application.class, args);
    }
} 