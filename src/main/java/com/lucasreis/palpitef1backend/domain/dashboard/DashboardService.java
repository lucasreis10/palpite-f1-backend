package com.lucasreis.palpitef1backend.domain.dashboard;

import com.lucasreis.palpitef1backend.domain.grandprix.GrandPrix;
import com.lucasreis.palpitef1backend.domain.grandprix.GrandPrixRepository;
import com.lucasreis.palpitef1backend.domain.guess.GuessRepository;
import com.lucasreis.palpitef1backend.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardService {
    
    private final GuessRepository guessRepository;
    private final UserRepository userRepository;
    private final GrandPrixRepository grandPrixRepository;
    
    public DashboardStatsResponse getDashboardStats() {
        log.debug("Calculando estatísticas da dashboard");
        
        // Total de palpites
        long totalGuesses = guessRepository.count();
        
        // Total de usuários
        long totalUsers = userRepository.count();
        
        // Total de corridas (GPs ativos)
        long totalRaces = grandPrixRepository.countByActiveTrue();
        
        // Média de palpites por corrida
        BigDecimal averageGuessesPerRace = totalRaces > 0 ? 
            BigDecimal.valueOf(totalGuesses).divide(BigDecimal.valueOf(totalRaces), 2, RoundingMode.HALF_UP) : 
            BigDecimal.ZERO;
        
        // Melhor pontuação (implementação simplificada)
        // TODO: Implementar busca da melhor pontuação real
        BestScoreInfo bestScore = new BestScoreInfo(
            BigDecimal.valueOf(32.5), 
            "João Silva", 
            "GP da Austrália"
        );
        
        return new DashboardStatsResponse(
            bestScore,
            totalGuesses,
            averageGuessesPerRace,
            totalUsers,
            totalRaces
        );
    }
    
    public List<TopUserResponse> getTopUsers(Integer limit, Integer season) {
        log.debug("Buscando top {} usuários da temporada {}", limit, season);
        
        // TODO: Implementar ranking real baseado nos palpites
        // Por enquanto, retornando dados mockados
        return List.of(
            new TopUserResponse(1L, "João Silva", "joao@email.com", BigDecimal.valueOf(245.5), 1),
            new TopUserResponse(2L, "Maria Santos", "maria@email.com", BigDecimal.valueOf(220.0), 2),
            new TopUserResponse(3L, "Pedro Oliveira", "pedro@email.com", BigDecimal.valueOf(198.5), 3),
            new TopUserResponse(4L, "Ana Costa", "ana@email.com", BigDecimal.valueOf(187.0), 4),
            new TopUserResponse(5L, "Lucas Ferreira", "lucas@email.com", BigDecimal.valueOf(156.5), 5)
        ).stream().limit(limit).collect(Collectors.toList());
    }
    
    public LastResultResponse getLastResult() {
        log.debug("Buscando último resultado");
        
        // Buscar o GP mais recente que foi concluído
        List<GrandPrix> recentCompleted = grandPrixRepository.findByCompletedTrueOrderByRaceDateTimeDesc();
        
        if (recentCompleted.isEmpty()) {
            return new LastResultResponse("Nenhum resultado disponível", List.of(), List.of());
        }
        
        GrandPrix lastGP = recentCompleted.get(0);
        
        // TODO: Implementar busca dos resultados reais do GP
        // Por enquanto, retornando dados mockados
        List<PilotResultInfo> qualifyingResults = List.of(
            new PilotResultInfo(1, "Max Verstappen", "Red Bull Racing"),
            new PilotResultInfo(2, "Charles Leclerc", "Ferrari"),
            new PilotResultInfo(3, "Lewis Hamilton", "Mercedes")
        );
        
        List<PilotResultInfo> raceResults = List.of(
            new PilotResultInfo(1, "Max Verstappen", "Red Bull Racing"),
            new PilotResultInfo(2, "Sergio Pérez", "Red Bull Racing"),
            new PilotResultInfo(3, "Carlos Sainz", "Ferrari")
        );
        
        return new LastResultResponse(lastGP.getName(), qualifyingResults, raceResults);
    }
} 