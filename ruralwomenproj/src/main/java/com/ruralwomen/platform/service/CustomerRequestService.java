package com.ruralwomen.platform.service;

import com.ruralwomen.platform.model.CustomerRequest;
import com.ruralwomen.platform.model.Women;
import com.ruralwomen.platform.repository.CustomerRequestRepository;
import com.ruralwomen.platform.repository.WomenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Service;

@Service
public class CustomerRequestService {

    @Autowired
    private CustomerRequestRepository customerRequestRepository;

    @Autowired
    private WomenRepository womenRepository;

    public CustomerRequest createRequest(CustomerRequest request) {
        // Save request to customer_requests collection
        CustomerRequest savedRequest = customerRequestRepository.save(request);

        // Add request to woman's customRequests list
        Women woman = womenRepository.findByName(request.getAssignedWoman());
        if (woman != null) {
            woman.getCustomRequests().add(savedRequest);
            womenRepository.save(woman);
        }

        return savedRequest;
    }
}
