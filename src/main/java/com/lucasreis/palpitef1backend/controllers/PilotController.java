package com.lucasreis.palpitef1backend.controllers;

import com.lucasreis.palpitef1backend.domain.pilot.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/pilots")
@RequiredArgsConstructor
public class PilotController {
    
    private final PilotService pilotService;
    
    @GetMapping
    public ResponseEntity<List<PilotResponse>> getAllPilots() {
        log.debug("Requisição para listar todos os pilotos");
        List<PilotResponse> pilots = pilotService.getAllPilots();
        return ResponseEntity.ok(pilots);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PilotResponse> getPilotById(@PathVariable Long id) {
        log.debug("Requisição para buscar piloto por ID: {}", id);
        PilotResponse pilot = pilotService.getPilotById(id);
        return ResponseEntity.ok(pilot);
    }
    
    @GetMapping("/driver/{driverId}")
    public ResponseEntity<PilotResponse> getPilotByDriverId(@PathVariable String driverId) {
        log.debug("Requisição para buscar piloto por driverId: {}", driverId);
        PilotResponse pilot = pilotService.getPilotByDriverId(driverId);
        return ResponseEntity.ok(pilot);
    }
    
    @GetMapping("/nationality/{nationality}")
    public ResponseEntity<List<PilotResponse>> getPilotsByNationality(@PathVariable String nationality) {
        log.debug("Requisição para buscar pilotos por nacionalidade: {}", nationality);
        List<PilotResponse> pilots = pilotService.getPilotsByNationality(nationality);
        return ResponseEntity.ok(pilots);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<PilotResponse>> searchPilotsByName(@RequestParam String name) {
        log.debug("Requisição para buscar pilotos por nome: {}", name);
        List<PilotResponse> pilots = pilotService.searchPilotsByName(name);
        return ResponseEntity.ok(pilots);
    }
    
    @PostMapping
    public ResponseEntity<PilotResponse> createPilot(@RequestBody @Valid CreatePilotRequest request) {
        log.debug("Requisição para criar novo piloto: {}", request.getDriverId());
        PilotResponse pilot = pilotService.createPilot(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(pilot);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<PilotResponse> updatePilot(
            @PathVariable Long id,
            @RequestBody @Valid UpdatePilotRequest request) {
        log.debug("Requisição para atualizar piloto ID: {}", id);
        PilotResponse pilot = pilotService.updatePilot(id, request);
        return ResponseEntity.ok(pilot);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePilot(@PathVariable Long id) {
        log.debug("Requisição para deletar piloto ID: {}", id);
        pilotService.deletePilot(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<PilotResponse>> getAllActivePilots() {
        log.debug("Requisição para listar todos os pilotos ativos");
        List<PilotResponse> pilots = pilotService.getAllActivePilots();
        return ResponseEntity.ok(pilots);
    }
    
    @GetMapping("/status/{active}")
    public ResponseEntity<List<PilotResponse>> getPilotsByStatus(@PathVariable Boolean active) {
        log.debug("Requisição para buscar pilotos por status ativo: {}", active);
        List<PilotResponse> pilots = pilotService.getPilotsByStatus(active);
        return ResponseEntity.ok(pilots);
    }
    
    @GetMapping("/active/nationality/{nationality}")
    public ResponseEntity<List<PilotResponse>> getActivePilotsByNationality(@PathVariable String nationality) {
        log.debug("Requisição para buscar pilotos ativos por nacionalidade: {}", nationality);
        List<PilotResponse> pilots = pilotService.getActivePilotsByNationality(nationality);
        return ResponseEntity.ok(pilots);
    }
    
    @GetMapping("/active/search")
    public ResponseEntity<List<PilotResponse>> searchActivePilotsByName(@RequestParam String name) {
        log.debug("Requisição para buscar pilotos ativos por nome: {}", name);
        List<PilotResponse> pilots = pilotService.searchActivePilotsByName(name);
        return ResponseEntity.ok(pilots);
    }
    
    @PatchMapping("/{id}/activate")
    public ResponseEntity<PilotResponse> activatePilot(@PathVariable Long id) {
        log.debug("Requisição para ativar piloto ID: {}", id);
        PilotResponse pilot = pilotService.activatePilot(id);
        return ResponseEntity.ok(pilot);
    }
    
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<PilotResponse> deactivatePilot(@PathVariable Long id) {
        log.debug("Requisição para inativar piloto ID: {}", id);
        PilotResponse pilot = pilotService.deactivatePilot(id);
        return ResponseEntity.ok(pilot);
    }
    
    @PostMapping("/batch")
    public ResponseEntity<CreatePilotsResponse> createPilots(@RequestBody @Valid CreatePilotsRequest request) {
        log.debug("Requisição para criar {} pilotos em lote", request.getPilots().size());
        CreatePilotsResponse response = pilotService.createPilots(request);
        
        // Se todos os pilotos foram criados com sucesso, retorna 201
        // Se houve alguns erros mas pelo menos um foi criado, retorna 207 (Multi-Status)
        // Se nenhum piloto foi criado, retorna 400 (Bad Request)
        if (response.getTotalErrors() == 0) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else if (response.getTotalCreated() > 0) {
            return ResponseEntity.status(207).body(response); // Multi-Status
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
} 