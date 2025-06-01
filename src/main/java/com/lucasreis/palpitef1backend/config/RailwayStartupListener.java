package com.lucasreis.palpitef1backend.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
public class RailwayStartupListener implements ApplicationListener<ApplicationReadyEvent> {
    private static final Logger logger = LoggerFactory.getLogger(RailwayStartupListener.class);

    @Value("${server.port:8080}")
    private String serverPort;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        logger.info("===========================================");
        logger.info("Railway Application Started Successfully!");
        logger.info("===========================================");
        logger.info("Server Port: {}", serverPort);
        logger.info("Context Path: {}", contextPath);
        logger.info("Health Check URL: {}:{}{}/health", 
            "http://localhost", serverPort, contextPath);
        logger.info("Environment PORT: {}", System.getenv("PORT"));
        logger.info("===========================================");
    }
}