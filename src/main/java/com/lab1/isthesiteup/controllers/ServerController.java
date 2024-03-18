package com.lab1.isthesiteup.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lab1.isthesiteup.entities.CheckEntity;
import com.lab1.isthesiteup.entities.ServerEntity;
import com.lab1.isthesiteup.services.ServerService;

@Controller
public class ServerController {

    private final ServerService serverService;

    public ServerController(ServerService serverService) {
        this.serverService = serverService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("servers", serverService.getAllServers());
        return "index";
    }

    @PostMapping("/check")
    public String checkServerStatus(@RequestParam String url, Model model) {
        CheckEntity check = serverService.checkServerStatus(url);
        model.addAttribute("url", check.getUrl());
        model.addAttribute("status", check.getStatus()); 
        return "checkurl";
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
public String updateServer(@PathVariable Long id, @ModelAttribute ServerEntity serverEntity, Model model) {
    serverService.updateServer(id, serverEntity);
    model.addAttribute("servers", serverService.getAllServers());
    return "redirect:/";
}

    @DeleteMapping("/server/{id}")
    public String deleteServer(@PathVariable Long id, Model model) {
        serverService.deleteServer(id);
        return "redirect:/";
    }
}
