package com.lucasreis.palpitef1backend.domain.pilot;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CreatePilotsResponse {
    
    private List<PilotResponse> createdPilots;
    private List<String> errors;
    private int totalRequested;
    private int totalCreated;
    private int totalErrors;
    
    public static CreatePilotsResponse of(List<PilotResponse> createdPilots, List<String> errors, int totalRequested) {
        return new CreatePilotsResponse(
            createdPilots,
            errors,
            totalRequested,
            createdPilots.size(),
            errors.size()
        );
    }
} 