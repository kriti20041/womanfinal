package com.ruralwomen.platform.repository;

import com.ruralwomen.platform.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByCreatedByUserId(String userId);
}

