package com.ruralwomen.platform.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "women")
public class Women {

    @Id
    private String id;
    private String name;
    private String village;
    private String district;
    private List<String> skills = new ArrayList<>();
    private List<CustomerRequest> customRequests = new ArrayList<>();

    public Women() {}

    public Women(String name, String village, String district, List<String> skills) {
        this.name = name;
        this.village = village;
        this.district = district;
        this.skills = skills;
    }

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getVillage() { return village; }
    public void setVillage(String village) { this.village = village; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public List<String> getSkills() { return skills; }
    public void setSkills(List<String> skills) { this.skills = skills; }

    public List<CustomerRequest> getCustomRequests() { return customRequests; }
    public void setCustomRequests(List<CustomerRequest> customRequests) { this.customRequests = customRequests; }
}
