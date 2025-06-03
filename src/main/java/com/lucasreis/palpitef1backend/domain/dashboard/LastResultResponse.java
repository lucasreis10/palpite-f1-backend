package com.lucasreis.palpitef1backend.domain.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LastResultResponse {
    private String grandPrixName;
    private List<PilotResultInfo> qualifyingResults;
    private List<PilotResultInfo> raceResults;
    private List<ParticipantGuess> qualifyingGuesses;
    private List<ParticipantGuess> raceGuesses;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ParticipantGuess {
        private Long userId;
        private String userName;
        private String userEmail;
        private String teamName;
        private BigDecimal score;
        private Integer position;
        private Boolean hasGuess;
    }
} 