package com.ruralwomen.platform.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ruralwomen.platform.model.Product;
import com.ruralwomen.platform.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MarketPriceService marketPriceService;

    @Autowired
    private Cloudinary cloudinary;

    public Product saveProduct(MultipartFile file, String name, String description, String category,
                               String village, String artisanName, String createdByUserId) throws IOException {

        // 1️⃣ Upload image to Cloudinary
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("folder", "products"));

        String imageUrl = (String) uploadResult.get("secure_url");

        // 2️⃣ Fetch real market price from SerpApi
        double predictedPrice = marketPriceService.fetchAverageMarketPrice(name + " " + category);

        // 3️⃣ Save product to MongoDB
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setCategory(category);
        product.setImageUrl(imageUrl);
        product.setVillage(village);
        product.setArtisanName(artisanName);
        product.setCreatedByUserId(createdByUserId);
        product.setPrice(predictedPrice);

        return productRepository.save(product);
    }
}
