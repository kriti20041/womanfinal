package com.ruralwomen.platform.repository;

import com.ruralwomen.platform.model.MarketPrediction;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MarketPredictionRepository extends MongoRepository<MarketPrediction, String> { }
