package com.ruralwomen.platform.controller;

import com.ruralwomen.platform.dto.PriceResponse;
import com.ruralwomen.platform.service.PricingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/pricing")
@CrossOrigin(origins = "*")
public class PricingController {

    @Autowired
    private PricingService pricingService;

    @PostMapping("/price")
    public ResponseEntity<PriceResponse> getPrice(@RequestParam("file") MultipartFile file) {
        PriceResponse response = pricingService.handleImageAndComputePrice(file);
        return ResponseEntity.ok(response);
    }
}
