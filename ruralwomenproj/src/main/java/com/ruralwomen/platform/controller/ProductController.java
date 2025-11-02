package com.ruralwomen.platform.controller;

import com.ruralwomen.platform.model.Product;
import com.ruralwomen.platform.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;   // ✅ This injects your service automatically

    @PostMapping("/upload")
    public ResponseEntity<Product> uploadProduct(
            @RequestParam("image") MultipartFile image,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("category") String category,
            @RequestParam("village") String village,
            @RequestParam("artisanName") String artisanName,
            @RequestParam("userId") String userId) {

        Product savedProduct = productService.saveProduct(
                image, name, description, category, village, artisanName, userId);

        return ResponseEntity.ok(savedProduct);
    }
}
