package com.ruralwomen.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OTPResponse {
    private String message;
    private Boolean success;
    private String otpId;
}
