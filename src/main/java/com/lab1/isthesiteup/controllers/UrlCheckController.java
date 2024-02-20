package com.lab1.isthesiteup.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lab1.isthesiteup.services.UrlCheckService;

@RestController                                                     //This annotation is used to mark a class as a request handling component 
public class UrlCheckController {

    private final UrlCheckService urlCheckService;

    public UrlCheckController(UrlCheckService urlCheckService) {
        this.urlCheckService = urlCheckService;
    }

    @GetMapping("/check")
    public String getUrlStatusMessage(@RequestParam String url) {
        return urlCheckService.checkUrl(url);
    }
}
