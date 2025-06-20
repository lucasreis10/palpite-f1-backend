package com.lucasreis.palpitef1backend.domain.comparison;

import com.lucasreis.palpitef1backend.domain.guess.GuessRepository;
import com.lucasreis.palpitef1backend.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ComparisonService {
    
    private final GuessRepository guessRepository;
    private final UserRepository userRepository;
    
    public HeadToHeadResponse compareUsers(Long userId1, Long userId2, Integer season) {
        log.debug("Comparando usuários {} e {} para temporada {}", userId1, userId2, season);
        
        // Buscar dados básicos dos usuários
        var user1 = userRepository.findById(userId1)
            .orElseThrow(() -> new IllegalArgumentException("Usuário " + userId1 + " não encontrado"));
        var user2 = userRepository.findById(userId2)
            .orElseThrow(() -> new IllegalArgumentException("Usuário " + userId2 + " não encontrado"));
        
        // Buscar estatísticas básicas
        UserComparisonStats stats1 = getUserStats(userId1, season);
        UserComparisonStats stats2 = getUserStats(userId2, season);
        
        // Calcular confrontos diretos por GP
        List<HeadToHeadRaceComparison> raceComparisons = getHeadToHeadRaceComparisons(userId1, userId2, season);
        
        // Calcular vencedor geral
        long user1Wins = raceComparisons.stream().mapToLong(r -> r.getWinner().equals(userId1) ? 1 : 0).sum();
        long user2Wins = raceComparisons.stream().mapToLong(r -> r.getWinner().equals(userId2) ? 1 : 0).sum();
        long ties = raceComparisons.stream().mapToLong(r -> r.getWinner() == null ? 1 : 0).sum();
        
        // Análise de performance por categoria
        PerformanceComparison performanceComparison = getPerformanceComparison(userId1, userId2, season);
        
        return HeadToHeadResponse.builder()
            .user1Name(user1.getName())
            .user2Name(user2.getName())
            .user1Stats(stats1)
            .user2Stats(stats2)
            .user1Wins((int) user1Wins)
            .user2Wins((int) user2Wins)
            .ties((int) ties)
            .totalRaces(raceComparisons.size())
            .raceComparisons(raceComparisons)
            .performanceComparison(performanceComparison)
            .build();
    }
    
    private UserComparisonStats getUserStats(Long userId, Integer season) {
        // Buscar estatísticas básicas do usuário
        List<Object[]> basicStats = guessRepository.getUserBasicStats(userId, season);
        
        if (basicStats.isEmpty()) {
            return UserComparisonStats.builder()
                .totalGuesses(0)
                .totalPoints(BigDecimal.ZERO)
                .averagePoints(BigDecimal.ZERO)
                .bestRaceScore(BigDecimal.ZERO)
                .correctPredictions(0)
                .accuracyRate(BigDecimal.ZERO)
                .build();
        }
        
        Object[] stats = basicStats.get(0);
        Long totalGuesses = (Long) stats[0];
        BigDecimal totalPoints = (BigDecimal) stats[1];
        
        // Verificar se há palpites para evitar divisão por zero
        BigDecimal averagePoints = totalGuesses > 0 ? 
            totalPoints.divide(BigDecimal.valueOf(totalGuesses), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
        
        // Buscar melhor pontuação em uma corrida
        BigDecimal bestRaceScore = guessRepository.getUserBestRaceScore(userId, season);
        
        // Buscar palpites corretos (simplificado - onde score > 0)
        Long correctPredictions = guessRepository.countCorrectPredictions(userId, season);
        
        BigDecimal accuracyRate = totalGuesses > 0 ? 
            BigDecimal.valueOf(correctPredictions)
                .divide(BigDecimal.valueOf(totalGuesses), 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100)) : BigDecimal.ZERO;
        
        return UserComparisonStats.builder()
            .totalGuesses(totalGuesses.intValue())
            .totalPoints(totalPoints)
            .averagePoints(averagePoints)
            .bestRaceScore(bestRaceScore != null ? bestRaceScore : BigDecimal.ZERO)
            .correctPredictions(correctPredictions.intValue())
            .accuracyRate(accuracyRate)
            .build();
    }
    
    private List<HeadToHeadRaceComparison> getHeadToHeadRaceComparisons(Long userId1, Long userId2, Integer season) {
        // Buscar comparações por GP
        List<Object[]> comparisons = guessRepository.getHeadToHeadRaceComparisons(userId1, userId2, season);
        
        return comparisons.stream().map(row -> {
            Long grandPrixId = (Long) row[0];
            String grandPrixName = (String) row[1];
            java.time.LocalDateTime raceDate = row[2] instanceof java.time.LocalDateTime ? 
                (java.time.LocalDateTime) row[2] : null;
            BigDecimal user1Score = (BigDecimal) row[3];
            BigDecimal user2Score = (BigDecimal) row[4];
            
            Long winner = null;
            if (user1Score.compareTo(user2Score) > 0) {
                winner = userId1;
            } else if (user2Score.compareTo(user1Score) > 0) {
                winner = userId2;
            }
            
            return HeadToHeadRaceComparison.builder()
                .grandPrixId(grandPrixId)
                .grandPrixName(grandPrixName)
                .raceDate(raceDate)
                .user1Score(user1Score)
                .user2Score(user2Score)
                .winner(winner)
                .scoreDifference(user1Score.subtract(user2Score).abs())
                .build();
        }).toList();
    }
    
    private PerformanceComparison getPerformanceComparison(Long userId1, Long userId2, Integer season) {
        // Comparar performance em diferentes categorias
        
        // Qualifying vs Race performance
        BigDecimal user1QualifyingAvg = guessRepository.getUserQualifyingAverage(userId1, season);
        BigDecimal user1RaceAvg = guessRepository.getUserRaceAverage(userId1, season);
        BigDecimal user2QualifyingAvg = guessRepository.getUserQualifyingAverage(userId2, season);
        BigDecimal user2RaceAvg = guessRepository.getUserRaceAverage(userId2, season);
        
        // Consistência (desvio padrão das pontuações)
        BigDecimal user1Consistency = guessRepository.getUserConsistency(userId1, season);
        BigDecimal user2Consistency = guessRepository.getUserConsistency(userId2, season);
        
        // Melhor e pior performance
        BigDecimal user1Best = guessRepository.getUserBestRaceScore(userId1, season);
        BigDecimal user1Worst = guessRepository.getUserWorstRaceScore(userId1, season);
        BigDecimal user2Best = guessRepository.getUserBestRaceScore(userId2, season);
        BigDecimal user2Worst = guessRepository.getUserWorstRaceScore(userId2, season);
        
        return PerformanceComparison.builder()
            .user1QualifyingAverage(user1QualifyingAvg != null ? user1QualifyingAvg : BigDecimal.ZERO)
            .user1RaceAverage(user1RaceAvg != null ? user1RaceAvg : BigDecimal.ZERO)
            .user2QualifyingAverage(user2QualifyingAvg != null ? user2QualifyingAvg : BigDecimal.ZERO)
            .user2RaceAverage(user2RaceAvg != null ? user2RaceAvg : BigDecimal.ZERO)
            .user1Consistency(user1Consistency != null ? user1Consistency : BigDecimal.ZERO)
            .user2Consistency(user2Consistency != null ? user2Consistency : BigDecimal.ZERO)
            .user1BestScore(user1Best != null ? user1Best : BigDecimal.ZERO)
            .user1WorstScore(user1Worst != null ? user1Worst : BigDecimal.ZERO)
            .user2BestScore(user2Best != null ? user2Best : BigDecimal.ZERO)
            .user2WorstScore(user2Worst != null ? user2Worst : BigDecimal.ZERO)
            .build();
    }
} 