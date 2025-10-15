package com.ruralwomen.platform.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "women")
public class Women {

    @Id
    private String id;
    private String name;
    private String village;
    private List<String> customRequests; // List to store customer prompts

    public Women() {
    }

    public Women(String id, String name, String village, List<String> customRequests) {
        this.id = id;
        this.name = name;
        this.village = village;
        this.customRequests = customRequests;
    }

    // --- Getters and Setters ---
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public List<String> getCustomRequests() {
        return customRequests;
    }

    public void setCustomRequests(List<String> customRequests) {
        this.customRequests = customRequests;
    }

    // Convenience method to add a new custom request
    public void addCustomRequest(String request) {
        if (this.customRequests == null) {
            this.customRequests = new java.util.ArrayList<>();
        }
        this.customRequests.add(request);
    }
}
