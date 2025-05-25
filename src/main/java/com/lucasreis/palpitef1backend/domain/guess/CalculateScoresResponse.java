package com.lucasreis.palpitef1backend.domain.guess;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalculateScoresResponse {
    
    private Long grandPrixId;
    private String grandPrixName;
    private GuessType guessType;
    private Integer totalGuesses;
    private Integer calculatedGuesses;
    private List<GuessResponse> results;
    private String message;
} 