package com.lucasreis.palpitef1backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;

@RestController
public class HealthController {
    private static final Logger logger = LoggerFactory.getLogger(HealthController.class);

    @GetMapping("/health")
    public String healthCheck() {
        LocalDateTime now = LocalDateTime.now();
        logger.debug("Health check called at {}", now);
        return String.format("OK - %s", now);
    }
} 