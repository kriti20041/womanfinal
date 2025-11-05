package com.ruralwomen.platform.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class ProductSearchService {

    private static final String API_KEY = "a2843c3a794437bfa0d6d8129cf29b4af89c6a04d1eb69539f5ff814e136f0a1";
    private static final String SERP_API_URL = "https://serpapi.com/search.json";

    public List<Map<String, Object>> searchProduct(String query) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            // Build the search URL
            String url = SERP_API_URL + "?engine=google_shopping&q=" + query + "&api_key=" + API_KEY;

            // Call SerpApi
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            // Extract product results
            List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("shopping_results");

            if (results == null) {
                return Collections.emptyList();
            }

            // Simplify data (title, price, link)
            List<Map<String, Object>> simplified = new ArrayList<>();
            for (Map<String, Object> item : results) {
                Map<String, Object> simplifiedItem = new HashMap<>();
                simplifiedItem.put("title", item.get("title"));
                simplifiedItem.put("price", item.get("price"));
                simplifiedItem.put("link", item.get("link"));
                simplifiedItem.put("source", item.get("source"));
                simplified.add(simplifiedItem);
            }

            return simplified;

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
