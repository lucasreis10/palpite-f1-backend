package com.lucasreis.palpitef1backend.domain.comparison;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HeadToHeadResponse {
    private String user1Name;
    private String user2Name;
    private UserComparisonStats user1Stats;
    private UserComparisonStats user2Stats;
    private Integer user1Wins;
    private Integer user2Wins;
    private Integer ties;
    private Integer totalRaces;
    private List<HeadToHeadRaceComparison> raceComparisons;
    private PerformanceComparison performanceComparison;
} 