package com.lab1.isthesiteup.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@EnableCaching
public class CacheConfig {

    private final Map<String, Object> cache = new ConcurrentHashMap<>();

    public void put(String key, Object value, long expirationTimeMillis) {
        cache.put(key, value);

        // Установка задержки удаления кэша
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(() -> cache.remove(key), expirationTimeMillis, TimeUnit.MILLISECONDS);
        executorService.shutdown();
    }

    public Object get(String key) {
        return cache.get(key);
    }

    public void remove(String key) {
        cache.remove(key);
    }
}