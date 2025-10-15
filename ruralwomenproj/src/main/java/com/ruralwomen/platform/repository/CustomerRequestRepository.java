package com.ruralwomen.platform.repository;

import com.ruralwomen.platform.model.CustomerRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRequestRepository extends MongoRepository<CustomerRequest, String> {}
