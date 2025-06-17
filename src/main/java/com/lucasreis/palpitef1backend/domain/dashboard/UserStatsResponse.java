package com.lucasreis.palpitef1backend.domain.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStatsResponse {
    
    // Dados básicos do usuário
    private Long userId;
    private String userName;
    private String userEmail;
    private Integer season;
    
    // Estatísticas gerais
    private GeneralStats generalStats;
    
    // Evolução da pontuação por GP
    private List<ScoreEvolution> scoreEvolution;
    
    // Taxa de acerto por posição
    private List<PositionAccuracy> positionAccuracy;
    
    // Performance por piloto
    private List<PilotPerformance> pilotPerformance;
    
    // Performance por tipo de circuito
    private List<CircuitTypePerformance> circuitPerformance;
    
    // Performance por clima
    private List<WeatherPerformance> weatherPerformance;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GeneralStats {
        private BigDecimal totalScore;
        private BigDecimal averageScore;
        private BigDecimal bestEventScore;
        private String bestEventName;
        private Integer totalGuesses;
        private Integer eventsParticipated;
        private Integer currentRanking;
        private Integer totalParticipants;
        private BigDecimal qualifyingScore;
        private BigDecimal raceScore;
        private BigDecimal qualifyingAverage;
        private BigDecimal raceAverage;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScoreEvolution {
        private Long grandPrixId;
        private String grandPrixName;
        private String country;
        private Integer round;
        private BigDecimal qualifyingScore;
        private BigDecimal raceScore;
        private BigDecimal totalScore;
        private BigDecimal cumulativeScore;
        private Integer position;
        private Boolean hasQualifyingGuess;
        private Boolean hasRaceGuess;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PositionAccuracy {
        private Integer position; // P1, P2, P3, etc.
        private String positionName; // "Pole Position", "P2", "P3", etc.
        private Integer correctGuesses; // quantas vezes acertou essa posição
        private Integer totalGuesses; // quantas vezes apostou nessa posição
        private BigDecimal accuracy; // taxa de acerto (%)
        private BigDecimal averagePoints; // pontos médios quando aposta nessa posição
        private String guessType; // QUALIFYING ou RACE
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PilotPerformance {
        private Long pilotId;
        private String pilotName;
        private String teamName;
        private Integer timesGuessed; // quantas vezes apostou nesse piloto
        private Integer correctGuesses; // quantas vezes acertou
        private BigDecimal accuracy; // taxa de acerto (%)
        private BigDecimal averagePoints; // pontos médios quando aposta nesse piloto
        private Integer bestPosition; // melhor posição que acertou com esse piloto
        private Integer worstPosition; // pior posição que acertou com esse piloto
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CircuitTypePerformance {
        private String circuitType; // "Street", "Road", "Oval"
        private Integer eventsParticipated;
        private BigDecimal averageScore;
        private BigDecimal bestScore;
        private String bestEvent;
        private BigDecimal qualifyingAverage;
        private BigDecimal raceAverage;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WeatherPerformance {
        private String weatherCondition; // "Dry", "Wet", "Mixed"
        private Integer eventsParticipated;
        private BigDecimal averageScore;
        private BigDecimal bestScore;
        private String bestEvent;
        private BigDecimal qualifyingAverage;
        private BigDecimal raceAverage;
    }
} 