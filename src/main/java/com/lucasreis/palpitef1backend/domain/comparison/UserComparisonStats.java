package com.lucasreis.palpitef1backend.domain.comparison;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserComparisonStats {
    private Integer totalGuesses;
    private BigDecimal totalPoints;
    private BigDecimal averagePoints;
    private BigDecimal bestRaceScore;
    private Integer correctPredictions;
    private BigDecimal accuracyRate;
} 