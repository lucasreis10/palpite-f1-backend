package com.lucasreis.palpitef1backend.domain.team;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamResponse {
    
    private Long id;
    private String name;
    private Integer year;
    private UserSummary user1;
    private UserSummary user2;
    private Integer totalScore;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserSummary {
        private Long id;
        private String name;
        private String email;
    }
    
    public static TeamResponse fromTeam(Team team) {
        return TeamResponse.builder()
                .id(team.getId())
                .name(team.getName())
                .year(team.getYear())
                .user1(UserSummary.builder()
                        .id(team.getUser1().getId())
                        .name(team.getUser1().getName())
                        .email(team.getUser1().getEmail())
                        .build())
                .user2(UserSummary.builder()
                        .id(team.getUser2().getId())
                        .name(team.getUser2().getName())
                        .email(team.getUser2().getEmail())
                        .build())
                .totalScore(team.getTotalScore())
                .active(team.getActive())
                .createdAt(team.getCreatedAt())
                .updatedAt(team.getUpdatedAt())
                .build();
    }
} 