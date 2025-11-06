package com.ruralwomen.platform.client; // or .service if you prefer

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruralwomen.platform.dto.MatchedProduct;
import com.ruralwomen.platform.dto.PriceResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class SerpApiClient {

    @Value("${serpapi.api.key}")
    private String serpApiKey;

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Call SerpAPI (google_shopping) with the image URL (or a text query).
     * Extracts prices from shopping_results and returns PriceResponse with examples.
     */
    public PriceResponse computeDynamicPrice(String imageUrlOrQuery) {
        try {
            // If imageUrlOrQuery looks like a URL, use google_lens; otherwise fallback to google_shopping text search.
            boolean looksLikeUrl = imageUrlOrQuery.startsWith("http://") || imageUrlOrQuery.startsWith("https://");
            String endpoint;
            if (looksLikeUrl) {
                String encoded = URLEncoder.encode(imageUrlOrQuery, StandardCharsets.UTF_8);
                endpoint = "https://serpapi.com/search.json?engine=google_lens&url=" + encoded + "&api_key=" + serpApiKey;
            } else {
                String encoded = URLEncoder.encode(imageUrlOrQuery, StandardCharsets.UTF_8);
                endpoint = "https://serpapi.com/search.json?engine=google_shopping&q=" + encoded + "&api_key=" + serpApiKey;
            }

            Request request = new Request.Builder().url(endpoint).get().build();
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful() || response.body() == null) {
                    return new PriceResponse(0.0, "INR", "api_error", Collections.emptyList());
                }

                JsonNode root = mapper.readTree(response.body().string());

                List<MatchedProduct> examples = new ArrayList<>();
                List<Double> prices = new ArrayList<>();

                // 1) Try shopping_results (best place for structured prices)
                JsonNode shopping = root.path("shopping_results");
                if (shopping.isArray() && shopping.size() > 0) {
                    for (JsonNode node : shopping) {
                        String title = node.path("title").asText(null);
                        String link = node.path("link").asText(null);
                        String source = node.path("source").asText(null);
                        String priceText = node.path("price").asText(null);

                        Double price = parsePrice(priceText);
                        if (price != null) prices.add(price);

                        examples.add(new MatchedProduct(title, link, price, "INR", source));
                    }
                }

                // 2) If no shopping_results, try visual_matches (Google Lens)
                if (examples.isEmpty() && root.has("visual_matches")) {
                    for (JsonNode node : root.get("visual_matches")) {
                        String title = node.path("title").asText(null);
                        String link = node.path("link").asText(null);
                        String source = node.path("source").asText(null);

                        // try multiple places for a price
                        Double price = null;
                        if (node.has("price")) price = parsePrice(node.path("price").asText(null));
                        if (price == null && node.has("extracted_price")) price = parsePrice(node.path("extracted_price").asText(null));
                        // sometimes price info is nested inside "shopping_results" or other children - try to find any number
                        if (price == null) {
                            String textified = node.toString();
                            price = extractFirstNumberFromText(textified);
                        }

                        if (price != null) prices.add(price);
                        examples.add(new MatchedProduct(title, link, price, "INR", source));
                    }
                }

                // 3) Final fallback: scan organic_results for rich snippets or price mentions
                if (examples.isEmpty() && root.has("organic_results")) {
                    for (JsonNode node : root.get("organic_results")) {
                        String title = node.path("title").asText(null);
                        String link = node.path("link").asText(null);
                        String source = node.path("displayed_link").asText(null);

                        Double price = null;
                        if (node.has("rich_snippet")) {
                            JsonNode rs = node.path("rich_snippet");
                            if (rs.has("top") && rs.get("top").has("metatags")) {
                                String t = rs.get("top").toString();
                                price = extractFirstNumberFromText(t);
                            }
                        }

                        if (price != null) prices.add(price);
                        examples.add(new MatchedProduct(title, link, price, "INR", source));
                    }
                }

                // Compute median if we have prices
                double computed = 0.0;
                String method = "no_data";
                if (!prices.isEmpty()) {
                    Collections.sort(prices);
                    // take median
                    int n = prices.size();
                    if (n % 2 == 1) computed = prices.get(n/2);
                    else computed = (prices.get(n/2 - 1) + prices.get(n/2)) / 2.0;
                    method = looksLikeUrl ? "google_lens_median" : "google_shopping_median";
                }

                return new PriceResponse(computed, "INR", method, examples);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new PriceResponse(0.0, "INR", "exception", Collections.emptyList());
        }
    }

    private Double parsePrice(String text) {
        if (text == null) return null;
        try {
            // remove currency symbols and commas, keep digits and dot
            String cleaned = text.replaceAll("[^0-9.]", "");
            if (cleaned.isEmpty()) return null;
            return Double.parseDouble(cleaned);
        } catch (Exception ex) {
            return null;
        }
    }

    private Double extractFirstNumberFromText(String text) {
        if (text == null) return null;
        // find the first numeric token like 1,234.56 or 1234.56
        java.util.regex.Matcher m = java.util.regex.Pattern.compile("([0-9]{1,3}(?:[,][0-9]{3})*(?:\\.[0-9]+)?)|([0-9]+(?:\\.[0-9]+)?)").matcher(text);
        if (m.find()) {
            String found = m.group().replaceAll(",", "");
            try { return Double.parseDouble(found); } catch (Exception ignored) {}
        }
        return null;
    }
}
