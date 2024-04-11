package com.lab1.isthesiteup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class IsTheSiteUpApplication {
    public static void main(String[] args) {
        SpringApplication.run(IsTheSiteUpApplication.class, args);
    }

}
