package com.ruralwomen.platform.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ruralwomen.platform.client.SerpApiClient;
import com.ruralwomen.platform.dto.MatchedProduct;
import com.ruralwomen.platform.dto.PriceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class PricingService {

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private SerpApiClient serpApiClient;

    /**
     * Handles uploaded image, uploads to Cloudinary, then gets dynamic price via SerpAPI.
     */
    public PriceResponse handleImageAndComputePrice(MultipartFile file) {
        try {
            // ✅ Step 1: Upload image to Cloudinary
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = (String) uploadResult.get("url");

            System.out.println("✅ Uploaded image to Cloudinary: " + imageUrl);

            // ✅ Step 2: Fetch similar products via SerpAPI
            PriceResponse response = serpApiClient.computeDynamicPrice(imageUrl);

            // ✅ Step 3: Add metadata for traceability
            if (response == null) {
                return new PriceResponse(0.0, "INR", "no_data", List.of());
            }

            return response;

        } catch (IOException e) {
            e.printStackTrace();
            return new PriceResponse(0.0, "INR", "upload_error", List.of());
        }
    }
}
