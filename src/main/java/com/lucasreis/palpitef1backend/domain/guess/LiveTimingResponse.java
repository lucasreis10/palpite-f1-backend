package com.lucasreis.palpitef1backend.domain.guess;

import com.lucasreis.palpitef1backend.domain.pilot.PilotResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LiveTimingResponse {
    
    private SessionInfo session;
    private List<DriverStanding> standings;
    private List<RaceControlMessage> raceControl;
    private List<LiveRanking> liveRanking;
    private Boolean hasGuesses;
    private Boolean isMockData;
    private Boolean hasF1Data;
    private LocalDateTime timestamp;
    private String sessionName;
    private String grandPrixName;
    private String sessionStatus;
    private Boolean isActive;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SessionInfo {
        private Long sessionKey;
        private String sessionName;
        private String sessionType;
        private LocalDateTime dateStart;
        private LocalDateTime dateEnd;
        private String location;
        private String countryName;
        private String circuitShortName;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DriverStanding {
        private Integer position;
        private Integer driverNumber;
        private String driverName;
        private String driverAcronym;
        private String teamName;
        private String teamColor;
        private Double gapToLeader;
        private Double interval;
        private LocalDateTime lastUpdate;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RaceControlMessage {
        private LocalDateTime date;
        private String category;
        private String message;
        private String flag;
        private String scope;
        private Integer driverNumber;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LiveRanking {
        private Long userId;
        private String userName;
        private String userEmail;
        private BigDecimal currentScore;
        private BigDecimal totalPossibleScore;
        private Integer correctGuesses;
        private List<PilotResponse> raceGuesses;
        private Map<Integer, Integer> positionDifferences;
    }
} 