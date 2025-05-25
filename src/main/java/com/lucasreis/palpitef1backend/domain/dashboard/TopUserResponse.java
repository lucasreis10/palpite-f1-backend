package com.lucasreis.palpitef1backend.domain.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopUserResponse {
    private Long id;
    private String name;
    private String email;
    private BigDecimal totalScore;
    private Integer position;
} 