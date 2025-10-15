package com.ruralwomen.platform.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "customer_requests")
public class CustomerRequest {

    @Id
    private String id;

    private String customerName;
    private String customerEmail;
    private String prompt; // what customer wants
    private String artisanId; // selected woman
    private String status = "Pending"; // Pending / In Progress / Completed

    public CustomerRequest() {}

    public CustomerRequest(String customerName, String customerEmail, String prompt, String artisanId) {
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.prompt = prompt;
        this.artisanId = artisanId;
    }

    public Object getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getArtisanId() {
        return artisanId;
    }

    public void setArtisanId(String artisanId) {
        this.artisanId = artisanId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // getters and setters
}
