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
public class PerformanceComparison {
    // Médias por tipo de sessão
    private BigDecimal user1QualifyingAverage;
    private BigDecimal user1RaceAverage;
    private BigDecimal user2QualifyingAverage;
    private BigDecimal user2RaceAverage;
    
    // Consistência (quanto menor, mais consistente)
    private BigDecimal user1Consistency;
    private BigDecimal user2Consistency;
    
    // Melhor e pior performance
    private BigDecimal user1BestScore;
    private BigDecimal user1WorstScore;
    private BigDecimal user2BestScore;
    private BigDecimal user2WorstScore;
} 