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
public class CalculatorResponse {
    
    private BigDecimal totalScore;
    private BigDecimal maxPossibleScore;
    private List<PositionScore> individualScores;
    private List<ScoreDetail> details;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PositionScore {
        private Integer position;
        private BigDecimal score;
        private Long guessPilotId;
        private Long actualPilotId;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScoreDetail {
        private Integer position;
        private String guessPilotName;
        private String actualPilotName;
        private BigDecimal points;
        private Boolean isCorrect;
    }
} 