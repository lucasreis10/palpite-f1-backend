package com.lucasreis.palpitef1backend.controllers;

import com.lucasreis.palpitef1backend.domain.grandprix.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/grand-prix")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class GrandPrixController {
    
    private final GrandPrixService grandPrixService;
    
    @GetMapping
    public ResponseEntity<List<GrandPrixResponse>> getAllGrandPrix() {
        log.debug("Requisição para listar todos os Grandes Prêmios");
        List<GrandPrixResponse> grandPrix = grandPrixService.getAllGrandPrix();
        return ResponseEntity.ok(grandPrix);
    }
    
    @GetMapping("/seasons")
    public ResponseEntity<List<Integer>> getAvailableSeasons() {
        log.debug("Requisição para listar temporadas disponíveis");
        List<Integer> seasons = grandPrixService.getAvailableSeasons();
        return ResponseEntity.ok(seasons);
    }
    
    @GetMapping("/season/{season}")
    public ResponseEntity<List<GrandPrixResponse>> getGrandPrixBySeason(@PathVariable Integer season) {
        log.debug("Requisição para buscar Grandes Prêmios da temporada: {}", season);
        List<GrandPrixResponse> grandPrix = grandPrixService.getGrandPrixBySeason(season);
        return ResponseEntity.ok(grandPrix);
    }
    
    @GetMapping("/season/{season}/active")
    public ResponseEntity<List<GrandPrixResponse>> getActiveGrandPrixBySeason(@PathVariable Integer season) {
        log.debug("Requisição para buscar Grandes Prêmios ativos da temporada: {}", season);
        List<GrandPrixResponse> grandPrix = grandPrixService.getActiveGrandPrixBySeason(season);
        return ResponseEntity.ok(grandPrix);
    }
    
    @GetMapping("/season/{season}/completed")
    public ResponseEntity<List<GrandPrixResponse>> getCompletedGrandPrixBySeason(@PathVariable Integer season) {
        log.debug("Requisição para buscar Grandes Prêmios concluídos da temporada: {}", season);
        List<GrandPrixResponse> grandPrix = grandPrixService.getCompletedGrandPrixBySeason(season);
        return ResponseEntity.ok(grandPrix);
    }
    
    @GetMapping("/season/{season}/pending")
    public ResponseEntity<List<GrandPrixResponse>> getPendingGrandPrixBySeason(@PathVariable Integer season) {
        log.debug("Requisição para buscar Grandes Prêmios pendentes da temporada: {}", season);
        List<GrandPrixResponse> grandPrix = grandPrixService.getPendingGrandPrixBySeason(season);
        return ResponseEntity.ok(grandPrix);
    }
    
    @GetMapping("/season/{season}/sprint")
    public ResponseEntity<List<GrandPrixResponse>> getSprintWeekendsBySeason(@PathVariable Integer season) {
        log.debug("Requisição para buscar fins de semana de sprint da temporada: {}", season);
        List<GrandPrixResponse> grandPrix = grandPrixService.getSprintWeekendsBySeason(season);
        return ResponseEntity.ok(grandPrix);
    }
    
    @GetMapping("/season/{season}/round/{round}")
    public ResponseEntity<GrandPrixResponse> getGrandPrixBySeasonAndRound(
            @PathVariable Integer season, 
            @PathVariable Integer round) {
        log.debug("Requisição para buscar Grande Prêmio da temporada {} rodada {}", season, round);
        GrandPrixResponse grandPrix = grandPrixService.getGrandPrixBySeasonAndRound(season, round);
        return ResponseEntity.ok(grandPrix);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<GrandPrixResponse> getGrandPrixById(@PathVariable Long id) {
        log.debug("Requisição para buscar Grande Prêmio por ID: {}", id);
        GrandPrixResponse grandPrix = grandPrixService.getGrandPrixById(id);
        return ResponseEntity.ok(grandPrix);
    }
    
    @GetMapping("/country/{country}")
    public ResponseEntity<List<GrandPrixResponse>> getGrandPrixByCountry(@PathVariable String country) {
        log.debug("Requisição para buscar Grandes Prêmios do país: {}", country);
        List<GrandPrixResponse> grandPrix = grandPrixService.getGrandPrixByCountry(country);
        return ResponseEntity.ok(grandPrix);
    }
    
    @GetMapping("/upcoming")
    public ResponseEntity<List<GrandPrixResponse>> getUpcomingGrandPrix() {
        log.debug("Requisição para buscar próximos Grandes Prêmios");
        List<GrandPrixResponse> grandPrix = grandPrixService.getUpcomingGrandPrix();
        return ResponseEntity.ok(grandPrix);
    }
    
    @GetMapping("/next")
    public ResponseEntity<GrandPrixResponse> getNextGrandPrix() {
        log.debug("Requisição para buscar próximo Grande Prêmio");
        GrandPrixResponse grandPrix = grandPrixService.getNextGrandPrix();
        return ResponseEntity.ok(grandPrix);
    }
    
    @GetMapping("/recent")
    public ResponseEntity<List<GrandPrixResponse>> getRecentCompletedGrandPrix() {
        log.debug("Requisição para buscar Grandes Prêmios recentemente concluídos");
        List<GrandPrixResponse> grandPrix = grandPrixService.getRecentCompletedGrandPrix();
        return ResponseEntity.ok(grandPrix);
    }
    
    @GetMapping("/search/circuit")
    public ResponseEntity<List<GrandPrixResponse>> searchByCircuitName(@RequestParam String name) {
        log.debug("Requisição para buscar Grandes Prêmios por nome do circuito: {}", name);
        List<GrandPrixResponse> grandPrix = grandPrixService.searchByCircuitName(name);
        return ResponseEntity.ok(grandPrix);
    }
    
    @PostMapping
    public ResponseEntity<GrandPrixResponse> createGrandPrix(@RequestBody @Valid CreateGrandPrixRequest request) {
        log.debug("Requisição para criar novo Grande Prêmio: {}", request.getName());
        GrandPrixResponse grandPrix = grandPrixService.createGrandPrix(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(grandPrix);
    }
    
    @PostMapping("/batch")
    public ResponseEntity<CreateGrandPrixBatchResponse> createGrandPrixBatch(@RequestBody @Valid CreateGrandPrixBatchRequest request) {
        log.debug("Requisição para criar {} Grandes Prêmios em lote", request.getGrandPrix().size());
        CreateGrandPrixBatchResponse response = grandPrixService.createGrandPrixBatch(request);
        
        // Retornar 207 Multi-Status se houve erros parciais, 201 se todos foram criados
        HttpStatus status = response.getTotalErrors() > 0 ? HttpStatus.MULTI_STATUS : HttpStatus.CREATED;
        return ResponseEntity.status(status).body(response);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<GrandPrixResponse> updateGrandPrix(
            @PathVariable Long id,
            @RequestBody @Valid UpdateGrandPrixRequest request) {
        log.debug("Requisição para atualizar Grande Prêmio ID: {}", id);
        GrandPrixResponse grandPrix = grandPrixService.updateGrandPrix(id, request);
        return ResponseEntity.ok(grandPrix);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGrandPrix(@PathVariable Long id) {
        log.debug("Requisição para deletar Grande Prêmio ID: {}", id);
        grandPrixService.deleteGrandPrix(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/complete")
    public ResponseEntity<GrandPrixResponse> markAsCompleted(@PathVariable Long id) {
        log.debug("Requisição para marcar Grande Prêmio como concluído ID: {}", id);
        GrandPrixResponse grandPrix = grandPrixService.markAsCompleted(id);
        return ResponseEntity.ok(grandPrix);
    }
    
    @PatchMapping("/{id}/complete-with-scores")
    public ResponseEntity<CompleteEventResponse> markAsCompletedWithScoreCalculation(@PathVariable Long id) {
        log.debug("Requisição para marcar Grande Prêmio como concluído e calcular pontuações ID: {}", id);
        CompleteEventResponse response = grandPrixService.markAsCompletedWithScoreCalculation(id);
        return ResponseEntity.ok(response);
    }
    
    @PatchMapping("/{id}/pending")
    public ResponseEntity<GrandPrixResponse> markAsPending(@PathVariable Long id) {
        log.debug("Requisição para marcar Grande Prêmio como pendente ID: {}", id);
        GrandPrixResponse grandPrix = grandPrixService.markAsPending(id);
        return ResponseEntity.ok(grandPrix);
    }
} 