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
public class LiveTimingRequest {
    
    private Long grandPrixId;
    private String sessionType; // "QUALIFYING" ou "RACE"
    private List<CurrentPosition> currentPositions;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CurrentPosition {
        private Integer position;
        private Integer driverNumber;
        private String driverName;
        private String driverAcronym;
        private String teamName;
        private String teamColor;
        private Double gapToLeader;
        private Double interval;
    }
} 