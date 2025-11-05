package com.ruralwomen.platform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class MarketPredictionService {

    @Autowired
    private ProductSearchService productSearchService;

    /**
     * Fetches average market price for a given product and category.
     */
    public double fetchAverageMarketPrice(String productName, String category) {
        try {
            // Combine product name + category for a more accurate search
            String query = productName + " " + category;

            // Fetch results from SerpApi
            List<Map<String, Object>> results = productSearchService.searchProduct(query);

            if (results.isEmpty()) {
                System.out.println("⚠️ No prices found for query: " + query);
                return 0.0;
            }

            // Extract numeric price values
            List<Double> prices = new ArrayList<>();
            for (Map<String, Object> item : results) {
                Object priceObj = item.get("price");
                if (priceObj != null) {
                    String priceStr = priceObj.toString()
                            .replace("$", "")
                            .replace(",", "")
                            .trim();

                    try {
                        double price = Double.parseDouble(priceStr);
                        prices.add(price);
                    } catch (NumberFormatException e) {
                        // Ignore invalid entries
                    }
                }
            }

            if (prices.isEmpty()) {
                System.out.println("⚠️ No valid numeric prices found for query: " + query);
                return 0.0;
            }

            // Calculate the average price
            double sum = 0.0;
            for (double price : prices) {
                sum += price;
            }
            double avg = sum / prices.size();

            System.out.println("✅ Average price for '" + query + "': $" + avg);
            return avg;

        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }
}
