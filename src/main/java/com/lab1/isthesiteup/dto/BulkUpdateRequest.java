package com.lab1.isthesiteup.dto;

import java.util.List;

public class BulkUpdateRequest {
    private List<Long> serverIds;
    private String newStatus;

    // Геттер для serverIds
    public List<Long> getServerIds() {
        return serverIds;
    }

    // Сеттер для serverIds
    public void setServerIds(List<Long> serverIds) {
        this.serverIds = serverIds;
    }

    // Геттер для newStatus
    public String getNewStatus() {
        return newStatus;
    }

    // Сеттер для newStatus
    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }
}
