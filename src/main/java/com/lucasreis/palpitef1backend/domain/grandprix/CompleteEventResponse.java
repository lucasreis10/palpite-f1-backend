package com.lucasreis.palpitef1backend.domain.grandprix;

import com.lucasreis.palpitef1backend.domain.guess.CalculateScoresResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompleteEventResponse {
    
    private GrandPrixResponse grandPrix;
    private boolean scoresCalculated;
    private List<CalculateScoresResponse> calculationResults;
    private String message;
    private List<String> warnings;
} 