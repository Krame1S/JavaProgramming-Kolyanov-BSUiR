package com.lab1.isthesiteup.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
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

    public CheckService(ServerRepository serverRepository, CheckRepository checkRepository) {
        this.serverRepository = serverRepository;
        this.checkRepository = checkRepository;
    }

    private final String STATUS_UP = "Site is up";
    private final String STATUS_DOWN = "Site is down";
    private final String INCORRECT_URL = "Incorrect URL";

    public List<CheckEntity> getAllChecks() {
        return checkRepository.findAll();
    }

    public CheckEntity getServerStatus(String url) {
        RestTemplate restTemplate = new RestTemplate();
        ServerEntity serverEntity = serverRepository.findByUrl(url)
                .orElseGet(() -> {
                    ServerEntity newServer = new ServerEntity();
                    newServer.setUrl(url);
                    return serverRepository.save(newServer);
                });
    
        CheckEntity checkEntity = new CheckEntity();
        checkEntity.setUrl(url);
        checkEntity.setServer(serverEntity); // Associate the CheckEntity with the ServerEntity
    
        try {
            restTemplate.getForEntity(url, String.class);
            checkEntity.setStatus(STATUS_UP); // Assuming 200 OK is the success status
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                checkEntity.setStatus(STATUS_DOWN); // 404 Not Found
            } else {
                checkEntity.setStatus(INCORRECT_URL); // Other client errors
            }
        } catch (RestClientException e) {
            // Handle other exceptions, such as when the URL is incorrect or the server is down
            checkEntity.setStatus(INCORRECT_URL);
        }
    
        return checkEntity; // Return the CheckEntity with the status and associated ServerEntity
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
