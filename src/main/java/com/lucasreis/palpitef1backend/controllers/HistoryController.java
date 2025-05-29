package com.lucasreis.palpitef1backend.controllers;

import com.lucasreis.palpitef1backend.domain.guess.GrandPrixHistoryResponse;
import com.lucasreis.palpitef1backend.domain.guess.HistoryService;
import com.lucasreis.palpitef1backend.domain.guess.SeasonRankingResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/history")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class HistoryController {
    
    private final HistoryService historyService;
    
    /**
     * Buscar histórico completo de um Grand Prix específico
     * Inclui ranking de qualifying, race, combinado e estatísticas
     */
    @GetMapping("/grand-prix/{grandPrixId}")
    public ResponseEntity<GrandPrixHistoryResponse> getGrandPrixHistory(@PathVariable Long grandPrixId) {
        log.debug("Requisição para buscar histórico do GP {}", grandPrixId);
        
        try {
            GrandPrixHistoryResponse history = historyService.getGrandPrixHistory(grandPrixId);
            return ResponseEntity.ok(history);
        } catch (RuntimeException e) {
            log.error("Erro ao buscar histórico do GP {}: {}", grandPrixId, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Buscar ranking completo de uma temporada
     * Inclui histórico detalhado de cada participante por evento
     */
    @GetMapping("/season/{season}")
    public ResponseEntity<SeasonRankingResponse> getSeasonRanking(@PathVariable Integer season) {
        log.debug("Requisição para buscar ranking da temporada {}", season);
        
        try {
            SeasonRankingResponse ranking = historyService.getSeasonRanking(season);
            return ResponseEntity.ok(ranking);
        } catch (Exception e) {
            log.error("Erro ao buscar ranking da temporada {}: {}", season, e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Buscar ranking atual da temporada (versão simplificada)
     * Útil para exibições rápidas sem histórico detalhado
     */
    @GetMapping("/season/{season}/simple")
    public ResponseEntity<SeasonRankingResponse> getSimpleSeasonRanking(@PathVariable Integer season) {
        log.debug("Requisição para buscar ranking simples da temporada {}", season);
        
        try {
            SeasonRankingResponse ranking = historyService.getSeasonRanking(season);
            
            // Remover histórico detalhado para resposta mais leve
            ranking.getRanking().forEach(participant -> 
                participant.setEventHistory(null)
            );
            
            return ResponseEntity.ok(ranking);
        } catch (Exception e) {
            log.error("Erro ao buscar ranking simples da temporada {}: {}", season, e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Endpoint de teste para verificar se o controller está funcionando
     */
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("HistoryController está funcionando!");
    }
    
    /**
     * Endpoint de teste para verificar se o HistoryService está sendo injetado
     */
    @GetMapping("/test-service")
    public ResponseEntity<String> testService() {
        if (historyService == null) {
            return ResponseEntity.ok("HistoryService é NULL!");
        }
        return ResponseEntity.ok("HistoryService está injetado corretamente!");
    }
} 