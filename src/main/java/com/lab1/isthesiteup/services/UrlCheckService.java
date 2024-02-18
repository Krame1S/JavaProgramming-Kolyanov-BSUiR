package com.lab1.isthesiteup.services;

import java.io.IOException;
import org.springframework.stereotype.Service;

@Service
public class UrlCheckService {

    private final UrlChecker urlChecker;

    public UrlCheckService(UrlChecker urlChecker) {
        this.urlChecker = urlChecker;
    }

    public String checkUrl(String url) {
        try {
            return urlChecker.check(url);
        } catch (IOException e) {
            return "Site is down!";
        }
    }
}