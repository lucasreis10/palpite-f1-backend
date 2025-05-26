package com.lucasreis.palpitef1backend.controllers;

import com.lucasreis.palpitef1backend.domain.dashboard.DashboardService;
import com.lucasreis.palpitef1backend.domain.dashboard.DashboardStatsResponse;
import com.lucasreis.palpitef1backend.domain.dashboard.LastResultResponse;
import com.lucasreis.palpitef1backend.domain.dashboard.TopUserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/dashboard")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class DashboardController {
    
    private final DashboardService dashboardService;
    
    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsResponse> getDashboardStats() {
        log.debug("Requisição para buscar estatísticas da dashboard");
        DashboardStatsResponse stats = dashboardService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/top-users")
    public ResponseEntity<List<TopUserResponse>> getTopUsers(
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "2024") Integer season) {
        log.debug("Requisição para buscar top {} usuários da temporada {}", limit, season);
        List<TopUserResponse> topUsers = dashboardService.getTopUsers(limit, season);
        return ResponseEntity.ok(topUsers);
    }
    
    @GetMapping("/last-result")
    public ResponseEntity<LastResultResponse> getLastResult() {
        log.debug("Requisição para buscar último resultado");
        LastResultResponse lastResult = dashboardService.getLastResult();
        return ResponseEntity.ok(lastResult);
    }
} 