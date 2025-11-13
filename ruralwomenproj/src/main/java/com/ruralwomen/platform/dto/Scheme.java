package com.ruralwomen.platform.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Entity
@Document(collection = "schemes")
@CompoundIndexes({
        @CompoundIndex(name = "src_srcid_idx", def = "{'source': 1, 'sourceId': 1}", unique = true)
})
@Table(name = "schemes")
public class Scheme {

    @Id
    private String id;

    private Double incomeMin;
    private Double incomeMax;

    private String name;
    private String description;


    private String category;
    private String govtLevel;
    private String stateName; // optional

    private String link;

    // source metadata for dedupe
    private String source;   // e.g., "india.gov.in"
    private String sourceId; // hashed canonical URL or site id

    private Instant scrapedAt;
    private boolean verifiedByAdmin = false;

    // ---------------- Constructors ----------------
    public Scheme() {
    }

    public Scheme(String name, String description, Double incomeMin, Double incomeMax,
                  String category, String govtLevel, String stateName, String link,
                  String source, String sourceId, Instant scrapedAt, boolean verifiedByAdmin) {
        this.name = name;
        this.description = description;
        this.incomeMin = incomeMin;
        this.incomeMax = incomeMax;
        this.category = category;
        this.govtLevel = govtLevel;
        this.stateName = stateName;
        this.link = link;
        this.source = source;
        this.sourceId = sourceId;
        this.scrapedAt = scrapedAt;
        this.verifiedByAdmin = verifiedByAdmin;
    }

    // ---------------- Getters and Setters ----------------

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getIncomeMin() {
        return incomeMin;
    }

    public void setIncomeMin(Double incomeMin) {
        this.incomeMin = incomeMin;
    }

    public Double getIncomeMax() {
        return incomeMax;
    }

    public void setIncomeMax(Double incomeMax) {
        this.incomeMax = incomeMax;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getGovtLevel() {
        return govtLevel;
    }

    public void setGovtLevel(String govtLevel) {
        this.govtLevel = govtLevel;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public Instant getScrapedAt() {
        return scrapedAt;
    }

    public void setScrapedAt(Instant scrapedAt) {
        this.scrapedAt = scrapedAt;
    }

    public boolean isVerifiedByAdmin() {
        return verifiedByAdmin;
    }

    public void setVerifiedByAdmin(boolean verifiedByAdmin) {
        this.verifiedByAdmin = verifiedByAdmin;
    }

    // ---------------- toString (optional) ----------------
    @Override
    public String toString() {
        return "Scheme{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", incomeMin=" + incomeMin +
                ", incomeMax=" + incomeMax +
                ", category='" + category + '\'' +
                ", govtLevel='" + govtLevel + '\'' +
                ", stateName='" + stateName + '\'' +
                ", link='" + link + '\'' +
                ", source='" + source + '\'' +
                ", sourceId='" + sourceId + '\'' +
                ", scrapedAt=" + scrapedAt +
                ", verifiedByAdmin=" + verifiedByAdmin +
                '}';
    }
}
