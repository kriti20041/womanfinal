package com.ruralwomen.platform.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "requests")
public class Request {

    @Id
    private String id;
    private String customerId;
    private String entrepreneurId;
    private String message;
    private String status = "pending";  // pending / accepted / rejected
    private LocalDateTime createdAt = LocalDateTime.now();

    // ✅ Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getEntrepreneurId() { return entrepreneurId; }
    public void setEntrepreneurId(String entrepreneurId) { this.entrepreneurId = entrepreneurId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
