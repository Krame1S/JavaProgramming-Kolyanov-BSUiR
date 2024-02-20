package com.lab1.isthesiteup.services;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.springframework.stereotype.Service;

@Service
public class UrlCheckService {

    private static final String SITE_IS_UP = "Site is up!";
    private static final String SITE_IS_DOWN = "Site is down!";
    private static final String INCORRECT_URL = "URL is incorrect!";

    public String checkUrl(String url) {
        String returnMessage = "";
        try {
            URL urlObj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responseCode = conn.getResponseCode();
            if (responseCode ==  200) {
                returnMessage = SITE_IS_UP;
            } else {
                returnMessage = SITE_IS_DOWN;
            }
        } catch (MalformedURLException e) {
            returnMessage = INCORRECT_URL;
        } catch (IOException e) {
            returnMessage = SITE_IS_DOWN;
        }
        return returnMessage;
    }
}