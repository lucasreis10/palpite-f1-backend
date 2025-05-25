package com.lucasreis.palpitef1backend.controllers;

import com.lucasreis.palpitef1backend.domain.user.User;
import com.lucasreis.palpitef1backend.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserRepository userRepository;
    
    @GetMapping
    public ResponseEntity<List<UserSummary>> getAllUsers() {
        log.debug("Requisição para listar todos os usuários");
        List<UserSummary> users = userRepository.findAll()
                .stream()
                .map(user -> new UserSummary(user.getId(), user.getName(), user.getEmail()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }
    
    public record UserSummary(Long id, String name, String email) {}
} 