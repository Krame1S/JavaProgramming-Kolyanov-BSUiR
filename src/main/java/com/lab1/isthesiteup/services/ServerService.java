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
public class ServerService {

    private final ServerRepository serverRepository;
    private final CheckRepository checkRepository;

    private final String STATUS_UP = "Site is up";
    private final String STATUS_DOWN = "Site is down";
    private final String INCORRECT_URL = "Incorrect URL";

    public ServerService(ServerRepository serverRepository, CheckRepository checkRepository) {
        this.serverRepository = serverRepository;
        this.checkRepository = checkRepository;
    }

    public List<ServerEntity> getAllServers() {
        return serverRepository.findAll();
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
            // Assuming INCORRECT_URL is used for exceptions related to URL issues
            checkEntity.setStatus(INCORRECT_URL);
        }
    
        checkRepository.save(checkEntity);
        return checkEntity;
    }
    
    
    public ServerEntity addServer(ServerEntity server) {
        Optional<ServerEntity> existingServer = serverRepository.findByUrl(server.getUrl());
        if (existingServer.isPresent()) {
            throw new IllegalArgumentException("A server with this URL already exists.");
        }
        return serverRepository.save(server);
    }
    

    public void updateServer(Long id, ServerEntity serverEntity) {
        serverRepository.findById(id).ifPresent(existingServer -> {
            existingServer.setUrl(serverEntity.getUrl());
            serverRepository.save(existingServer);
        });
    }
    

    public void deleteServer(Long id) {
        serverRepository.deleteById(id);
    
