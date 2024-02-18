package com.lab1.isthesiteup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lab1.isthesiteup.services.UrlCheckService;

@RestController
public class UrlCheckController {

    private final UrlCheckService urlCheckService;

    @Autowired
    public UrlCheckController(UrlCheckService urlCheckService) {
        this.urlCheckService = urlCheckService;
    }

    @GetMapping("/check")
    public String getUrlStatusMessage(@RequestParam String url) {
        return urlCheckService.checkUrl(url);
    }
}
