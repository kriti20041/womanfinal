package com.ruralwomen.platform.controller;

import com.ruralwomen.platform.model.CustomerRequest;
import com.ruralwomen.platform.model.Women;
import com.ruralwomen.platform.repository.CustomerRequestRepository;
import com.ruralwomen.platform.repository.WomenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/custom-requests")
public class CustomerRequestController {

    @Autowired
    private CustomerRequestRepository customerRequestRepository;

    @Autowired
    private WomenRepository womenRepository;

    // 1️⃣ Customer creates a new request
    @PostMapping
    public ResponseEntity<?> createRequest(@RequestBody Map<String, String> requestBody) {
        // Extract request data
        String customerName = requestBody.get("customerName");
        String customerEmail = requestBody.get("customerEmail");
        String prompt = requestBody.get("prompt");
        String artisanId = requestBody.get("artisanId");

        // Check if selected artisan exists
        Optional<Women> artisanOptional = womenRepository.findById(artisanId);
        if (artisanOptional.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Selected artisan not found"));
        }

        // Save request in customerRequest collection
        CustomerRequest customerRequest = new CustomerRequest(customerName, customerEmail, prompt, artisanId);
        customerRequestRepository.save(customerRequest);

        // Add request to artisan's customRequests list
        Women artisan = artisanOptional.get();
        if (artisan.getCustomRequests() == null) {
            artisan.setCustomRequests(new ArrayList<>());
        }
        artisan.getCustomRequests().add(prompt);
        womenRepository.save(artisan);

        // Return response
        return ResponseEntity.ok(Map.of(
                "message", "Request submitted successfully",
                "requestId", customerRequest.getId()
        ));
    }

    // 2️⃣ Get all requests for a specific artisan
    @GetMapping("/artisan/{artisanId}")
    public ResponseEntity<List<CustomerRequest>> getRequestsForArtisan(@PathVariable String artisanId) {
        // Filter requests by artisanId
        List<CustomerRequest> requests = customerRequestRepository.findAll()
                .stream()
                .filter(r -> r.getArtisanId().equals(artisanId))
                .toList();

        return ResponseEntity.ok(requests);
    }

    // 3️⃣ Get all customer requests
    @GetMapping
    public ResponseEntity<List<CustomerRequest>> getAllRequests() {
        return ResponseEntity.ok(customerRequestRepository.findAll());
    }
}
