package com.ruralwomen.platform.controller;

import com.ruralwomen.platform.model.Product;
import com.ruralwomen.platform.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/upload")
    public Product uploadProduct(@RequestParam("file") MultipartFile file,
                                 @RequestParam("name") String name,
                                 @RequestParam("description") String description,
                                 @RequestParam("category") String category,
                                 @RequestParam("village") String village,
                                 @RequestParam("artisanName") String artisanName,
                                 @RequestParam("createdByUserId") String createdByUserId) throws Exception {

        return productService.saveProduct(file, name, description, category, village, artisanName, createdByUserId);
    }
}
