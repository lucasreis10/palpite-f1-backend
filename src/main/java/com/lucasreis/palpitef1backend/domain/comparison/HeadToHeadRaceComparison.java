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
public class HeadToHeadRaceComparison {
    private Long grandPrixId;
    private String grandPrixName;
    private BigDecimal user1Score;
    private BigDecimal user2Score;
    private Long winner; // ID do usuário vencedor, null em caso de empate
    private BigDecimal scoreDifference;
} 