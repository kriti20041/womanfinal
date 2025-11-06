package com.ruralwomen.platform.dto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MatchedProduct {
    private String title;
    private String url;
    private Double price;
    private String currency;
    private String source; // e.g., "google_lens" or seller
}
