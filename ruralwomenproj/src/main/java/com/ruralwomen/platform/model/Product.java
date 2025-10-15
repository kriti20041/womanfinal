package com.ruralwomen.platform.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "products")
public class Product {
    @Id
    private String id;
    private String name;
    private String description;
    private String category;
    private String imageUrl; // AI-generated image will be stored here
    private double price;
    private String village;
    private String artisanName;

    private String createdByUserId; // optional: link to User.id (woman entrepreneur)
}

