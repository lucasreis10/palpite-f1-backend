package com.lucasreis.palpitef1backend.domain.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LastResultResponse {
    private String grandPrixName;
    private List<PilotResultInfo> qualifyingResults;
    private List<PilotResultInfo> raceResults;
} 