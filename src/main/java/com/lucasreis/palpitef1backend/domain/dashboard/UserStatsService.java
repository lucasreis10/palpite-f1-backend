package com.lucasreis.palpitef1backend.domain.dashboard;

import com.lucasreis.palpitef1backend.domain.guess.GuessRepository;
import com.lucasreis.palpitef1backend.domain.guess.GuessType;
import com.lucasreis.palpitef1backend.domain.user.User;
import com.lucasreis.palpitef1backend.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserStatsService {
    
    private final GuessRepository guessRepository;
    private final UserRepository userRepository;
    
    public UserStatsResponse getUserAdvancedStats(Long userId, Integer season) {
        log.debug("Calculando estatísticas avançadas para usuário {} na temporada {}", userId, season);
        
        try {
            // Buscar dados do usuário
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                throw new RuntimeException("Usuário não encontrado");
            }
            
            User user = userOpt.get();
            
            // Buscar estatísticas gerais - por enquanto simplificadas
            UserStatsResponse.GeneralStats generalStats = buildGeneralStatsSimple(userId, season);
            
            // Por enquanto, retornar apenas as estatísticas básicas
            return UserStatsResponse.builder()
                    .userId(userId)
                    .userName(user.getName())
                    .userEmail(user.getEmail())
                    .season(season)
                    .generalStats(generalStats)
                    .scoreEvolution(new ArrayList<>())
                    .positionAccuracy(new ArrayList<>())
                    .pilotPerformance(new ArrayList<>())
                    .circuitPerformance(new ArrayList<>())
                    .weatherPerformance(new ArrayList<>())
                    .build();
        } catch (Exception e) {
            log.error("Erro ao calcular estatísticas avançadas para usuário {} na temporada {}: {}", userId, season, e.getMessage(), e);
            throw new RuntimeException("Erro ao calcular estatísticas do usuário: " + e.getMessage());
        }
    }
    
    private UserStatsResponse.GeneralStats buildGeneralStatsSimple(Long userId, Integer season) {
        try {
            // Contar palpites do usuário
            long totalGuesses = guessRepository.countDistinctUsersBySeasonAndCalculated(season, true);
            
            // Calcular pontuação total básica
            List<Object[]> scoreData = guessRepository.getUserGeneralStats(userId, season);
            BigDecimal totalScore = BigDecimal.ZERO;
            BigDecimal averageScore = BigDecimal.ZERO;
            
            if (!scoreData.isEmpty()) {
                Object[] row = scoreData.get(0);
                totalScore = toBigDecimal(row[0]);
                averageScore = toBigDecimal(row[1]);
            }
            
            return UserStatsResponse.GeneralStats.builder()
                    .totalScore(totalScore.setScale(2, RoundingMode.HALF_UP))
                    .averageScore(averageScore.setScale(2, RoundingMode.HALF_UP))
                    .bestEventScore(BigDecimal.ZERO)
                    .bestEventName("N/A")
                    .totalGuesses((int) totalGuesses)
                    .eventsParticipated(0)
                    .currentRanking(0)
                    .totalParticipants(0)
                    .qualifyingScore(BigDecimal.ZERO)
                    .raceScore(BigDecimal.ZERO)
                    .qualifyingAverage(BigDecimal.ZERO)
                    .raceAverage(BigDecimal.ZERO)
                    .build();
        } catch (Exception e) {
            log.error("Erro ao calcular estatísticas gerais simples: {}", e.getMessage());
            // Retornar dados vazios em caso de erro
            return UserStatsResponse.GeneralStats.builder()
                    .totalScore(BigDecimal.ZERO)
                    .averageScore(BigDecimal.ZERO)
                    .bestEventScore(BigDecimal.ZERO)
                    .bestEventName("N/A")
                    .totalGuesses(0)
                    .eventsParticipated(0)
                    .currentRanking(0)
                    .totalParticipants(0)
                    .qualifyingScore(BigDecimal.ZERO)
                    .raceScore(BigDecimal.ZERO)
                    .qualifyingAverage(BigDecimal.ZERO)
                    .raceAverage(BigDecimal.ZERO)
                    .build();
        }
    }
    
    private UserStatsResponse.GeneralStats buildGeneralStats(Long userId, Integer season) {
        List<Object[]> statsData = guessRepository.getUserGeneralStats(userId, season);
        
        if (statsData.isEmpty()) {
            return UserStatsResponse.GeneralStats.builder()
                    .totalScore(BigDecimal.ZERO)
                    .averageScore(BigDecimal.ZERO)
                    .bestEventScore(BigDecimal.ZERO)
                    .bestEventName("Nenhum")
                    .totalGuesses(0)
                    .eventsParticipated(0)
                    .currentRanking(0)
                    .totalParticipants(0)
                    .qualifyingScore(BigDecimal.ZERO)
                    .raceScore(BigDecimal.ZERO)
                    .qualifyingAverage(BigDecimal.ZERO)
                    .raceAverage(BigDecimal.ZERO)
                    .build();
        }
        
        Object[] row = statsData.get(0);
        BigDecimal totalScore = toBigDecimal(row[0]);
        BigDecimal averageScore = toBigDecimal(row[1]);
        BigDecimal bestEventScore = toBigDecimal(row[2]);
        Long totalGuesses = (Long) row[3];
        Long eventsParticipated = (Long) row[4];
        BigDecimal qualifyingScore = toBigDecimal(row[5]);
        BigDecimal raceScore = toBigDecimal(row[6]);
        BigDecimal qualifyingAverage = toBigDecimal(row[7]);
        BigDecimal raceAverage = toBigDecimal(row[8]);
        
        // Buscar melhor evento
        List<String> bestEventNames = guessRepository.getUserBestEventName(userId, season);
        String bestEventName = bestEventNames.isEmpty() ? "Nenhum" : bestEventNames.get(0);
        
        // Buscar posição atual no ranking
        List<Object[]> rankingData = guessRepository.getUserCurrentRanking(userId, season);
        Integer currentRanking = rankingData.isEmpty() ? 0 : ((Long) rankingData.get(0)[0]).intValue();
        
        // Buscar total de participantes (aproximado)
        long totalParticipants = guessRepository.countDistinctUsersBySeasonAndCalculated(season, true);
        
        return UserStatsResponse.GeneralStats.builder()
                .totalScore(totalScore.setScale(2, RoundingMode.HALF_UP))
                .averageScore(averageScore.setScale(2, RoundingMode.HALF_UP))
                .bestEventScore(bestEventScore.setScale(2, RoundingMode.HALF_UP))
                .bestEventName(bestEventName)
                .totalGuesses(totalGuesses.intValue())
                .eventsParticipated(eventsParticipated.intValue())
                .currentRanking(currentRanking)
                .totalParticipants((int) totalParticipants)
                .qualifyingScore(qualifyingScore.setScale(2, RoundingMode.HALF_UP))
                .raceScore(raceScore.setScale(2, RoundingMode.HALF_UP))
                .qualifyingAverage(qualifyingAverage.setScale(2, RoundingMode.HALF_UP))
                .raceAverage(raceAverage.setScale(2, RoundingMode.HALF_UP))
                .build();
    }
    
    private List<UserStatsResponse.ScoreEvolution> buildScoreEvolution(Long userId, Integer season) {
        List<Object[]> evolutionData = guessRepository.getUserScoreEvolution(userId, season);
        List<UserStatsResponse.ScoreEvolution> evolution = new ArrayList<>();
        
        BigDecimal cumulativeScore = BigDecimal.ZERO;
        
        for (Object[] row : evolutionData) {
            Long grandPrixId = (Long) row[0];
            String grandPrixName = (String) row[1];
            String country = (String) row[2];
            Integer round = (Integer) row[3];
            BigDecimal qualifyingScore = toBigDecimal(row[4]);
            BigDecimal raceScore = toBigDecimal(row[5]);
            BigDecimal totalScore = toBigDecimal(row[6]);
            Boolean hasQualifyingGuess = (Boolean) row[7];
            Boolean hasRaceGuess = (Boolean) row[8];
            
            cumulativeScore = cumulativeScore.add(totalScore);
            
            evolution.add(UserStatsResponse.ScoreEvolution.builder()
                    .grandPrixId(grandPrixId)
                    .grandPrixName(grandPrixName)
                    .country(country)
                    .round(round)
                    .qualifyingScore(qualifyingScore.setScale(2, RoundingMode.HALF_UP))
                    .raceScore(raceScore.setScale(2, RoundingMode.HALF_UP))
                    .totalScore(totalScore.setScale(2, RoundingMode.HALF_UP))
                    .cumulativeScore(cumulativeScore.setScale(2, RoundingMode.HALF_UP))
                    .position(null) // TODO: calcular posição no ranking após cada GP
                    .hasQualifyingGuess(hasQualifyingGuess)
                    .hasRaceGuess(hasRaceGuess)
                    .build());
        }
        
        return evolution;
    }
    
    // Método buildPositionAccuracy removido - usando processPositionAccuracy
    
    // Método buildPilotPerformance removido - usando processPilotPerformance
    
    private List<UserStatsResponse.CircuitTypePerformance> buildCircuitPerformance(Long userId, Integer season) {
        List<Object[]> performanceData = guessRepository.getUserCircuitTypePerformance(userId, season);
        Map<String, Object[]> bestEventMap = guessRepository.getUserBestEventByCircuitType(userId, season)
                .stream()
                .collect(Collectors.toMap(
                    row -> (String) row[0],
                    row -> row,
                    (existing, replacement) -> existing
                ));
        
        List<UserStatsResponse.CircuitTypePerformance> performance = new ArrayList<>();
        
        for (Object[] row : performanceData) {
            String circuitType = (String) row[0];
            Long eventsParticipated = (Long) row[1];
            BigDecimal averageScore = toBigDecimal(row[2]);
            BigDecimal bestScore = toBigDecimal(row[3]);
            BigDecimal qualifyingAverage = toBigDecimal(row[4]);
            BigDecimal raceAverage = toBigDecimal(row[5]);
            
            String bestEvent = "Nenhum";
            if (bestEventMap.containsKey(circuitType)) {
                bestEvent = (String) bestEventMap.get(circuitType)[1];
            }
            
            performance.add(UserStatsResponse.CircuitTypePerformance.builder()
                    .circuitType(circuitType)
                    .eventsParticipated(eventsParticipated.intValue())
                    .averageScore(averageScore.setScale(2, RoundingMode.HALF_UP))
                    .bestScore(bestScore.setScale(2, RoundingMode.HALF_UP))
                    .bestEvent(bestEvent)
                    .qualifyingAverage(qualifyingAverage.setScale(2, RoundingMode.HALF_UP))
                    .raceAverage(raceAverage.setScale(2, RoundingMode.HALF_UP))
                    .build());
        }
        
        return performance;
    }
    
    private List<UserStatsResponse.WeatherPerformance> buildWeatherPerformance(Long userId, Integer season) {
        List<Object[]> performanceData = guessRepository.getUserWeatherPerformance(userId, season);
        Map<String, Object[]> bestEventMap = guessRepository.getUserBestEventByWeather(userId, season)
                .stream()
                .collect(Collectors.toMap(
                    row -> (String) row[0],
                    row -> row,
                    (existing, replacement) -> existing
                ));
        
        List<UserStatsResponse.WeatherPerformance> performance = new ArrayList<>();
        
        for (Object[] row : performanceData) {
            String weatherCondition = (String) row[0];
            Long eventsParticipated = (Long) row[1];
            BigDecimal averageScore = toBigDecimal(row[2]);
            BigDecimal bestScore = toBigDecimal(row[3]);
            BigDecimal qualifyingAverage = toBigDecimal(row[4]);
            BigDecimal raceAverage = toBigDecimal(row[5]);
            
            String bestEvent = "Nenhum";
            if (bestEventMap.containsKey(weatherCondition)) {
                bestEvent = (String) bestEventMap.get(weatherCondition)[1];
            }
            
            performance.add(UserStatsResponse.WeatherPerformance.builder()
                    .weatherCondition(weatherCondition)
                    .eventsParticipated(eventsParticipated.intValue())
                    .averageScore(averageScore.setScale(2, RoundingMode.HALF_UP))
                    .bestScore(bestScore.setScale(2, RoundingMode.HALF_UP))
                    .bestEvent(bestEvent)
                    .qualifyingAverage(qualifyingAverage.setScale(2, RoundingMode.HALF_UP))
                    .raceAverage(raceAverage.setScale(2, RoundingMode.HALF_UP))
                    .build());
        }
        
        return performance;
    }
    
    private BigDecimal toBigDecimal(Object value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        if (value instanceof Double) {
            return BigDecimal.valueOf((Double) value);
        }
        if (value instanceof Long) {
            return BigDecimal.valueOf((Long) value);
        }
        if (value instanceof Integer) {
            return BigDecimal.valueOf((Integer) value);
        }
        return BigDecimal.ZERO;
    }
    
    private String getPositionName(Integer position, String guessType) {
        if (position == null) return "N/A";
        
        if ("QUALIFYING".equals(guessType) && position == 1) {
            return "Pole Position";
        }
        
        return "P" + position;
    }

    private List<UserStatsResponse.PositionAccuracy> processPositionAccuracy(List<Object[]> positionData) {
        List<UserStatsResponse.PositionAccuracy> positionAccuracy = new ArrayList<>();
        
        for (Object[] row : positionData) {
            GuessType guessType = (GuessType) row[0];
            Long totalGuesses = (Long) row[1];
            BigDecimal averagePoints = row[2] instanceof Double ? 
                BigDecimal.valueOf((Double) row[2]) : (BigDecimal) row[2];
            
            // Como simplificamos a consulta, vamos criar dados agregados por tipo
            positionAccuracy.add(UserStatsResponse.PositionAccuracy.builder()
                .position(guessType == GuessType.QUALIFYING ? 1 : 2) // 1 para quali, 2 para race
                .positionName(guessType == GuessType.QUALIFYING ? "Qualifying" : "Race")
                .guessType(guessType.toString())
                .totalGuesses(totalGuesses.intValue())
                .correctGuesses(0) // Não conseguimos calcular sem a entidade Result
                .accuracy(BigDecimal.ZERO) // Não conseguimos calcular sem a entidade Result
                .averagePoints(averagePoints.setScale(3, RoundingMode.HALF_UP))
                .build());
        }
        
        return positionAccuracy;
    }
    
    private List<UserStatsResponse.PilotPerformance> processPilotPerformance(List<Object[]> pilotData) {
        List<UserStatsResponse.PilotPerformance> pilotPerformance = new ArrayList<>();
        
        for (Object[] row : pilotData) {
            Long pilotId = (Long) row[0];
            String givenName = (String) row[1];
            String familyName = (String) row[2];
            String teamName = (String) row[3];
            Long timesGuessed = (Long) row[4];
            BigDecimal averagePoints = row[5] instanceof Double ? 
                BigDecimal.valueOf((Double) row[5]) : (BigDecimal) row[5];
            
            String fullName = givenName + " " + familyName;
            
            pilotPerformance.add(UserStatsResponse.PilotPerformance.builder()
                .pilotId(pilotId)
                .pilotName(fullName)
                .teamName(teamName)
                .timesGuessed(timesGuessed.intValue())
                .correctGuesses(0) // Não conseguimos calcular sem a entidade Result
                .accuracy(BigDecimal.ZERO) // Não conseguimos calcular sem a entidade Result
                .averagePoints(averagePoints.setScale(3, RoundingMode.HALF_UP))
                .bestPosition(0) // Não conseguimos calcular sem a entidade Result
                .worstPosition(0) // Não conseguimos calcular sem a entidade Result
                .build());
        }
        
        return pilotPerformance;
    }
} 