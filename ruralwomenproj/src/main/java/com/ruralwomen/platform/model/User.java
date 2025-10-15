package com.ruralwomen.platform.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {

    @Id
    private String id;

    @Indexed(unique = true)
    private String mobileNumber;

    private String name;
    private String password;
    private String role;     // WOMAN_ENTREPRENEUR, HUB_MANAGER, CUSTOMER
    private String village;
    private String district;
    private String state;
    private String product;

}
