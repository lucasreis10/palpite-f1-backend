package com.lucasreis.palpitef1backend.domain.team;

import com.lucasreis.palpitef1backend.domain.user.User;
import com.lucasreis.palpitef1backend.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamService {
    
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    
    public List<TeamResponse> getAllTeams() {
        log.debug("Buscando todas as equipes");
        return teamRepository.findAll()
                .stream()
                .map(TeamResponse::fromTeam)
                .collect(Collectors.toList());
    }
    
    public List<TeamResponse> getTeamsByYear(Integer year) {
        log.debug("Buscando equipes do ano: {}", year);
        return teamRepository.findByYearOrderByTotalScoreDesc(year)
                .stream()
                .map(TeamResponse::fromTeam)
                .collect(Collectors.toList());
    }
    
    public List<TeamResponse> getActiveTeamsByYear(Integer year) {
        log.debug("Buscando equipes ativas do ano: {}", year);
        return teamRepository.findByYearAndActiveOrderByTotalScoreDesc(year, true)
                .stream()
                .map(TeamResponse::fromTeam)
                .collect(Collectors.toList());
    }
    
    public TeamResponse getTeamById(Long id) {
        log.debug("Buscando equipe por ID: {}", id);
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipe não encontrada com ID: " + id));
        return TeamResponse.fromTeam(team);
    }
    
    public TeamResponse getTeamByNameAndYear(String name, Integer year) {
        log.debug("Buscando equipe por nome '{}' e ano {}", name, year);
        Team team = teamRepository.findByNameAndYear(name, year)
                .orElseThrow(() -> new RuntimeException("Equipe não encontrada com nome '" + name + "' no ano " + year));
        return TeamResponse.fromTeam(team);
    }
    
    public TeamResponse getUserTeamByYear(Long userId, Integer year) {
        log.debug("Buscando equipe do usuário {} no ano {}", userId, year);
        Team team = teamRepository.findByUserIdAndYear(userId, year)
                .orElseThrow(() -> new RuntimeException("Usuário não possui equipe no ano " + year));
        return TeamResponse.fromTeam(team);
    }
    
    public List<TeamResponse> getUserTeams(Long userId) {
        log.debug("Buscando todas as equipes do usuário: {}", userId);
        return teamRepository.findByUserId(userId)
                .stream()
                .map(TeamResponse::fromTeam)
                .collect(Collectors.toList());
    }
    
    public List<TeamResponse> searchTeamsByName(String name) {
        log.debug("Buscando equipes por nome: {}", name);
        return teamRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(TeamResponse::fromTeam)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public TeamResponse createTeam(CreateTeamRequest request) {
        log.debug("Criando nova equipe: {}", request.getName());
        
        // Validações
        validateCreateTeamRequest(request);
        
        // Buscar usuários
        User user1 = userRepository.findById(request.getUser1Id())
                .orElseThrow(() -> new RuntimeException("Usuário 1 não encontrado com ID: " + request.getUser1Id()));
        
        User user2 = userRepository.findById(request.getUser2Id())
                .orElseThrow(() -> new RuntimeException("Usuário 2 não encontrado com ID: " + request.getUser2Id()));
        
        // Criar equipe
        Team team = Team.builder()
                .name(request.getName())
                .year(request.getYear())
                .user1(user1)
                .user2(user2)
                .totalScore(0)
                .active(true)
                .build();
        
        Team savedTeam = teamRepository.save(team);
        log.debug("Equipe criada com sucesso: {}", savedTeam.getId());
        
        return TeamResponse.fromTeam(savedTeam);
    }
    
    @Transactional
    public TeamResponse updateTeam(Long id, UpdateTeamRequest request) {
        log.debug("Atualizando equipe ID: {}", id);
        
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipe não encontrada com ID: " + id));
        
        // Atualizar campos se fornecidos
        if (request.getName() != null) {
            // Verificar se o novo nome já existe no mesmo ano
            if (!team.getName().equals(request.getName()) && 
                teamRepository.existsByNameAndYear(request.getName(), team.getYear())) {
                throw new RuntimeException("Já existe uma equipe com o nome '" + request.getName() + "' no ano " + team.getYear());
            }
            team.setName(request.getName());
        }
        
        if (request.getTotalScore() != null) {
            team.setTotalScore(request.getTotalScore());
        }
        
        if (request.getActive() != null) {
            team.setActive(request.getActive());
        }
        
        Team updatedTeam = teamRepository.save(team);
        log.debug("Equipe atualizada com sucesso: {}", updatedTeam.getId());
        
        return TeamResponse.fromTeam(updatedTeam);
    }
    
    @Transactional
    public void deleteTeam(Long id) {
        log.debug("Deletando equipe ID: {}", id);
        
        if (!teamRepository.existsById(id)) {
            throw new RuntimeException("Equipe não encontrada com ID: " + id);
        }
        
        teamRepository.deleteById(id);
        log.debug("Equipe deletada com sucesso: {}", id);
    }
    
    public List<TeamResponse> getRankingByYear(Integer year) {
        log.debug("Buscando ranking de equipes do ano: {}", year);
        return teamRepository.findTopTeamsByYear(year)
                .stream()
                .map(TeamResponse::fromTeam)
                .collect(Collectors.toList());
    }
    
    private void validateCreateTeamRequest(CreateTeamRequest request) {
        // Verificar se os usuários são diferentes
        if (request.getUser1Id().equals(request.getUser2Id())) {
            throw new RuntimeException("Uma equipe deve ter dois usuários diferentes");
        }
        
        // Verificar se já existe equipe com o nome no ano
        if (teamRepository.existsByNameAndYear(request.getName(), request.getYear())) {
            throw new RuntimeException("Já existe uma equipe com o nome '" + request.getName() + "' no ano " + request.getYear());
        }
        
        // Verificar se os usuários já estão em equipes no ano
        if (teamRepository.existsByUserIdAndYear(request.getUser1Id(), request.getYear())) {
            throw new RuntimeException("Usuário 1 já está em uma equipe no ano " + request.getYear());
        }
        
        if (teamRepository.existsByUserIdAndYear(request.getUser2Id(), request.getYear())) {
            throw new RuntimeException("Usuário 2 já está em uma equipe no ano " + request.getYear());
        }
    }
} 