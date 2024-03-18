package com.lab1.isthesiteup.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.lab1.isthesiteup.entities.ServerEntity;
import com.lab1.isthesiteup.services.CheckService;
import com.lab1.isthesiteup.services.ServerService;

@Controller
public class ServerController {

    private final ServerService serverService;
    private final CheckService checkService;


    public ServerController(ServerService serverService, CheckService checkService) {
        this.serverService = serverService;
        this.checkService = checkService;
    }

    @GetMapping("/")
    public String server(Model model) {
        model.addAttribute("servers", serverService.getAllServers());
        model.addAttribute("checks", checkService.getAllChecks());
        return "server";
    }

    @PostMapping("/server")
    public String addServer(@ModelAttribute ServerEntity server, Model model, RedirectAttributes redirectAttributes) {
        try {
            serverService.addServer(server);
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            // Add error message to the model and redirect back to the form
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/";
        }
    }
    
    
    @PutMapping("/server/{id}")
    public String updateServer(@PathVariable Long id, @ModelAttribute ServerEntity server, Model model) {
        try {
            serverService.updateServer(id, server);
            model.addAttribute("servers", serverService.getAllServers());
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to update server: " + e.getMessage());
            return "redirect:/";
        }
    }


    @DeleteMapping("/server/{id}")
    public String deleteServer(@PathVariable Long id, Model model) {
        serverService.deleteServer(id);
        return "redirect:/";
    }
}
