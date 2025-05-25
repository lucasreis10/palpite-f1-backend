package com.lucasreis.palpitef1backend.domain.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardStatsResponse {
    private BestScoreInfo bestScore;
    private Long totalGuesses;
    private BigDecimal averageGuessesPerRace;
    private Long totalUsers;
    private Long totalRaces;
} 