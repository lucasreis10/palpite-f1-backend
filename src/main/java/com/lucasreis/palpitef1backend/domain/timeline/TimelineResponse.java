package com.lucasreis.palpitef1backend.domain.timeline;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimelineResponse {
    private Long id;
    private String eventType;
    private String title;
    private String description;
    private Integer pointsGained;
    private Integer season;
    private LocalDateTime createdAt;
    private String icon;
    private String color;
    private String grandPrixName;
    
    public static TimelineResponse fromEntity(TimelineEvent event) {
        return TimelineResponse.builder()
            .id(event.getId())
            .eventType(event.getEventType().name())
            .title(event.getTitle())
            .description(event.getDescription())
            .pointsGained(event.getPointsGained())
            .season(event.getSeason())
            .createdAt(event.getCreatedAt())
            .icon(event.getIcon())
            .color(event.getColor())
            .grandPrixName(event.getGrandPrix() != null ? event.getGrandPrix().getName() : null)
            .build();
    }
} 