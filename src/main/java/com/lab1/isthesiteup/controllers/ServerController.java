package com.lab1.isthesiteup.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.lab1.isthesiteup.entities.ServerEntity;
import com.lab1.isthesiteup.services.CheckService;
import com.lab1.isthesiteup.services.ServerService;

@Controller
public class ServerController {

    private final Logger logger = LoggerFactory.getLogger(ServerController.class);

    private final ServerService serverService;
    private final CheckService checkService;

    public ServerController(ServerService serverService, CheckService checkService) {
        this.serverService = serverService;
        this.checkService = checkService;
    }

    @GetMapping("/")
    public String server(Model model) {
        try {
            logger.info("Home page accessed");
            model.addAttribute("servers", serverService.getAllServers());
            model.addAttribute("checks", checkService.getAllChecks());
            return "server";
        } catch (Exception e) {
            handleException(e, model);
            return "error";
        }
    }

    @GetMapping("/servers/status")
    public String getServersByCheckStatus(@RequestParam String status, Model model) {
        try {
            logger.info("Getting servers by check status: {}", status);
            List<ServerEntity> servers = serverService.findServersByCheckStatus(status);
            model.addAttribute("servers", servers);
            return "servers";
        } catch (Exception e) {
            logger.error("Failed to get servers by check status", e);
            handleException(e, model);
            return "error";
        }
    }

    @PostMapping("/server")
    public String addServer(@ModelAttribute ServerEntity server, Model model, RedirectAttributes redirectAttributes) {
        try {
            logger.info("Adding server: {}", server.getUrl());
            serverService.addServer(server);
            return "redirect:/";
        } catch (Exception e) {
            logger.error("Failed to add server", e);
            handleException(e, model);
            return "error";
        }
    }

    @PutMapping("/server/{id}")
    public String updateServer(@PathVariable Long id, @RequestParam String url, Model model) {
        try {
            logger.info("Updating server URL. Server ID: {}, New URL: {}", id, url);
            serverService.updateServerUrl(id, url);
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            logger.error("Failed to update server URL", e);
            model.addAttribute("error", e.getMessage());
            return "redirect:/";
        } catch (Exception e) {
            logger.error("Failed to update server URL", e);
            handleException(e, model);
            return "error";
        }
    }

    @DeleteMapping("/server/{id}")
    public String deleteServer(@PathVariable Long id, Model model) {
        try {
            logger.info("Deleting server. Server ID: {}", id);
            serverService.deleteServer(id);
            return "redirect:/";
        } catch (Exception e) {
            logger.error("Failed to delete server", e);
            handleException(e, model);
            return "error";
        }
    }

    private void handleException(Exception e, Model model) {
        model.addAttribute("error", e.getMessage());
    }
}
