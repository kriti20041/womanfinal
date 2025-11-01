package com.ruralwomen.platform.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruralwomen.platform.model.SocialPost;
import com.ruralwomen.platform.repository.SocialPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.HttpURLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Map;

@Service
public class SocialPostService {

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private SocialPostRepository repository;

    @Value("${ollama.url}")                 // fill in application.properties
    private String ollamaUrl;

    @Value("${stable.diffusion.url}")       // fill in application.properties
    private String stableDiffusionUrl;

    @Value("${instagram.access.token}")     // fill in application.properties
    private String instaAccessToken;

    @Value("${instagram.user.id}")          // fill in application.properties
    private String instaUserId;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    /**
     * Public method you call from controller.
     * womanName, village, businessType come from your Women collection.
     */
    public SocialPost createAndPost(String userId, String womanName, String village, String businessType) {
        SocialPost post = new SocialPost();
        post.setUserId(userId);
        post.setWomanName(womanName);
        post.setVillage(village);
        post.setBusinessType(businessType);
        post.setCreatedAt(LocalDateTime.now());
        post.setPostStatus("PENDING");
        repository.save(post);

        try {
            // 1) create caption via Ollama (Mistral)
            String captionPrompt = buildCaptionPrompt(womanName, village, businessType);
            String caption = callOllamaForCaption(captionPrompt);

            // 2) create image via Stable Diffusion
            String imagePrompt = buildImagePrompt(womanName, village, businessType);
            byte[] imageBytes = callStableDiffusionForImage(imagePrompt);

            // 3) upload image to Cloudinary
            String imageUrl = uploadToCloudinary(imageBytes, "naari_udyami");

            // 4) post to Instagram (Graph API requires public HTTPS image_url)
            String creationId = createInstagramMedia(imageUrl, caption);
            publishInstagramMedia(creationId);

            // 5) update and save post
            post.setCaption(caption);
            post.setImageUrl(imageUrl);
            post.setPostStatus("POSTED");
            post.setCreatedAt(LocalDateTime.now());
            repository.save(post);
            return post;
        } catch (Exception e) {
            e.printStackTrace();
            post.setPostStatus("FAILED");
            repository.save(post);
            throw new RuntimeException("Failed to create/post: " + e.getMessage(), e);
        }
    }

    // -----------------------
    // Helper methods
    // -----------------------

    private String buildCaptionPrompt(String name, String village, String businessType) {
        return String.format(
                "Write a short (1-2 sentences) upbeat Instagram caption introducing %s from %s, who runs a %s. "
                        + "Include hashtags: #NaariUdyami #WomenEntrepreneurs #MadeInIndia and make it empowering.",
                name, village, businessType
        );
    }

    private String buildImagePrompt(String name, String village, String businessType) {
        return String.format(
                "Photorealistic promotional portrait of %s, a rural woman entrepreneur from %s, showcasing her %s. "
                        + "Warm natural light, smiling, high-quality, 3:4 crop, suitable for Instagram post.",
                name, village, businessType
        );
    }

    // ----- Ollama call (text generation) -----
    private String callOllamaForCaption(String prompt) throws Exception {
        // Ollama local completions endpoint (default port usually 11434)
        // NOTE: set ollamaUrl in properties, e.g. http://localhost:11434
        String payload = String.format("{\"model\":\"mistral\",\"prompt\":\"%s\"}", escapeJson(prompt));

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(ollamaUrl + "/v1/completions"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        JsonNode root = objectMapper.readTree(resp.body());

        // two common shapes: { "choices":[{"text":"..."}] } or { "response":"..." } — try both safely.
        if (root.has("choices") && root.get("choices").isArray() && root.get("choices").size() > 0) {
            return root.get("choices").get(0).path("text").asText().trim();
        } else if (root.has("response")) {
            return root.get("response").asText().trim();
        } else {
            return resp.body(); // fallback (raw)
        }
    }

    // ----- Stable Diffusion call (image generation) -----
    private byte[] callStableDiffusionForImage(String prompt) throws Exception {
        // stableDiffusionUrl should be e.g. http://localhost:7860 for AUTOMATIC1111 or your SD server root
        // We call /sdapi/v1/txt2img (Automatic1111) and parse base64 image from JSON "images":[...]
        String payload = String.format("{\"prompt\":\"%s\",\"width\":768,\"height\":1024,\"steps\":20}", escapeJson(prompt));

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(stableDiffusionUrl + "/sdapi/v1/txt2img"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        JsonNode root = objectMapper.readTree(resp.body());

        if (root.has("images") && root.get("images").isArray() && root.get("images").size() > 0) {
            String b64 = root.get("images").get(0).asText();
            // Automatic1111 returns raw base64 (no data:prefix)
            return Base64.getDecoder().decode(b64);
        } else {
            throw new RuntimeException("Stable Diffusion response did not contain images: " + resp.body());
        }
    }

    // ----- Cloudinary upload -----
    private String uploadToCloudinary(byte[] imageBytes, String folder) throws Exception {
        Map uploadResult = cloudinary.uploader().upload(imageBytes, ObjectUtils.asMap(
                "resource_type", "image",
                "folder", folder
        ));
        return (String) uploadResult.get("secure_url");
    }

    // ----- Instagram Graph API methods -----
    // Returns creation id
    private String createInstagramMedia(String imageUrl, String caption) throws Exception {
        // Instagram requires a publicly accessible HTTPS image_url
        String endpoint = String.format("https://graph.facebook.com/v21.0/%s/media", instaUserId);

        String params = String.format("image_url=%s&caption=%s&access_token=%s",
                urlEncode(imageUrl), urlEncode(caption), urlEncode(instaAccessToken));

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(endpoint + "?" + params))
                .POST(HttpRequest.BodyPublishers.noBody())
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        JsonNode root = objectMapper.readTree(resp.body());
        if (root.has("id")) {
            return root.get("id").asText();
        } else {
            throw new RuntimeException("Instagram create media failed: " + resp.body());
        }
    }

    private void publishInstagramMedia(String creationId) throws Exception {
        String endpoint = String.format("https://graph.facebook.com/v21.0/%s/media_publish", instaUserId);
        String params = String.format("creation_id=%s&access_token=%s", urlEncode(creationId), urlEncode(instaAccessToken));

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(endpoint + "?" + params))
                .POST(HttpRequest.BodyPublishers.noBody())
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        // optional: validate response contains "id" (instagram post id)
        JsonNode root = objectMapper.readTree(resp.body());
        if (!root.has("id")) {
            throw new RuntimeException("Instagram publish failed: " + resp.body());
        }
    }

    // -----------------------
    // Utility helpers
    // -----------------------
    private static String escapeJson(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static String urlEncode(String s) {
        try { return java.net.URLEncoder.encode(s, java.nio.charset.StandardCharsets.UTF_8.toString()); }
        catch (Exception e) { return s; }
    }
}
