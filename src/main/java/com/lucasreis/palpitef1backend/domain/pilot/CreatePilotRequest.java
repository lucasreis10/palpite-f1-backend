package com.lucasreis.palpitef1backend.domain.pilot;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePilotRequest {
    
    @NotBlank(message = "O ID do piloto é obrigatório")
    private String driverId;
    
    @NotBlank(message = "O nome é obrigatório")
    private String givenName;
    
    @NotBlank(message = "O sobrenome é obrigatório")
    private String familyName;
    
    @NotNull(message = "A data de nascimento é obrigatória")
    private LocalDate dateOfBirth;
    
    @NotBlank(message = "A nacionalidade é obrigatória")
    private String nationality;
    
    private String url;
    
    private Integer permanentNumber;
    
    private String code;
} 