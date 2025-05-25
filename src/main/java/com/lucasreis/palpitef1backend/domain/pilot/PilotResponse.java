package com.lucasreis.palpitef1backend.domain.pilot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PilotResponse {
    
    private Long id;
    
    private String driverId;
    
    private String givenName;
    
    private String familyName;
    
    private String fullName;
    
    private LocalDate dateOfBirth;
    
    private String nationality;
    
    private String url;
    
    private Integer permanentNumber;
    
    private String code;
    
    private Boolean active;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    public static PilotResponse fromPilot(Pilot pilot) {
        return PilotResponse.builder()
                .id(pilot.getId())
                .driverId(pilot.getDriverId())
                .givenName(pilot.getGivenName())
                .familyName(pilot.getFamilyName())
                .fullName(pilot.getFullName())
                .dateOfBirth(pilot.getDateOfBirth())
                .nationality(pilot.getNationality())
                .url(pilot.getUrl())
                .permanentNumber(pilot.getPermanentNumber())
                .code(pilot.getCode())
                .active(pilot.getActive())
                .createdAt(pilot.getCreatedAt())
                .updatedAt(pilot.getUpdatedAt())
                .build();
    }
} 