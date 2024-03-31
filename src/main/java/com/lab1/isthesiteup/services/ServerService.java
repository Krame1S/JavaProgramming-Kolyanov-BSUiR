package com.lab1.isthesiteup.services;

import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import com.lab1.isthesiteup.config.CacheConfig;
import com.lab1.isthesiteup.entities.CheckEntity;
import com.lab1.isthesiteup.entities.ServerEntity;
import com.lab1.isthesiteup.repositories.CheckRepository;
import com.lab1.isthesiteup.repositories.ServerRepository;

import jakarta.validation.constraints.Null;

import java.util.List;



@Service
public class ServerService {

    private final CheckRepository checkRepository;
    private final ServerRepository serverRepository;
    private final CacheConfig cacheConfig;

    public ServerService(ServerRepository serverRepository, CheckRepository checkRepository, CacheConfig cacheConfig) {
        this.serverRepository = serverRepository;
        this.checkRepository = checkRepository;
        this.cacheConfig = cacheConfig;
    }

    public List<ServerEntity> findServersByCheckStatus(String status) {
        try {
            @SuppressWarnings("unchecked")
            List<ServerEntity> cachedServers = (List<ServerEntity>) cacheConfig.get(status);
            if (cachedServers != null) {
                return cachedServers;
            }
            List<ServerEntity> servers = serverRepository.findServersByCheckStatus(status);
            cacheConfig.put(status, servers, 20000);
            return servers;
        } catch (Exception e) {
            throw new ServiceException("Failed to find servers by check status", e);
        }
    }

    public List<ServerEntity> getAllServers() {
        try {
            return serverRepository.findAll();
        } catch (Exception e) {
            throw new ServiceException("Failed to get all servers", e);
        }
    }

    public ServerEntity addServer(ServerEntity server) {
        try {
            ServerEntity existingServer = serverRepository.findByUrl(server.getUrl());
            if (existingServer != null) {
                throw new IllegalArgumentException("A server with this URL already exists.");
            }
            return serverRepository.save(server);
        } catch (Exception e) {
            throw new ServiceException("Failed to add server", e);
        }
    }

    public ServerEntity updateServerUrl(Long id, String newUrl) {
        try {
            ServerEntity server = serverRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Server not found with id: " + id));
    
            List<CheckEntity> checksToRemove = checkRepository.findByServerEntityUrl(server.getUrl());
            checkRepository.deleteAll(checksToRemove);
    
            ServerEntity existingServer = serverRepository.findByUrl(newUrl);
            if (existingServer != null && !existingServer.getId().equals(id)) {
                throw new IllegalArgumentException("URL already exists");
            }
    
            server.setUrl(newUrl);
            return serverRepository.save(server);
        } catch (Exception e) {
            throw new ServiceException("Failed to update server URL", e);
        }
    }

    public void deleteServer(Long id) {
        try {
            serverRepository.deleteById(id);
        } catch (Exception e) {
            throw new ServiceException("Failed to delete server", e);
        }
    }
}