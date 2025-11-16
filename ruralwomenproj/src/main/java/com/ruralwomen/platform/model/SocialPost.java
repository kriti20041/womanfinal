package com.ruralwomen.platform.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "social_posts")
public class SocialPost {
    @Id
    private String id;
    private String userId;
    private String womanName;
    private String village;
    private String businessType;
    private String caption;
    private String imageUrl;
    private String postStatus; // PENDING / POSTED / FAILED
    private LocalDateTime createdAt;

    // Getters / Setters (omitted for brevity — generate with your IDE or Lombok)
    // ...
    public String getId(){return id;} public void setId(String id){this.id = id;}
    public String getUserId(){return userId;} public void setUserId(String userId){this.userId=userId;}
    public String getWomanName(){return womanName;} public void setWomanName(String womanName){this.womanName=womanName;}
    public String getVillage(){return village;} public void setVillage(String village){this.village=village;}
    public String getBusinessType(){return businessType;} public void setBusinessType(String businessType){this.businessType=businessType;}
    public String getCaption(){return caption;} public void setCaption(String caption){this.caption=caption;}
    public String getImageUrl(){return imageUrl;} public void setImageUrl(String imageUrl){this.imageUrl=imageUrl;}
    public String getPostStatus(){return postStatus;} public void setPostStatus(String postStatus){this.postStatus=postStatus;}
    public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime createdAt){this.createdAt=createdAt;}
}
