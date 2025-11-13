package com.ruralwomen.platform.controller;

import com.ruralwomen.platform.dto.Scheme;
import com.ruralwomen.platform.service.NcwCsvLoaderService;
import com.ruralwomen.platform.service.SchemeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schemes")
@CrossOrigin("*")
public class SchemeController {

    private final NcwCsvLoaderService ncwCsvLoaderService;
    private final SchemeService schemeService;

    public SchemeController(NcwCsvLoaderService ncwCsvLoaderService, SchemeService schemeService) {
        this.ncwCsvLoaderService = ncwCsvLoaderService;
        this.schemeService = schemeService;
    }


    @GetMapping("/byIncome")
    public ResponseEntity<List<Scheme>> getSchemesByIncome(@RequestParam double income) {
        List<Scheme> filtered = schemeService.getSchemesByIncome(income);
        return ResponseEntity.ok(filtered);
    }
}
