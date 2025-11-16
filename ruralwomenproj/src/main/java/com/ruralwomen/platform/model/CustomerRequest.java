package com.ruralwomen.platform.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection = "customer_requests")
public class CustomerRequest {

    @Id
    private String id;
    private String customerName;
    private String requestedProduct;
    private String assignedWoman;   // woman's name
    private String status;          // Pending, Accepted, Completed
    private Double price;

    public CustomerRequest() {}

    public CustomerRequest(String customerName, String requestedProduct, String assignedWoman, String status, Double price) {
        this.customerName = customerName;
        this.requestedProduct = requestedProduct;
        this.assignedWoman = assignedWoman;
        this.status = status;
        this.price = price;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getRequestedProduct() { return requestedProduct; }
    public void setRequestedProduct(String requestedProduct) { this.requestedProduct = requestedProduct; }

    public String getAssignedWoman() { return assignedWoman; }
    public void setAssignedWoman(String assignedWoman) { this.assignedWoman = assignedWoman; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}
