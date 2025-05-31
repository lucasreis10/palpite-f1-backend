package com.lucasreis.palpitef1backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class HealthController {
    
    private static final Logger logger = LoggerFactory.getLogger(HealthController.class);

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        logger.info("Health check endpoint called");
        return ResponseEntity.ok("OK");
    }
} 