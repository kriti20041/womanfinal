package com.ruralwomen.platform.controller;

import com.ruralwomen.platform.dto.Scheme;
import com.ruralwomen.platform.service.NcwCsvLoaderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scrape") // 👈 Changed base path to avoid collision
@CrossOrigin(origins = "*")
public class ScrapeController {

    private final NcwCsvLoaderService ncwCsvLoaderService;

    public ScrapeController(NcwCsvLoaderService ncwCsvLoaderService) {
        this.ncwCsvLoaderService = ncwCsvLoaderService;
    }

    /**
     * ✅ Loads schemes from the NCW CSV file into MongoDB
     * URL: GET http://localhost:8082/api/scrape/load-ncw
     */
    @GetMapping("/load-ncw")
    public ResponseEntity<List<Scheme>> loadNcwSchemes() {
        System.out.println("🚀 Loading NCW Schemes from CSV...");
        List<Scheme> loadedSchemes = ncwCsvLoaderService.loadNcwSchemes();

        if (loadedSchemes.isEmpty()) {
            System.err.println("⚠️ No schemes loaded — check if /data/ncw_schemes.csv exists.");
            return ResponseEntity.noContent().build();
        }

        System.out.println("✅ Successfully loaded " + loadedSchemes.size() + " schemes from CSV.");
        return ResponseEntity.ok(loadedSchemes);
    }
}
