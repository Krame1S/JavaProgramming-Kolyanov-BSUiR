package com.lab1.isthesiteup.services;

import org.springframework.stereotype.Service;
import com.lab1.isthesiteup.entities.ServerEntity;
import com.lab1.isthesiteup.repositories.ServerRepository;
import java.util.List;
import java.util.Optional;


@Service
public class ServerService {

    private final ServerRepository serverRepository;    

    public ServerService(ServerRepository serverRepository) {
        this.serverRepository = serverRepository;
    }

    public List<ServerEntity> getAllServers() {
        return serverRepository.findAll();
    }
    
    
    public ServerEntity addServer(ServerEntity server) {
        Optional<ServerEntity> existingServer = serverRepository.findByUrl(server.getUrl());
        if (existingServer.isPresent()) {
            throw new IllegalArgumentException("A server with this URL already exists.");
        }
        return serverRepository.save(server);
    }
    

    public void updateServer(Long id, ServerEntity server) {
        ServerEntity existingServer = serverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Server not found with id " + id));
        existingServer.setUrl(server.getUrl());
        serverRepository.save(existingServer);
    }
    

    public void deleteServer(Long id) {
        serverRepository.deleteById(id);
    
    }
}