package com.krainet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableFeignClients
public class NotificationApplication  {
    public static void main(String[] args) {
        SpringApplication.run(NotificationApplication.class, args);
    }
}