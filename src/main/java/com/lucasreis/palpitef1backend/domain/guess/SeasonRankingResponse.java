package com.lucasreis.palpitef1backend.domain.guess;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeasonRankingResponse {
    
    private Integer season;
    private List<SeasonParticipant> ranking;
    private SeasonStatistics statistics;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SeasonParticipant {
        private Integer position;
        private Long userId;
        private String userName;
        private String userEmail;
        private BigDecimal totalScore;
        private BigDecimal qualifyingScore;
        private BigDecimal raceScore;
        private Integer totalGuesses;
        private Integer qualifyingGuesses;
        private Integer raceGuesses;
        private BigDecimal averageScore;
        private BigDecimal bestEventScore;
        private String bestEventName;
        private Integer eventsParticipated;
        private List<EventParticipation> eventHistory;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EventParticipation {
        private Long grandPrixId;
        private String grandPrixName;
        private String country;
        private Integer round;
        private BigDecimal qualifyingScore;
        private BigDecimal raceScore;
        private BigDecimal totalScore;
        private Integer qualifyingPosition;
        private Integer racePosition;
        private Integer combinedPosition;
        private Boolean hasQualifyingGuess;
        private Boolean hasRaceGuess;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SeasonStatistics {
        private Integer totalParticipants;
        private Integer totalEvents;
        private Integer completedEvents;
        private BigDecimal averageParticipationRate;
        private BigDecimal averageScore;
        private BigDecimal highestEventScore;
        private String mostActiveParticipant;
        private Integer mostActiveParticipantGuesses;
        private String topPerformer;
        private BigDecimal topPerformerScore;
    }
} 