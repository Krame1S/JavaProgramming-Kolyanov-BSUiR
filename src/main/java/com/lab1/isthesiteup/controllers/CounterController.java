package com.lab1.isthesiteup.controllers;

import com.lab1.isthesiteup.services.CounterService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CounterController {

    private final CounterService counterService;
    private static final Logger logger = LoggerFactory.getLogger(CounterController.class);


    public CounterController(CounterService counterService) {
        this.counterService = counterService;
    }

    @GetMapping("/counter")
    public int getCounterValue() {
        logger.info("Getting counter value");
        return counterService.getCount();
    }

    @GetMapping("/reset-counter")
    public int resetCounterValue() {
        logger.info("Resetting counter value");
        return counterService.resetCount();
    }
}
