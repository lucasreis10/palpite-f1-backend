package com.lucasreis.palpitef1backend.domain.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BestScoreInfo {
    private BigDecimal score;
    private String userName;
    private String grandPrixName;
} 