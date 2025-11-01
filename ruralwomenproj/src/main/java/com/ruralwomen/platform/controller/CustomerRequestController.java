package com.ruralwomen.platform.controller;

import com.ruralwomen.platform.model.CustomerRequest;
import com.ruralwomen.platform.model.Women;
import com.ruralwomen.platform.repository.CustomerRequestRepository;
import com.ruralwomen.platform.repository.WomenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/custom-requests")
@CrossOrigin
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
        String customerEmail = requestBody.get("customerEmail"); // you can store if you extend model
        String prompt = requestBody.get("prompt");               // description / requested product
        String artisanId = requestBody.get("artisanId");        // artisan's _id in women collection

        // validate required fields
        if (customerName == null || prompt == null || artisanId == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Missing required fields"));
        }

        // Check if selected artisan exists
        Optional<Women> artisanOptional = womenRepository.findById(artisanId);
        if (artisanOptional.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Selected artisan not found"));
        }

        // Create a CustomerRequest object.
        // Mapping: prompt -> requestedProduct (you may extend model to include description/email)
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setCustomerName(customerName);
        customerRequest.setRequestedProduct(prompt);
        customerRequest.setAssignedWoman(artisanId); // store artisan's id
        customerRequest.setStatus("Pending");
        customerRequest.setPrice(null); // or parse from requestBody if present

        // Save request in customer_requests collection
        CustomerRequest savedRequest = customerRequestRepository.save(customerRequest);

        // Add request to artisan's customRequests list (store the full request object)
        Women artisan = artisanOptional.get();
        if (artisan.getCustomRequests() == null) {
            artisan.setCustomRequests(new ArrayList<>());
        }
        artisan.getCustomRequests().add(savedRequest);
        womenRepository.save(artisan);

        // Return response
        return ResponseEntity.ok(Map.of(
                "message", "Request submitted successfully",
                "requestId", savedRequest.getId()
        ));
    }

    // 2️⃣ Get all requests for a specific artisan (by artisan id)
    @GetMapping("/artisan/{artisanId}")
    public ResponseEntity<List<CustomerRequest>> getRequestsForArtisan(@PathVariable String artisanId) {
        // Filter requests by artisanId (assignedWoman stores artisan id)
        List<CustomerRequest> requests = customerRequestRepository.findAll()
                .stream()
                .filter(r -> artisanId.equals(r.getAssignedWoman()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(requests);
    }

    // 3️⃣ Get all customer requests
    @GetMapping
    public ResponseEntity<List<CustomerRequest>> getAllRequests() {
        return ResponseEntity.ok(customerRequestRepository.findAll());
    }
}
