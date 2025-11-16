package com.ruralwomen.platform.service;



import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageUploadService {

    private final String uploadDir = "uploads/"; // you can put this outside src for persistence

    public String upload(MultipartFile file) {
        try {
            // Create uploads folder if not exists
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Generate unique name
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + fileName);

            // Save file to server
            Files.write(filePath, file.getBytes());

            // Return accessible path (can be served by controller)
            return "/uploads/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image: " + e.getMessage());
        }
    }
}
