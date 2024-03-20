package com.lab1.isthesiteup.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.lab1.isthesiteup.config.CacheConfig;
import com.lab1.isthesiteup.entities.CheckEntity;
import com.lab1.isthesiteup.entities.ServerEntity;
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

    public List<CheckEntity> getAllChecks() {
        return checkRepository.findAll();
    }

    public CheckEntity getServerStatus(String url) {
        CheckEntity cachedCheck = (CheckEntity) cacheConfig.get(url);
        if (cachedCheck != null) {
            CheckEntity cachedCheckCopy = createCheckEntityCopy(cachedCheck);
            return cachedCheckCopy;
        }

        RestTemplate restTemplate = new RestTemplate();
        ServerEntity serverEntity = serverRepository.findByUrl(url)
                .orElseGet(() -> {
                    ServerEntity newServer = new ServerEntity();
                    newServer.setUrl(url);
                    return serverRepository.save(newServer);
                });
    
        CheckEntity checkEntity = new CheckEntity();
        checkEntity.setUrl(url);
        checkEntity.setServer(serverEntity);
    
        try {
            restTemplate.getForEntity(url, String.class);
            checkEntity.setStatus(STATUS_UP);

            saveCheckEntity(checkEntity);
            cacheConfig.put(url, checkEntity, 10000);
        
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                checkEntity.setStatus(STATUS_DOWN);
            } else {
                checkEntity.setStatus(INCORRECT_URL);
            }
        } catch (RestClientException e) {
            checkEntity.setStatus(INCORRECT_URL);
        }
    
        return checkEntity;
    }

    private CheckEntity createCheckEntityCopy(CheckEntity checkEntity) {
        CheckEntity copy = new CheckEntity();
        copy.setStatus(checkEntity.getStatus());
        copy.setUrl(checkEntity.getUrl());
        copy.setServer(checkEntity.getServer());
        return copy;
    }

    public CheckEntity saveCheckEntity(CheckEntity checkEntity) {
        return checkRepository.save(checkEntity);
    }

    public void updateCheck(Long id, CheckEntity checkEntity) {
        Optional<CheckEntity> existingCheck = checkRepository.findById(id);
        if (existingCheck.isPresent()) {
            CheckEntity updatedCheck = existingCheck.get();
            updatedCheck.setUrl(checkEntity.getUrl());
    
            // Fetch or create the ServerEntity associated with the updated URL
            ServerEntity serverEntity = serverRepository.findByUrl(updatedCheck.getUrl())
                    .orElseGet(() -> {
                        ServerEntity newServer = new ServerEntity();
                        newServer.setUrl(updatedCheck.getUrl());
                        return serverRepository.save(newServer);
                    });
    
            // Associate the updated CheckEntity with the ServerEntity
            updatedCheck.setServer(serverEntity);
    
            // Check the server status of the updated URL
            String status = getServerStatus(updatedCheck.getUrl()).getStatus();
            updatedCheck.setStatus(status);
    
            // Save the updated check entity
            saveCheckEntity(updatedCheck);
        }
    }

    public void deleteCheck(Long id) {
        checkRepository.deleteById(id);
    }
}
