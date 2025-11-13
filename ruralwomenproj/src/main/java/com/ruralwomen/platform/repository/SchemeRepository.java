package com.ruralwomen.platform.repository;

import com.ruralwomen.platform.dto.Scheme;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface SchemeRepository extends MongoRepository<Scheme, String> {
    Scheme findBySourceAndSourceId(String source, String sourceId);
    List<Scheme> findByVerifiedByAdminFalse();


    List<Scheme> findByIncomeMinLessThanEqualAndIncomeMaxGreaterThanEqual(Double incomeMin, Double incomeMax);
}
