package com.lucasreis.palpitef1backend.domain.grandprix;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrandPrixResponse {
    
    private Long id;
    private Integer season;
    private Integer round;
    private String name;
    private String country;
    private String city;
    private String circuitName;
    private String circuitUrl;
    private String fullName;
    
    // Horários dos eventos
    private LocalDateTime practice1DateTime;
    private LocalDateTime practice2DateTime;
    private LocalDateTime practice3DateTime;
    private LocalDateTime qualifyingDateTime;
    private LocalDateTime sprintDateTime;
    private LocalDateTime raceDateTime;
    private LocalDateTime bettingDeadline;

    // Informações adicionais
    private String timezone;
    private Integer laps;
    private Double circuitLength;
    private String description;
    private Boolean active;
    private Boolean completed;
    private Boolean isSprintWeekend;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static GrandPrixResponse fromGrandPrix(GrandPrix grandPrix) {
        return GrandPrixResponse.builder()
                .id(grandPrix.getId())
                .season(grandPrix.getSeason())
                .round(grandPrix.getRound())
                .name(grandPrix.getName())
                .country(grandPrix.getCountry())
                .city(grandPrix.getCity())
                .circuitName(grandPrix.getCircuitName())
                .circuitUrl(grandPrix.getCircuitUrl())
                .fullName(grandPrix.getFullName())
                .practice1DateTime(grandPrix.getPractice1DateTime())
                .practice2DateTime(grandPrix.getPractice2DateTime())
                .practice3DateTime(grandPrix.getPractice3DateTime())
                .qualifyingDateTime(grandPrix.getQualifyingDateTime())
                .sprintDateTime(grandPrix.getSprintDateTime())
                .raceDateTime(grandPrix.getRaceDateTime())
                .timezone(grandPrix.getTimezone())
                .laps(grandPrix.getLaps())
                .circuitLength(grandPrix.getCircuitLength())
                .description(grandPrix.getDescription())
                .active(grandPrix.getActive())
                .completed(grandPrix.getCompleted())
                .isSprintWeekend(grandPrix.isSprintWeekend())
                .createdAt(grandPrix.getCreatedAt())
                .updatedAt(grandPrix.getUpdatedAt())
                .build();
    }
} 
