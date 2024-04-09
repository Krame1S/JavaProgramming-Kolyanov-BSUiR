package com.lab1.isthesiteup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.lab1.isthesiteup"})
@EntityScan("com.lab1.isthesiteup.entities")
@EnableCaching
public class IsTheSiteUpApplication {
    public static void main(String[] args) {
        SpringApplication.run(IsTheSiteUpApplication.class, args);
    }

}
