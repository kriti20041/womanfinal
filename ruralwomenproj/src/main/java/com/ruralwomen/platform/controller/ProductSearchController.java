package com.ruralwomen.platform.controller;

import com.ruralwomen.platform.service.ProductSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductSearchController {

    @Autowired
    private ProductSearchService productSearchService;

    @GetMapping("/search")
    public ResponseEntity<?> searchProducts(@RequestParam String query) {
        return ResponseEntity.ok(productSearchService.searchProduct(query));
    }
}
