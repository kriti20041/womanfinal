package com.ruralwomen.platform.repository;

import com.ruralwomen.platform.model.Women;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WomenRepository extends MongoRepository<Women, String> {
    Women findByName(String name);
}
