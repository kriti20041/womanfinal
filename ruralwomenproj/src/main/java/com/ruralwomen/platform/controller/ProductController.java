package com.ruralwomen.platform.controller;

import com.ruralwomen.platform.model.Product;
import com.ruralwomen.platform.repository.ProductRepository;
import com.ruralwomen.platform.service.ImageGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ImageGenerationService imageGenerationService;

    // --- Add a new product ---
    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        Product saved = productRepository.save(product);
        return ResponseEntity.ok(saved);
    }

    // --- Get all products ---
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productRepository.findAll());
    }

    // --- Get product by ID ---
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        Optional<Product> optional = productRepository.findById(id);
        return optional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // --- Generate AI Image for a product ---
    @PostMapping("/{id}/generate-image")
    public ResponseEntity<?> generateImage(@PathVariable String id) {
        Optional<Product> optional = productRepository.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Product product = optional.get();

        // Build prompt from product details
        String prompt = "Showcase the product: " + product.getName() +
                ". Description: " + product.getDescription() +
                ". Artisan: " + product.getArtisanName() +
                ". Village: " + product.getVillage();

        try {
            // Call AI service to generate image
            String imageUrl = imageGenerationService.generateImage(prompt);

            // Save image URL in MongoDB
            product.setImageUrl(imageUrl);
            productRepository.save(product);

            return ResponseEntity.ok(Map.of(
                    "message", "Image generated successfully!",
                    "image_url", imageUrl
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // --- Delete product ---
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable String id) {
        if (!productRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        productRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Product deleted successfully"));
    }
}
