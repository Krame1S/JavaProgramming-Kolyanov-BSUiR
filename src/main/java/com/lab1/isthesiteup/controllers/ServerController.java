package com.lab1.isthesiteup.controllers;

import com.lab1.isthesiteup.entities.Server;
import com.lab1.isthesiteup.services.ServerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ServerController {

    private final ServerService serverService;
    private static final Logger logger = LoggerFactory.getLogger(ServerController.class);

    public ServerController(ServerService serverService) {
        this.serverService = serverService;
    }

    @GetMapping("/servers/status")
    public ResponseEntity<List<Server>> getServersByCheckStatus(@RequestParam String status) {
        status = status.replaceAll("[\n\r]", "_");
        logger.info("Getting servers by check status: {}", status);
        List<Server> servers = serverService.findServersByCheckStatus(status);
        return ResponseEntity.ok(servers);
    }

    @GetMapping("/")
    public ResponseEntity<List<Server>> getAllServersAndChecks() {
        logger.info("Getting all servers and checks");
        List<Server> servers = serverService.getAllServers();
        return ResponseEntity.ok(servers);
    }

    @PostMapping("/server")
    public ResponseEntity<Void> addServer(@RequestBody Server server) {
        try {
            serverService.addServer(server);
            logger.info("Server added successfully: {}", server);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            logger.error("Error adding server: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/server/{id}")
    public ResponseEntity<Void> updateServer(@PathVariable Long id, @RequestParam String url) {
        if (url == null || url.isEmpty()) {
            String errorMessage = "URL cannot be empty";
            logger.error("Error updating server: {}", errorMessage);
            return ResponseEntity.badRequest().build();
        }

        try {
            serverService.updateServerUrl(id, url);
            url = url.replaceAll("[\n\r]", "_");
            logger.info("Server updated successfully. URL: {}", url);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            logger.error("Error updating server: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/server/{id}")
    public ResponseEntity<Void> deleteServer(@PathVariable Long id) {
        logger.info("Deleting server. ID: {}", id);
        serverService.deleteServer(id);
        logger.info("Server deleted successfully. ID: {}", id);
        return ResponseEntity.ok().build();
    }
}
