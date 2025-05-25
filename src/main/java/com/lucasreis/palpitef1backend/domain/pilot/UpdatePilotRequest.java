package com.lucasreis.palpitef1backend.domain.pilot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePilotRequest {
    
    private String givenName;
    
    private String familyName;
    
    private LocalDate dateOfBirth;
    
    private String nationality;
    
    private String url;
    
    private Integer permanentNumber;
    
    private String code;
    
    private Boolean active;
} 