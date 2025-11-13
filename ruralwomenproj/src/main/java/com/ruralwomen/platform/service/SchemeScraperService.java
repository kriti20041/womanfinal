package com.ruralwomen.platform.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruralwomen.platform.dto.Scheme;
import com.ruralwomen.platform.repository.SchemeRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class SchemeScraperService {

    private final SchemeRepository repo;

    public SchemeScraperService(SchemeRepository repo) {
        this.repo = repo;
    }

    private static final String DATASET_URL =
            "https://api.data.gov.in/resource/56a08a8b-004e-46da-bb77-9f8d1a8a7f84?format=json&limit=50&api-key=579b464db66ec23bdd000001b1f92e7f116b42b45aaf0c3bb9798aff";

    public List<Scheme> fetchFromDataGov() {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();

        try {
            String response = restTemplate.getForObject(DATASET_URL, String.class);
            JsonNode root = mapper.readTree(response);
            JsonNode records = root.path("records");

            List<Scheme> schemes = new ArrayList<>();

            for (JsonNode record : records) {
                Scheme s = new Scheme();
                s.setName(record.path("scheme").asText("Unnamed Scheme"));
                s.setDescription(record.path("description").asText("No description available"));
                s.setCategory("Women Empowerment");
                s.setGovtLevel(record.path("ministry_department").asText("Unknown Ministry"));
                s.setSource("data.gov.in");
                s.setLink(record.path("url").asText("https://data.gov.in"));
                s.setScrapedAt(Instant.now());
                s.setVerifiedByAdmin(false);

                repo.save(s);
                schemes.add(s);
            }

            return schemes;

        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to fetch data from data.gov.in: " + e.getMessage(), e);
        }
    }
}
