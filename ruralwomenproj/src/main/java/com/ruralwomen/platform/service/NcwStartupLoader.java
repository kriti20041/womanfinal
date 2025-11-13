package com.ruralwomen.platform.service;



import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class NcwStartupLoader {

    private final NcwCsvLoaderService loaderService;

    public NcwStartupLoader(NcwCsvLoaderService loaderService) {
        this.loaderService = loaderService;
    }

    @PostConstruct
    public void loadCsvOnStartup() {
        System.out.println("🚀 Loading NCW schemes automatically at startup...");
        var schemes = loaderService.loadNcwSchemes();

        if (schemes.isEmpty()) {
            System.err.println("⚠️ No NCW schemes found in CSV!");
        } else {
            System.out.println("✅ Loaded " + schemes.size() + " NCW schemes from CSV.");
        }
    }
}
