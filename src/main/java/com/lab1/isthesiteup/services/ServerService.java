package com.lab1.isthesiteup.services;

import org.springframework.stereotype.Service;
import com.lab1.isthesiteup.entities.Check;
import com.lab1.isthesiteup.entities.Server;
import com.lab1.isthesiteup.repositories.CheckRepository;
import com.lab1.isthesiteup.repositories.ServerRepository;
import java.util.List;
import java.util.Optional;


@Service
public class ServerService {

    private final CheckRepository checkRepository;
    private final ServerRepository serverRepository;

    public ServerService(ServerRepository serverRepository, CheckRepository checkRepository) {
        this.serverRepository = serverRepository;
        this.checkRepository = checkRepository;
    }

    public List<Server> findServersByCheckStatus(String status) {
        List<Server> servers = serverRepository.findServersByCheckStatus(status);
        return servers;
    }

    public List<Server> getAllServers() {
        return serverRepository.findAll();
    }
    
    
    public Server addServer(Server server) {
        Optional<Server> existingServer = serverRepository.findByUrl(server.getUrl());
        if (existingServer.isPresent()) {
            throw new IllegalArgumentException("A server with this URL already exists.");
        }
        return serverRepository.save(server);
    }    

    public Server updateServerUrl(Long id, String newUrl) {
        Server server = serverRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Server not found with id: " + id));
        List<Check> checksToRemove = checkRepository.findByServerUrl(server.getUrl());
        checkRepository.deleteAll(checksToRemove);
        // if the server url already exists do not update it
        if (serverRepository.findByUrl(newUrl).isPresent()) {
            throw new IllegalArgumentException("A server with this URL already exists.");
        }
        server.setUrl(newUrl);
        return serverRepository.save(server);
    }
    

    public void deleteServer(Long id) {
        serverRepository.deleteById(id);
    
    }
}