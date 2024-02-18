package com.lab1.isthesiteup.services;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.springframework.stereotype.Service;

@Service
public class UrlChecker {

    public String check(String url) throws IOException {
        try {
            URL urlObj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responseCode = conn.getResponseCode();
            if (responseCode ==   200) {
                return "Site is up!";
            } else {
                return "Site is down!";
            }
        } catch (MalformedURLException e) {
            return "URL is incorrect!";
        }
    }
}
