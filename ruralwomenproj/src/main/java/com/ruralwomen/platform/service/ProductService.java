package com.ruralwomen.platform.service;

import com.ruralwomen.platform.model.Product;
import com.ruralwomen.platform.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

@Service
public class ProductService {

    private static final String UPLOAD_DIR = "uploads/";

    @Autowired
    private ProductRepository productRepository;

    public Product saveProduct(MultipartFile image,
                               String name,
                               String description,
                               String category,
                               String village,
                               String artisanName,
                               String userId) {

        try {
            // Ensure upload directory exists
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            //  Save the uploaded file
            String fileName = Instant.now().getEpochSecond() + "_" + image.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR + fileName);
            Files.copy(image.getInputStream(), filePath);

            // Create Product object
            Product product = new Product();
            product.setName(name);
            product.setDescription(description);
            product.setCategory(category);
            product.setVillage(village);
            product.setArtisanName(artisanName);
            product.setCreatedByUserId(userId);
            product.setImageUrl("/uploads/" + fileName);

            // Temporary: we’ll later add AI-based market price prediction here
            product.setPrice(0.0);

            //  Save to MongoDB
            return productRepository.save(product);

        } catch (IOException e) {
            throw new RuntimeException("Failed to save product image", e);
        }
    }

    // Optional: Get all products
    public Iterable<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
