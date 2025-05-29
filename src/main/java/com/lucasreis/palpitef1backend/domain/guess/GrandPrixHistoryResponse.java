package com.lucasreis.palpitef1backend.domain.guess;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrandPrixHistoryResponse {
    
    private Long grandPrixId;
    private String grandPrixName;
    private String country;
    private Integer season;
    private Integer round;
    private LocalDateTime raceDate;
    private Boolean completed;
    
    // Ranking do Qualifying
    private List<ParticipantRanking> qualifyingRanking;
    
    // Ranking da Corrida
    private List<ParticipantRanking> raceRanking;
    
    // Ranking Combinado (Qualifying + Race)
    private List<ParticipantRanking> combinedRanking;
    
    // Estatísticas do evento
    private EventStatistics statistics;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParticipantRanking {
        private Integer position;
        private Long userId;
        private String userName;
        private String userEmail;
        private BigDecimal score;
        private BigDecimal qualifyingScore;
        private BigDecimal raceScore;
        private Boolean hasQualifyingGuess;
        private Boolean hasRaceGuess;
        private Integer totalGuesses;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EventStatistics {
        private Integer totalParticipants;
        private Integer qualifyingParticipants;
        private Integer raceParticipants;
        private BigDecimal averageQualifyingScore;
        private BigDecimal averageRaceScore;
        private BigDecimal averageCombinedScore;
        private BigDecimal highestQualifyingScore;
        private BigDecimal highestRaceScore;
        private BigDecimal highestCombinedScore;
        private String topPerformerName;
        private BigDecimal topPerformerScore;
    }
} 