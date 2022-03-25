package com.top.okya;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableAsync
public class OkyaApplication {

    public static void main(String[] args) {
        SpringApplication.run(OkyaApplication.class, args);
    }

}
