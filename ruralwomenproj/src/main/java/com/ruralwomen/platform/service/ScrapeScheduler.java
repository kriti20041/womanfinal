package com.ruralwomen.platform.service;

import org.springframework.stereotype.Component;

@Component
public class ScrapeScheduler {

    private final SchemeScraperService service;

    public ScrapeScheduler(SchemeScraperService service) {
        this.service = service;
    }

    // ❌ Disabled automatic fetching from data.gov.in
    // @Scheduled(fixedRate = 43200000)
    public void scheduledFetch() {
        try {
            // service.fetchFromDataGov();
            // System.out.println("✅ Schemes updated successfully from data.gov.in");
            System.out.println("⏸ Auto data.gov.in scraping is disabled.");
        } catch (Exception e) {
            System.err.println("❌ Failed to fetch schemes: " + e.getMessage());
        }
    }
}
