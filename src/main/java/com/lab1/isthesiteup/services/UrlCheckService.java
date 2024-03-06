package com.lab1.isthesiteup.services;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.stereotype.Service;

import com.lab1.isthesiteup.entities.UrlCheckEntity;
import com.lab1.isthesiteup.repositories.UrlCheckRepository;

@Service
public class UrlCheckService {

    private final UrlCheckRepository urlCheckRepository;

    private final String STATUS_UP = "Site is up";
    private final String STATUS_DOWN = "Site is down";
    private final String INCORRECT_URL = "Incorrect URL";

    public UrlCheckService(UrlCheckRepository urlCheckRepository) {
        this.urlCheckRepository = urlCheckRepository;
    }

    public UrlCheckEntity checkUrl(String url) {
        UrlCheckEntity entity = new UrlCheckEntity();
        try {
            URL urlObj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responseCode = conn.getResponseCode();
            if (responseCode >= 200 && responseCode < 300) {
                entity.setStatus(STATUS_UP);                      
            } else {
                entity.setStatus(STATUS_DOWN);
            }
        } catch (MalformedURLException e) {
            entity.setStatus(INCORRECT_URL);
        } catch (IOException e) {
            entity.setStatus(INCORRECT_URL);  
        }

        entity.setUrl(url);                                 

        urlCheckRepository.save(entity); 
        return entity;
    }
}