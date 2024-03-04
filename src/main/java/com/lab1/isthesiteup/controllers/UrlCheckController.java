package com.lab1.isthesiteup.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.lab1.isthesiteup.services.UrlCheckService;
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
        String result = urlCheckService.checkUrl(url);
        model.addAttribute("urlStatusMessage", result);
        return "check";
    }
}
