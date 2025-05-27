package com.lucasreis.palpitef1backend.domain.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TopUserResponse {
    private Long id;
    private String name;
    private String email;
    private BigDecimal totalScore;
    private Integer position;
    private String teamName;
    private Long teamId;
} 