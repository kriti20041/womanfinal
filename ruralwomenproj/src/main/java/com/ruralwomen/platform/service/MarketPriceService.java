package com.ruralwomen.platform.service;

import org.springframework.stereotype.Service;

@Service
public class MarketPriceService {

    public double predictPrice(String imageUrl, String name, String description, String category) {
        // Step 1: Identify product type
        String keyword = category != null ? category : name.toLowerCase();

        // Step 2: Approximate pricing
        switch (keyword) {
            case "basket": return 250.0;
            case "necklace": return 500.0;
            case "pottery": return 350.0;
            case "cloth": return 700.0;
            default: return 300.0;
        }
    }
}
