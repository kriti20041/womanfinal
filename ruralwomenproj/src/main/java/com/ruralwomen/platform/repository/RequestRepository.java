package com.ruralwomen.platform.repository;


import com.ruralwomen.platform.model.Request;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface RequestRepository extends MongoRepository<Request, String> {
    List<Request> findByEntrepreneurId(String entrepreneurId);
    List<Request> findByCustomerId(String customerId);
}
