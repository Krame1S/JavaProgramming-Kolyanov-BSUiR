package com.lab1.isthesiteup.controllers;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.lab1.isthesiteup.entities.UrlCheckEntity;
import com.lab1.isthesiteup.services.UrlCheckService;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@SessionAttributes("userId") 
public class UrlCheckController {

    private final UrlCheckService urlCheckService;

    public UrlCheckController(UrlCheckService urlCheckService) {
        this.urlCheckService = urlCheckService;
    }

    @GetMapping("/main-page/{userId}")
    public String showCheckForm(@PathVariable Long userId, Model model) {
        model.addAttribute("userId", userId); 
        return "check";
    }

    @PostMapping("/check/{userId}")
    public String checkUrl(@PathVariable Long userId, @RequestParam String url, Model model) {
    UrlCheckEntity result = urlCheckService.checkUrl(url, userId);
        model.addAttribute("message", result.getStatus());
        model.addAttribute("url", result.getUrl());
        model.addAttribute("time", result.getTime());
        return "check";
    }
   

    private String showUrlChecksForPeriod(@PathVariable Long userId,String period, Model model) {
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
        model.addAttribute("userId", userId);
           
        return "all-checks";
    }

    @GetMapping("/{userId}/{period}")
    public String showUrlChecks(@PathVariable Long userId, @PathVariable String period, Model model) { 
        return showUrlChecksForPeriod(userId, period, model);
    }


    @DeleteMapping("/all-checks/{id}/{period}")
    public String deleteUrlCheck(@PathVariable Long id, @PathVariable String period) {
        urlCheckService.deleteUrlCheck(id);
        return "redirect:/" + period;
    }
    
}
