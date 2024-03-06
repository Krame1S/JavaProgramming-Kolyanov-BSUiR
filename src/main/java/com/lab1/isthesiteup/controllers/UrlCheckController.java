package com.lab1.isthesiteup.controllers;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.lab1.isthesiteup.entities.UrlCheckEntity;
import com.lab1.isthesiteup.services.UrlCheckService;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UrlCheckController {

    private final UrlCheckService urlCheckService;

    public UrlCheckController(UrlCheckService urlCheckService) {
        this.urlCheckService = urlCheckService;
    }

    @GetMapping("/")
    public String showCheckForm(Model model) {
        return "check";
    }

    @PostMapping("/check")
    public String checkUrl(@RequestParam String url, Model model) {
        UrlCheckEntity result = urlCheckService.checkUrl(url);
        model.addAttribute("message", result.getStatus());
        model.addAttribute("url", result.getUrl());
        return "check";
    }

    @GetMapping("/all-checks")
    public String showAllUrlChecks(Model model) {
        List<UrlCheckEntity> allUrlChecks = urlCheckService.getAllUrlChecks();
        model.addAttribute("allUrlChecks", allUrlChecks);
        return "all-checks";
    }

    @DeleteMapping("/all-checks/{id}")
    public String deleteUrlCheck(@PathVariable Long id) {
        urlCheckService.deleteUrlCheck(id);
        return "redirect:/all-checks";
    }



}
