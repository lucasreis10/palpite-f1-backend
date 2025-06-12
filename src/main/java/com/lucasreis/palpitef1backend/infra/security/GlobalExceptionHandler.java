package com.lucasreis.palpitef1backend.infra.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Map<String, Object>> handleExpiredJwtException(ExpiredJwtException e) {
        log.warn("Token JWT expirado: {}", e.getMessage());
        
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Token expirado");
        response.put("message", "Seu token de acesso expirou. Faça login novamente.");
        response.put("timestamp", System.currentTimeMillis());
        response.put("status", HttpStatus.UNAUTHORIZED.value());
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler({MalformedJwtException.class, UnsupportedJwtException.class, SignatureException.class})
    public ResponseEntity<Map<String, Object>> handleInvalidJwtException(JwtException e) {
        log.warn("Token JWT inválido: {}", e.getMessage());
        
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Token inválido");
        response.put("message", "Token de acesso inválido. Faça login novamente.");
        response.put("timestamp", System.currentTimeMillis());
        response.put("status", HttpStatus.UNAUTHORIZED.value());
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Map<String, Object>> handleJwtException(JwtException e) {
        log.error("Erro geral no token JWT: {}", e);
        
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Erro de autenticação");
        response.put("message", "Erro no token de acesso. Faça login novamente.");
        response.put("timestamp", System.currentTimeMillis());
        response.put("status", HttpStatus.UNAUTHORIZED.value());
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleAuthenticationException(AuthenticationException e) {
        log.warn("Erro de autenticação: {}", e.getMessage());
        
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Falha na autenticação");
        response.put("message", "Credenciais inválidas ou token expirado.");
        response.put("timestamp", System.currentTimeMillis());
        response.put("status", HttpStatus.UNAUTHORIZED.value());
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentialsException(BadCredentialsException e) {
        log.warn("Credenciais inválidas: {}", e.getMessage());
        
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Credenciais inválidas");
        response.put("message", "Email ou senha incorretos.");
        response.put("timestamp", System.currentTimeMillis());
        response.put("status", HttpStatus.UNAUTHORIZED.value());
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDeniedException(AccessDeniedException e) {
        log.warn("Acesso negado: {}", e.getMessage());
        
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Acesso negado");
        response.put("message", "Você não tem permissão para acessar este recurso.");
        response.put("timestamp", System.currentTimeMillis());
        response.put("status", HttpStatus.FORBIDDEN.value());
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception e) {
        log.error("Erro interno do servidor: {}", e.getMessage(), e);
        
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Erro interno do servidor");
        response.put("message", "Ocorreu um erro inesperado. Tente novamente mais tarde.");
        response.put("timestamp", System.currentTimeMillis());
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
} 
