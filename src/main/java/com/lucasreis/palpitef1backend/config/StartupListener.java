package com.lucasreis.palpitef1backend.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class StartupListener implements ApplicationListener<ApplicationReadyEvent> {
    
    private static final Logger logger = LoggerFactory.getLogger(StartupListener.class);

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        logger.info("==============================================");
        logger.info("Application started successfully!");
        logger.info("Health endpoint should be available at: /api/health");
        logger.info("==============================================");
    }
} 