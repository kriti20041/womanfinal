package com.ruralwomen.platform.service;

import com.ruralwomen.platform.dto.Scheme;
import com.ruralwomen.platform.repository.SchemeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SchemeService {

    private final SchemeRepository repo;

    public SchemeService(SchemeRepository repo) {
        this.repo = repo;
    }

    // Returns only top 3 relevant schemes based on income
    public List<Scheme> getSchemesByIncome(Double income) {
        return repo.findAll().stream()
                .filter(s ->
                        s.getIncomeMin() != null &&
                                s.getIncomeMax() != null &&
                                income >= s.getIncomeMin() &&
                                income <= s.getIncomeMax())
                .limit(3) // 👈 show only 3 schemes
                .collect(Collectors.toList());
    }

    public List<Scheme> getAllSchemes() {
        return repo.findAll();
    }
}
