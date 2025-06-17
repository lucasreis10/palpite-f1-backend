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
            
            // Buscar estatísticas gerais completas
            UserStatsResponse.GeneralStats generalStats = buildGeneralStats(userId, season);
            
            // Buscar evolução da pontuação por GP
            List<UserStatsResponse.ScoreEvolution> scoreEvolution = buildScoreEvolution(userId, season);
            
            // Buscar dados de acurácia por tipo de palpite
            List<UserStatsResponse.PositionAccuracy> positionAccuracy = processPositionAccuracy(guessRepository.getUserPositionAccuracy(userId, season));
            
            // Buscar performance por piloto
            List<UserStatsResponse.PilotPerformance> pilotPerformance = processPilotPerformance(guessRepository.getUserPilotPerformance(userId, season));
            
            // Buscar performance por circuito (simplificada)
            List<UserStatsResponse.CircuitTypePerformance> circuitPerformance = buildCircuitPerformance(userId, season);
            
            // Buscar performance por clima (simplificada)
            List<UserStatsResponse.WeatherPerformance> weatherPerformance = buildWeatherPerformance(userId, season);
            
            return UserStatsResponse.builder()
                    .userId(userId)
                    .userName(user.getName())
                    .userEmail(user.getEmail())
                    .season(season)
                    .generalStats(generalStats)
                    .scoreEvolution(scoreEvolution)
                    .positionAccuracy(positionAccuracy)
                    .pilotPerformance(pilotPerformance)
                    .circuitPerformance(circuitPerformance)
                    .weatherPerformance(weatherPerformance)
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
            Long eventsParticipated = toLong(row[1]);
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
            Long eventsParticipated = toLong(row[1]);
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

    private Long toLong(Object value) {
        if (value == null) {
            return 0L;
        }
        if (value instanceof Long) {
            return (Long) value;
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).longValue();
        }
        if (value instanceof Double) {
            return ((Double) value).longValue();
        }
        if (value instanceof Integer) {
            return ((Integer) value).longValue();
        }
        return 0L;
    }

    private Integer toInteger(Object value) {
        if (value == null) {
            return 0;
        }
        if (value instanceof Integer) {
            return (Integer) value;
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).intValue();
        }
        if (value instanceof Long) {
            return ((Long) value).intValue();
        }
        if (value instanceof Double) {
            return ((Double) value).intValue();
        }
        return 0;
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
            String guessTypeStr = (String) row[0]; // Agora vem como String da query nativa
            GuessType guessType = GuessType.valueOf(guessTypeStr);
            Long totalGuesses = toLong(row[1]);
            BigDecimal averagePoints = toBigDecimal(row[2]);
            Long correctGuesses = toLong(row[3]); // Agora temos os acertos reais
            
            // Calcular taxa de acerto
            BigDecimal accuracy = BigDecimal.ZERO;
            if (totalGuesses > 0) {
                accuracy = BigDecimal.valueOf(correctGuesses)
                    .divide(BigDecimal.valueOf(totalGuesses), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            }
            
            positionAccuracy.add(UserStatsResponse.PositionAccuracy.builder()
                .position(guessType == GuessType.QUALIFYING ? 1 : 2) // 1 para quali, 2 para race
                .positionName(guessType == GuessType.QUALIFYING ? "Qualifying" : "Race")
                .guessType(guessType.toString())
                .totalGuesses(totalGuesses.intValue())
                .correctGuesses(correctGuesses.intValue()) // Agora com dados reais
                .accuracy(accuracy.setScale(2, RoundingMode.HALF_UP)) // Taxa de acerto calculada
                .averagePoints(averagePoints.setScale(3, RoundingMode.HALF_UP))
                .build());
        }
        
        return positionAccuracy;
    }
    
    private List<UserStatsResponse.PilotPerformance> processPilotPerformance(List<Object[]> pilotData) {
        List<UserStatsResponse.PilotPerformance> pilotPerformance = new ArrayList<>();
        
        for (Object[] row : pilotData) {
            Long pilotId = toLong(row[0]);
            String givenName = (String) row[1];
            String familyName = (String) row[2];
            String teamName = (String) row[3];
            Long timesGuessed = toLong(row[4]);
            BigDecimal averagePoints = toBigDecimal(row[5]);
            Long correctGuesses = toLong(row[6]);
            Integer bestPosition = toInteger(row[7]);
            Integer worstPosition = toInteger(row[8]);
            
            String fullName = givenName + " " + familyName;
            
            // Calcular porcentagem de acerto
            BigDecimal accuracy = BigDecimal.ZERO;
            if (timesGuessed > 0) {
                accuracy = BigDecimal.valueOf(correctGuesses)
                    .divide(BigDecimal.valueOf(timesGuessed), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            }
            
            pilotPerformance.add(UserStatsResponse.PilotPerformance.builder()
                .pilotId(pilotId)
                .pilotName(fullName)
                .teamName(teamName)
                .timesGuessed(timesGuessed.intValue())
                .correctGuesses(correctGuesses.intValue())
                .accuracy(accuracy.setScale(2, RoundingMode.HALF_UP))
                .averagePoints(averagePoints.setScale(3, RoundingMode.HALF_UP))
                .bestPosition(bestPosition + 1) // +1 porque position é 0-based na tabela
                .worstPosition(worstPosition + 1) // +1 porque position é 0-based na tabela
                .build());
        }
        
        return pilotPerformance;
    }
} 