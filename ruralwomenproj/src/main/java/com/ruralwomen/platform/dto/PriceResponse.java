package com.ruralwomen.platform.dto;



import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PriceResponse {
    private double computedPrice;
    private String currency;
    private String method;               // e.g., "google_shopping", "median_top_10", "no_data"
    private List<MatchedProduct> examples;
}