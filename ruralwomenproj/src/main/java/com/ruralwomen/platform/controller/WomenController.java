package com.ruralwomen.platform.controller;

import com.ruralwomen.platform.model.Women;
import com.ruralwomen.platform.repository.WomenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/women")
public class WomenController {

    @Autowired
    private WomenRepository womenRepository;

    @PostMapping("/add")
    public ResponseEntity<Women> addWoman(@RequestBody Women woman) {
        Women savedWoman = womenRepository.save(woman);
        return ResponseEntity.ok(savedWoman);
    }
}
