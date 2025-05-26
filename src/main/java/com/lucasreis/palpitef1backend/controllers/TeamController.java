package com.lucasreis.palpitef1backend.controllers;

import com.lucasreis.palpitef1backend.domain.team.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/teams")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class TeamController {
    
    private final TeamService teamService;
    
    @GetMapping
    public ResponseEntity<List<TeamResponse>> getAllTeams() {
        log.debug("Requisição para listar todas as equipes");
        List<TeamResponse> teams = teamService.getAllTeams();
        return ResponseEntity.ok(teams);
    }
    
    @GetMapping("/year/{year}")
    public ResponseEntity<List<TeamResponse>> getTeamsByYear(@PathVariable Integer year) {
        log.debug("Requisição para buscar equipes do ano: {}", year);
        List<TeamResponse> teams = teamService.getTeamsByYear(year);
        return ResponseEntity.ok(teams);
    }
    
    @GetMapping("/year/{year}/active")
    public ResponseEntity<List<TeamResponse>> getActiveTeamsByYear(@PathVariable Integer year) {
        log.debug("Requisição para buscar equipes ativas do ano: {}", year);
        List<TeamResponse> teams = teamService.getActiveTeamsByYear(year);
        return ResponseEntity.ok(teams);
    }
    
    @GetMapping("/year/{year}/ranking")
    public ResponseEntity<List<TeamResponse>> getRankingByYear(@PathVariable Integer year) {
        log.debug("Requisição para buscar ranking de equipes do ano: {}", year);
        List<TeamResponse> teams = teamService.getRankingByYear(year);
        return ResponseEntity.ok(teams);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TeamResponse> getTeamById(@PathVariable Long id) {
        log.debug("Requisição para buscar equipe por ID: {}", id);
        TeamResponse team = teamService.getTeamById(id);
        return ResponseEntity.ok(team);
    }
    
    @GetMapping("/name/{name}/year/{year}")
    public ResponseEntity<TeamResponse> getTeamByNameAndYear(
            @PathVariable String name, 
            @PathVariable Integer year) {
        log.debug("Requisição para buscar equipe por nome '{}' e ano {}", name, year);
        TeamResponse team = teamService.getTeamByNameAndYear(name, year);
        return ResponseEntity.ok(team);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TeamResponse>> getUserTeams(@PathVariable Long userId) {
        log.debug("Requisição para buscar todas as equipes do usuário: {}", userId);
        List<TeamResponse> teams = teamService.getUserTeams(userId);
        return ResponseEntity.ok(teams);
    }
    
    @GetMapping("/user/{userId}/year/{year}")
    public ResponseEntity<TeamResponse> getUserTeamByYear(
            @PathVariable Long userId, 
            @PathVariable Integer year) {
        log.debug("Requisição para buscar equipe do usuário {} no ano {}", userId, year);
        TeamResponse team = teamService.getUserTeamByYear(userId, year);
        return ResponseEntity.ok(team);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<TeamResponse>> searchTeamsByName(@RequestParam String name) {
        log.debug("Requisição para buscar equipes por nome: {}", name);
        List<TeamResponse> teams = teamService.searchTeamsByName(name);
        return ResponseEntity.ok(teams);
    }
    
    @PostMapping
    public ResponseEntity<TeamResponse> createTeam(@RequestBody @Valid CreateTeamRequest request) {
        log.debug("Requisição para criar nova equipe: {}", request.getName());
        TeamResponse team = teamService.createTeam(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(team);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TeamResponse> updateTeam(
            @PathVariable Long id,
            @RequestBody @Valid UpdateTeamRequest request) {
        log.debug("Requisição para atualizar equipe ID: {}", id);
        TeamResponse team = teamService.updateTeam(id, request);
        return ResponseEntity.ok(team);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        log.debug("Requisição para deletar equipe ID: {}", id);
        teamService.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }
} 