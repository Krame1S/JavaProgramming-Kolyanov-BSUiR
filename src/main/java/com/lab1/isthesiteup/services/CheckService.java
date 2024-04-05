package com.lab1.isthesiteup.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final CacheConfig cacheConfig;

    public CheckService(ServerRepository serverRepository, CheckRepository checkRepository, CacheConfig cacheConfig) {
        this.serverRepository = serverRepository;
        this.checkRepository = checkRepository;
        this.cacheConfig = cacheConfig;
    }

    private final String STATUS_UP = "Site is up";
    private final String STATUS_DOWN = "Site is down";
    private final String INCORRECT_URL = "Incorrect URL";

    public List<Check> getAllChecks() {
        return checkRepository.findAll();
    }

    public Check getServerStatus(String url) {
        Check cachedCheck = (Check) cacheConfig.get(url);
        if (cachedCheck != null) {
            Check cachedCheckCopy = createCheckCopy(cachedCheck);
            return cachedCheckCopy;
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
            check.setStatus(STATUS_UP);

            saveCheck(check);
            cacheConfig.put(url, check, 10000);
        
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                check.setStatus(STATUS_DOWN);
            } else {
                check.setStatus(INCORRECT_URL);
            }
        } catch (RestClientException e) {
            check.setStatus(INCORRECT_URL);
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
    
            // Fetch or create the server associated with the updated URL
            Server server = serverRepository.findByUrl(updatedCheck.getUrl())
                    .orElseGet(() -> {
                        Server newServer = new Server();
                        newServer.setUrl(updatedCheck.getUrl());
                        return serverRepository.save(newServer);
                    });
    
            // Associate the updated check with the server
            updatedCheck.setServer(server);
    
            // Check the server status of the updated URL
            String status = getServerStatus(updatedCheck.getUrl()).getStatus();
            updatedCheck.setStatus(status);
    
            // Save the updated check entity
            saveCheck(updatedCheck);
        }
    }

    public void deleteCheck(Long id) {
        checkRepository.deleteById(id);
    }
}
