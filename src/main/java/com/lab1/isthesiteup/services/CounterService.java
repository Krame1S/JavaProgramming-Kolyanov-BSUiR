package com.lab1.isthesiteup.services;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CounterService {

    private int counter = 0;
    private static final Logger logger = LoggerFactory.getLogger(CounterService.class);


    public synchronized void increment() {
        logger.info("Incrementing counter");
        counter++;
    }

    public synchronized int getCount() {
        logger.info("Getting counter value");
        return counter;
    }

    public synchronized int resetCount() {
        logger.info("Resetting counter");
        counter = 0;
        return counter;
    }
}
