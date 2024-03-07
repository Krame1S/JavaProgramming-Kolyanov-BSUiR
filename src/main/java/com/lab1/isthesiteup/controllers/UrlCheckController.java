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
        model.addAttribute("time", result.getTime());
        return "check";
    }    

    private String showUrlChecksForPeriod(String period, Model model) {
        List<UrlCheckEntity> urlChecks;
        switch (period) {
            case "today":
                urlChecks = urlCheckService.getUrlChecksForToday();
                break;
            case "yesterday":
                urlChecks = urlCheckService.getUrlChecksForYesterday();
                break;
            case "this-week":
                urlChecks = urlCheckService.getUrlChecksForThisWeek();
                break;
            default:
                throw new IllegalArgumentException("Invalid period: " + period);
        }
        model.addAttribute("allUrlChecks", urlChecks);
        model.addAttribute("period", period);   
        return "all-checks";
    }

    @GetMapping("/all-checks/{period}")
    public String showUrlChecks(@PathVariable String period, Model model) {
        return showUrlChecksForPeriod(period, model);
    }


    @DeleteMapping("/all-checks/{id}")
    public String deleteUrlCheck(@PathVariable Long id) {
        urlCheckService.deleteUrlCheck(id);
        return "redirect:/all-checks";
    }

    //@PutMapping("/all-checks/{id}")
    
}
