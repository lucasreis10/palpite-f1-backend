package com.lucasreis.palpitef1backend.controllers;

import com.lucasreis.palpitef1backend.domain.timeline.TimelineResponse;
import com.lucasreis.palpitef1backend.domain.timeline.TimelineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/timeline")
@RequiredArgsConstructor
@Slf4j
public class TimelineController {
    
    private final TimelineService timelineService;
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TimelineResponse>> getUserTimeline(
            @PathVariable Long userId,
            @RequestParam(required = false) Integer season) {
        
        log.debug("Requisição para buscar timeline do usuário {} para temporada {}", userId, season);
        
        try {
            List<TimelineResponse> timeline = timelineService.getUserTimeline(userId, season)
                .stream()
                .map(TimelineResponse::fromEntity)
                .toList();
            return ResponseEntity.ok(timeline);
        } catch (Exception e) {
            log.error("Erro ao buscar timeline do usuário {}: {}", userId, e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/milestones/{userId}")
    public ResponseEntity<List<TimelineResponse>> getSeasonMilestones(
            @PathVariable Long userId,
            @RequestParam Integer season) {
        
        log.debug("Requisição para buscar marcos da temporada {} do usuário {}", season, userId);
        
        try {
            List<TimelineResponse> milestones = timelineService.getSeasonMilestones(userId, season)
                .stream()
                .map(TimelineResponse::fromEntity)
                .toList();
            return ResponseEntity.ok(milestones);
        } catch (Exception e) {
            log.error("Erro ao buscar marcos da temporada {} do usuário {}: {}", season, userId, e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/recent/{userId}")
    public ResponseEntity<List<TimelineResponse>> getRecentEvents(@PathVariable Long userId) {
        
        log.debug("Requisição para buscar eventos recentes do usuário {}", userId);
        
        try {
            List<TimelineResponse> recentEvents = timelineService.getRecentEvents(userId)
                .stream()
                .map(TimelineResponse::fromEntity)
                .toList();
            return ResponseEntity.ok(recentEvents);
        } catch (Exception e) {
            log.error("Erro ao buscar eventos recentes do usuário {}: {}", userId, e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
} 