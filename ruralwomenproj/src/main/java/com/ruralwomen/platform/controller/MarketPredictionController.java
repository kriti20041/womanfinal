package com.ruralwomen.platform.controller;

import com.ruralwomen.platform.service.MarketPredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/predict")
public class MarketPredictionController {

    @Autowired
    private MarketPredictionService service;

    @PostMapping("/predict")
    public ResponseEntity<?> predictPrice(@RequestBody Map<String, String> request) {
        String productName = request.get("name");
        String category = request.get("category");

        double price = service.fetchAverageMarketPrice(productName, category);
        return ResponseEntity.ok(Map.of(
                "product", productName,
                "predictedPrice", price,
                "currency", "USD"
        ));
    }
}
