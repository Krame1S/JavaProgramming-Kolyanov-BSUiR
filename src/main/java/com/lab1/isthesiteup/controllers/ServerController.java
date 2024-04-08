package com.lab1.isthesiteup.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.lab1.isthesiteup.entities.Server;
import com.lab1.isthesiteup.services.CheckService;
import com.lab1.isthesiteup.services.ServerService;

@Controller
public class ServerController {

    private final ServerService serverService;
    private final CheckService checkService;

    private static final Logger logger = LoggerFactory.getLogger(ServerController.class);

    public ServerController(ServerService serverService, CheckService checkService) {
        this.serverService = serverService;
        this.checkService = checkService;
    }

    @GetMapping("/servers/status")
    @Operation(summary = "Get servers by check status")
    public String getServersByCheckStatus(@RequestParam @Parameter(description = "Check status") String status, Model model) {
        logger.info("Getting servers by check status: {}", status);
        List<Server> servers = serverService.findServersByCheckStatus(status);
        model.addAttribute("servers", servers);
        return "servers";
    }

    @GetMapping("/")
    @Operation(summary = "Get all servers and checks")
    public String server(Model model) {
        logger.info("Getting all servers and checks");
        model.addAttribute("servers", serverService.getAllServers());
        model.addAttribute("checks", checkService.getAllChecks());
        return "server";
    }

    @PostMapping("/server")
    @Operation(summary = "Add a new server")
    public String addServer(@ModelAttribute Server server, Model model, RedirectAttributes redirectAttributes) {
        try {
            serverService.addServer(server);
            logger.info("Server added successfully: {}", server);
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            logger.error("Error adding server: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/";
        }
    }

    @PutMapping("/server/{id}")
    public String updateServer(@PathVariable Long id, @RequestParam @Parameter(description = "New server URL") String url, Model model) {
        if (url == null || url.isEmpty()) {
            String errorMessage = "URL cannot be empty";
            logger.error("Error updating server: {}", errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        try {
            serverService.updateServerUrl(id, url);
            logger.info("Server updated successfully. ID: {}, URL: {}", id, url);
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            logger.error("Error updating server: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "redirect:/";
        }
    }

    @DeleteMapping("/server/{id}")
    @Operation(summary = "Delete a server by ID")
    public String deleteServer(@PathVariable Long id, Model model) {
        logger.info("Deleting server. ID: {}", id);
        serverService.deleteServer(id);
        logger.info("Server deleted successfully. ID: {}", id);
        return "redirect:/";
    }
}
