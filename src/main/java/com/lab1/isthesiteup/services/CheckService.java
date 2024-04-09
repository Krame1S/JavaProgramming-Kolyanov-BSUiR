package com.lab1.isthesiteup.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.lab1.isthesiteup.config.CacheConfig;
import com.lab1.isthesiteup.entities.Check;
import com.lab1.isthesiteup.entities.Server;
import com.lab1.isthesiteup.repositories.CheckRepository;
import com.lab1.isthesiteup.repositories.ServerRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CheckService {

    private final CheckRepository checkRepository;
    private final ServerRepository serverRepository;
    
    @Autowired
    private CacheConfig cacheConfig;

    public CheckService(ServerRepository serverRepository, CheckRepository checkRepository, CacheConfig cacheConfig) {
        this.serverRepository = serverRepository;
        this.checkRepository = checkRepository;
        this.cacheConfig = cacheConfig;
    }

    private static final String STATUSUP = "Site is up";
    private static final String STATUSDOWN = "Site is down";
    private static final String INCORRECTURL = "Incorrect URL";

    public List<Check> getAllChecks() {
        return checkRepository.findAll();
    }

    public Check getServerStatus(String url) {
        Check cachedCheck = (Check) cacheConfig.get(url);
        if (cachedCheck != null) {
            return createCheckCopy(cachedCheck);
        }

        RestTemplate restTemplate = new RestTemplate();
        Server server = serverRepository.findByUrl(url)
                .orElseGet(() -> {
                    Server newServer = new Server();
                    newServer.setUrl(url);
                    return serverRepository.save(newServer);
                });
    
        Check check = new Check();
        check.setUrl(url);
        check.setServer(server);
    
        try {
            restTemplate.getForEntity(url, String.class);
            check.setStatus(STATUSUP);

            saveCheck(check);
            cacheConfig.put(url, check, 10000);
        
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                check.setStatus(STATUSDOWN);
            } else {
                check.setStatus(INCORRECTURL);
            }
        } catch (RestClientException e) {
            check.setStatus(INCORRECTURL);
        }    
        return check;
    }

    private Check createCheckCopy(Check check) {
        Check copy = new Check();
        copy.setStatus(check.getStatus());
        copy.setUrl(check.getUrl());
        copy.setServer(check.getServer());
        return copy;
    }

    public Check saveCheck(Check check) {
        return checkRepository.save(check);
    }

    public void updateCheck(Long id, Check check) {
        Optional<Check> existingCheck = checkRepository.findById(id);
        if (existingCheck.isPresent()) {
            Check updatedCheck = existingCheck.get();
            updatedCheck.setUrl(check.getUrl());
    
            Server server = serverRepository.findByUrl(updatedCheck.getUrl())
                    .orElseGet(() -> {
                        Server newServer = new Server();
                        newServer.setUrl(updatedCheck.getUrl());
                        return serverRepository.save(newServer);
                    });
    
            updatedCheck.setServer(server);
    
            String status = getServerStatus(updatedCheck.getUrl()).getStatus();
            updatedCheck.setStatus(status);
    
            saveCheck(updatedCheck);

            cacheConfig.remove(check.getUrl());

        }
    }

    public void deleteCheck(Long id) {
        Optional<Check> checkOptional = checkRepository.findById(id);
        if (checkOptional.isPresent()) {
            Check check = checkOptional.get();

            checkRepository.deleteById(id);

            cacheConfig.remove(check.getUrl());
        }
    }

    public void bulkUpdateServerStatusThroughChecks(List<Long> serverIds, String newStatus) {
        serverIds.stream()
            .map(serverRepository::findById)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .forEach(server -> {
                // Найти все проверки для текущего сервера
                List<Check> checks = checkRepository.findByServerUrl(server.getUrl());
                // Обновить статус каждой проверки
                checks.forEach(check -> {
                    check.setStatus(newStatus);
                    // Сохранить обновленную проверку
                    checkRepository.save(check);
                });
            });
    }
}    
