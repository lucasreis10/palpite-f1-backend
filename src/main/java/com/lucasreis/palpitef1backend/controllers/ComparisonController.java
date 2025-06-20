package com.lucasreis.palpitef1backend.controllers;

import com.lucasreis.palpitef1backend.domain.comparison.ComparisonService;
import com.lucasreis.palpitef1backend.domain.comparison.HeadToHeadResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/comparison")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class ComparisonController {
    
    private final ComparisonService comparisonService;
    
    /**
     * Endpoint para comparação head-to-head entre dois usuários
     * 
     * @param user1Id ID do primeiro usuário
     * @param user2Id ID do segundo usuário
     * @param season Temporada para comparação (opcional, padrão 2025)
     * @return Comparação detalhada entre os usuários
     */
    @GetMapping("/head-to-head")
    public ResponseEntity<HeadToHeadResponse> compareUsers(
            @RequestParam Long user1Id,
            @RequestParam Long user2Id,
            @RequestParam(defaultValue = "2025") Integer season) {
        
        log.debug("Requisição para comparação head-to-head entre usuários {} e {} na temporada {}", 
                user1Id, user2Id, season);
        
        try {
            HeadToHeadResponse comparison = comparisonService.compareUsers(user1Id, user2Id, season);
            return ResponseEntity.ok(comparison);
        } catch (IllegalArgumentException e) {
            log.error("Erro de validação na comparação: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Erro interno na comparação entre usuários {} e {}: {}", user1Id, user2Id, e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Endpoint de teste para verificar se o controller está funcionando
     */
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("ComparisonController está funcionando!");
    }
} 