package com.lucasreis.palpitef1backend.controllers;

import com.lucasreis.palpitef1backend.domain.guess.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/guesses")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class GuessController {
    
    private final GuessService guessService;
    
    // ========== ENDPOINTS PARA USUÁRIOS ==========
    
    @PostMapping("/user/{userId}")
    public ResponseEntity<GuessResponse> createGuess(
            @PathVariable Long userId,
            @RequestBody @Valid CreateGuessRequest request) {
        log.debug("Requisição para criar palpite do usuário {} para GP {} tipo {}", 
                userId, request.getGrandPrixId(), request.getGuessType());
        
        GuessResponse guess = guessService.createGuess(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(guess);
    }
    
    @PutMapping("/user/{userId}/{guessId}")
    public ResponseEntity<GuessResponse> updateGuess(
            @PathVariable Long userId,
            @PathVariable Long guessId,
            @RequestBody @Valid UpdateGuessRequest request) {
        log.debug("Requisição para atualizar palpite {} do usuário {}", guessId, userId);
        
        GuessResponse guess = guessService.updateGuess(userId, guessId, request);
        return ResponseEntity.ok(guess);
    }
    
    @GetMapping("/{guessId}")
    public ResponseEntity<GuessResponse> getGuessById(@PathVariable Long guessId) {
        log.debug("Requisição para buscar palpite por ID: {}", guessId);
        
        GuessResponse guess = guessService.getGuessById(guessId);
        return ResponseEntity.ok(guess);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<GuessResponse>> getUserGuesses(@PathVariable Long userId) {
        log.debug("Requisição para buscar todos os palpites do usuário: {}", userId);
        
        List<GuessResponse> guesses = guessService.getUserGuesses(userId);
        return ResponseEntity.ok(guesses);
    }
    

    
    @GetMapping("/user/{userId}/season/{season}")
    public ResponseEntity<List<GuessResponse>> getUserGuessesBySeason(
            @PathVariable Long userId,
            @PathVariable Integer season) {
        log.debug("Requisição para buscar palpites do usuário {} na temporada {}", userId, season);
        
        List<GuessResponse> guesses = guessService.getUserGuessesBySeason(userId, season);
        return ResponseEntity.ok(guesses);
    }
    
    @GetMapping("/user/{userId}/grand-prix/{grandPrixId}")
    public ResponseEntity<GuessResponse> getUserGuessForGrandPrix(
            @PathVariable Long userId,
            @PathVariable Long grandPrixId,
            @RequestParam GuessType guessType) {
        log.debug("Requisição para buscar palpite do usuário {} para GP {} tipo {}", 
                userId, grandPrixId, guessType);
        
        GuessResponse guess = guessService.getUserGuessForGrandPrix(userId, grandPrixId, guessType);
        
        if (guess == null) {
            log.debug("Palpite não encontrado para usuário {} GP {} tipo {}", userId, grandPrixId, guessType);
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(guess);
    }
    
    @DeleteMapping("/user/{userId}/{guessId}")
    public ResponseEntity<Void> deleteGuess(
            @PathVariable Long userId,
            @PathVariable Long guessId) {
        log.debug("Requisição para deletar palpite {} do usuário {}", guessId, userId);
        
        guessService.deleteGuess(userId, guessId);
        return ResponseEntity.noContent().build();
    }
    
    // ========== ENDPOINTS PARA CONSULTAS GERAIS ==========
    
    @GetMapping("/grand-prix/{grandPrixId}")
    public ResponseEntity<List<GuessResponse>> getGrandPrixGuesses(
            @PathVariable Long grandPrixId,
            @RequestParam GuessType guessType) {
        log.debug("Requisição para buscar palpites do GP {} tipo {}", grandPrixId, guessType);
        
        List<GuessResponse> guesses = guessService.getGrandPrixGuesses(grandPrixId, guessType);
        return ResponseEntity.ok(guesses);
    }
    
    @GetMapping("/grand-prix/{grandPrixId}/ranking")
    public ResponseEntity<List<GuessResponse>> getGrandPrixRanking(
            @PathVariable Long grandPrixId,
            @RequestParam GuessType guessType) {
        log.debug("Requisição para buscar ranking do GP {} tipo {}", grandPrixId, guessType);
        
        List<GuessResponse> ranking = guessService.getGrandPrixRanking(grandPrixId, guessType);
        return ResponseEntity.ok(ranking);
    }
    
    // ========== ENDPOINTS ADMINISTRATIVOS ==========
    
    @PostMapping("/admin/calculate-scores")
        public ResponseEntity<CalculateScoresResponse> setRealResultAndCalculateScores(
                @RequestBody @Valid SetRealResultRequest request) {
            log.debug("Requisição administrativa para definir resultado real e calcular pontuações para GP {} tipo {}",
                    request.getGrandPrixId(), request.getGuessType());

            CalculateScoresResponse response = guessService.setRealResultAndCalculateScores(request);
            return ResponseEntity.ok(response);
    }
    
    // ========== ENDPOINTS PARA RANKING E ESTATÍSTICAS ==========
    
    @GetMapping("/season/{season}/ranking")
    public ResponseEntity<List<Object[]>> getSeasonGeneralRanking(@PathVariable Integer season) {
        log.debug("Requisição para buscar ranking geral da temporada {}", season);
        
        List<Object[]> ranking = guessService.getSeasonGeneralRanking(season);
        return ResponseEntity.ok(ranking);
    }
    
    @GetMapping("/season/{season}/ranking/{guessType}")
    public ResponseEntity<List<GuessResponse>> getSeasonRankingByType(
            @PathVariable Integer season,
            @PathVariable GuessType guessType) {
        log.debug("Requisição para buscar ranking da temporada {} por tipo {}", season, guessType);
        
        List<GuessResponse> ranking = guessService.getSeasonRankingByGuessType(season, guessType);
        return ResponseEntity.ok(ranking);
    }
    
    // ========== ENDPOINTS DE HISTÓRICO ==========
    
    @GetMapping("/grand-prix/{grandPrixId}/history")
    public ResponseEntity<String> getGrandPrixHistory(@PathVariable Long grandPrixId) {
        log.debug("Requisição para buscar histórico do GP {}", grandPrixId);
        return ResponseEntity.ok("Histórico do GP " + grandPrixId + " - Funcionalidade em desenvolvimento");
    }
    
    @GetMapping("/test-history")
    public ResponseEntity<String> testHistory() {
        return ResponseEntity.ok("Endpoint de histórico funcionando no GuessController!");
    }
    

    
    // ========== ENDPOINT PARA LIVE TIMING ==========
    
    @PostMapping("/live-timing")
    public ResponseEntity<LiveTimingResponse> calculateLiveTiming(
            @RequestBody @Valid LiveTimingRequest request) {
        log.debug("Requisição para calcular live timing do GP {} tipo {}", 
                request.getGrandPrixId(), request.getSessionType());
        
        LiveTimingResponse response = guessService.calculateLiveTiming(request);
        return ResponseEntity.ok(response);
    }
    
    // ========== ENDPOINT PARA CALCULADORA ==========
    
    @PostMapping("/calculate-score")
    public ResponseEntity<CalculatorResponse> calculateScore(
            @RequestBody @Valid CalculatorRequest request) {
        log.debug("Requisição para calcular pontuação da calculadora - tipo: {}, posições: {}", 
                request.getGuessType(), request.getUserGuess().size());
        
        CalculatorResponse response = guessService.calculateScore(request);
        return ResponseEntity.ok(response);
    }
} 
