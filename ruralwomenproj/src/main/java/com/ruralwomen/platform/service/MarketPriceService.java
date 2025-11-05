package com.ruralwomen.platform.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.json.JSONArray;
import org.json.JSONObject;

@Service
public class MarketPriceService {

    private static final String SERP_API_KEY = "YOUR_SERP_API_KEY"; // 🔑 paste from serpapi.com
    private static final String SERP_API_URL = "https://serpapi.com/search.json";

    private final RestTemplate restTemplate = new RestTemplate();

    public double fetchAverageMarketPrice(String query) {
        try {
            // Build URL for SerpApi
            String url = SERP_API_URL + "?engine=google&q=" + query.replace(" ", "+") + "+price&api_key=" + SERP_API_KEY;

            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                JSONObject json = new JSONObject(response.getBody());
                JSONArray results = json.optJSONArray("shopping_results");

                if (results != null && results.length() > 0) {
                    double total = 0;
                    int count = 0;

                    for (int i = 0; i < results.length(); i++) {
                        JSONObject item = results.getJSONObject(i);
                        if (item.has("extracted_price")) {
                            total += item.getDouble("extracted_price");
                            count++;
                        }
                    }

                    if (count > 0) return total / count; // ✅ average price
                }
            }
        } catch (Exception e) {
            System.out.println("Error fetching from SerpApi: " + e.getMessage());
        }
        return 100.0; // fallback value
    }
}
