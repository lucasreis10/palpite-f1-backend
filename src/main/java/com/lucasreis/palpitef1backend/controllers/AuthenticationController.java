package com.lucasreis.palpitef1backend.controllers;

import com.lucasreis.palpitef1backend.domain.auth.AuthenticationRequest;
import com.lucasreis.palpitef1backend.domain.auth.AuthenticationResponse;
import com.lucasreis.palpitef1backend.domain.auth.AuthenticationService;
import com.lucasreis.palpitef1backend.domain.auth.RegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody @Valid RegisterRequest request
    ) {
        log.debug("Received register request for email: {}", request.getEmail());
        var response = service.register(request);
        log.debug("User registered successfully: {}", response.getEmail());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest request
    ) {
        log.debug("Received login request for email: {}", request.getEmail());
        var response = service.authenticate(request);
        log.debug("User authenticated successfully: {}", response.getEmail());
        return ResponseEntity.ok(response);
    }
} 