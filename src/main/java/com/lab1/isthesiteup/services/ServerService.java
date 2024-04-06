package com.lab1.isthesiteup.services;

import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import com.lab1.isthesiteup.config.CacheConfig;
import com.lab1.isthesiteup.entities.Check;
import com.lab1.isthesiteup.entities.Server;
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

    public List<Server> findServersByCheckStatus(String status) {
        @SuppressWarnings("unchecked")
        List<Server> cachedServers = (List<Server>) cacheConfig.get(status);
        if (cachedServers != null) {
            return cachedServers;
        }
        List<Server> servers = serverRepository.findServersByCheckStatus(status);
        cacheConfig.put(status, servers, 20000);

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
    }


    public Server updateServerUrl(Long id, String newUrl) {
        Server server = serverRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Server not found with id: " + id));
        List<Check> checksToRemove = checkRepository.findByServerUrl(server.getUrl());
        checkRepository.deleteAll(checksToRemove);
        server.setUrl(newUrl);
        return serverRepository.save(server);
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