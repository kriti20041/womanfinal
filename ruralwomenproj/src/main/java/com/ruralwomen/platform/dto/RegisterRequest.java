package com.ruralwomen.platform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid mobile number")
    private String mobileNumber;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Name is required")
    private String name;

    private String email;

    @NotBlank(message = "Role is required")
    private String role; // WOMAN_ENTREPRENEUR, HUB_MANAGER, CUSTOMER

    private String village;
    private String district;
    private String state;

    // For Women Entrepreneurs
    private String businessName;
    private String businessCategory;

    // For Hub Managers
    private String assignedVillages;
    private String certificationId;

    // For Customers
    private String deliveryAddress;
    private String pincode;
}
