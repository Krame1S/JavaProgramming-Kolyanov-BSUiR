package com.lab1.isthesiteup.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    private final String STATUS_UP = "Site is up";
    private final String STATUS_DOWN = "Site is down";
    private final String INCORRECT_URL = "Incorrect URL";

    public CheckService(ServerRepository serverRepository, CheckRepository checkRepository) {
        this.serverRepository = serverRepository;
        this.checkRepository = checkRepository;
    }

    public List<CheckEntity> getAllChecks() {
        return checkRepository.findAll();
    }

    public CheckEntity checkServerStatus(String url) {
        ServerEntity serverEntity = serverRepository.findByUrl(url)
                .orElseGet(() -> {
                    ServerEntity newServer = new ServerEntity();
                    newServer.setUrl(url);
                    return serverRepository.save(newServer);
                });
    
        RestTemplate restTemplate = new RestTemplate();
        CheckEntity checkEntity = new CheckEntity();
        checkEntity.setUrl(url);
        checkEntity.setServer(serverEntity);
    
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                checkEntity.setStatus(STATUS_UP);
            } else {
                checkEntity.setStatus(STATUS_DOWN);
            }
        } catch (Exception e) {
            checkEntity.setStatus(INCORRECT_URL);
        }
    
        checkRepository.save(checkEntity);
        return checkEntity;
    }

    public void updateCheck(Long id, CheckEntity checkEntity) {
    Optional<CheckEntity> existingCheck = checkRepository.findById(id);
    if (existingCheck.isPresent()) {
        CheckEntity updatedCheck = existingCheck.get();
        updatedCheck.setUrl(checkEntity.getUrl());
        updatedCheck.setStatus(checkEntity.getStatus());
        checkRepository.save(updatedCheck);
    }
}


    public void deleteCheck(Long id) {
        checkRepository.deleteById(id);
    }
}
