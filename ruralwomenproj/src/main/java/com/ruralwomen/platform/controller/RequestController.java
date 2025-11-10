package com.ruralwomen.platform.controller;


import com.ruralwomen.platform.model.Request;
import com.ruralwomen.platform.service.RequestService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/requests")
@CrossOrigin
public class RequestController {

    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    // 🟢 1. Create new request
    @PostMapping
    public Request createRequest(@RequestBody Request req) {
        return requestService.createRequest(req);
    }

    // 🟢 2. Get all requests for entrepreneur
    @GetMapping("/entrepreneur/{id}")
    public List<Request> getRequestsForEntrepreneur(@PathVariable String id) {
        return requestService.getRequestsForEntrepreneur(id);
    }

    // 🟢 3. Get requests by customer
    @GetMapping("/customer/{id}")
    public List<Request> getRequestsByCustomer(@PathVariable String id) {
        return requestService.getRequestsByCustomer(id);
    }

    // 🟢 4. Accept a request
    @PutMapping("/{id}/accept")
    public Optional<Request> acceptRequest(@PathVariable String id) {
        return requestService.acceptRequest(id);
    }

    // 🟢 5. Reject a request
    @PutMapping("/{id}/reject")
    public Optional<Request> rejectRequest(@PathVariable String id) {
        return requestService.rejectRequest(id);
    }
}
