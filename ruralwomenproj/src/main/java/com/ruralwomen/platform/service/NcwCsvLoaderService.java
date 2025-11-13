package com.ruralwomen.platform.service;

import com.opencsv.CSVReader;
import com.ruralwomen.platform.dto.Scheme;
import com.ruralwomen.platform.repository.SchemeRepository;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.io.Reader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class NcwCsvLoaderService {

    private final SchemeRepository repo;

    public NcwCsvLoaderService(SchemeRepository repo) {
        this.repo = repo;
    }

    public List<Scheme> loadNcwSchemes() {
        List<Scheme> schemes = new ArrayList<>();

        try (Reader reader = new InputStreamReader(
                getClass().getResourceAsStream("/data/ncw_schemes.csv"));
             CSVReader csvReader = new CSVReader(reader)) {

            String[] line;
            boolean skipHeader = true;

            while ((line = csvReader.readNext()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }

                if (line.length < 6) continue;

                String schemeName = line[1].trim();

                // Avoid duplicate entries
                boolean exists = repo.findAll().stream()
                        .anyMatch(s -> s.getName().equalsIgnoreCase(schemeName));

                if (exists) continue;

                Scheme s = new Scheme();
                s.setGovtLevel(line[0].trim());
                s.setName(schemeName);
                s.setDescription(line[2].trim());
                s.setCategory(line[3].trim());
                s.setSource("ncw.gov.in");
                s.setLink("https://www.ncw.gov.in/publications/women-centric-schemes-by-different-ministries-of-government-of-india-goi");
                s.setScrapedAt(Instant.now());
                s.setVerifiedByAdmin(true);

                try {
                    s.setIncomeMin(Double.parseDouble(line[4].trim()));
                    s.setIncomeMax(Double.parseDouble(line[5].trim()));
                } catch (Exception ex) {
                    s.setIncomeMin(0.0);
                    s.setIncomeMax(9999999.0);
                }

                repo.save(s);
                schemes.add(s);
            }

        } catch (Exception e) {
            throw new RuntimeException("❌ Error loading NCW CSV: " + e.getMessage(), e);
        }

        return schemes;
    }
}
