package com.lucasreis.palpitef1backend.domain.team;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateTeamRequest {
    
    @Size(min = 3, max = 100, message = "Nome da equipe deve ter entre 3 e 100 caracteres")
    private String name;
    
    @Min(value = 0, message = "Pontuação total deve ser maior ou igual a 0")
    @Max(value = 10000, message = "Pontuação total deve ser menor ou igual a 10000")
    private Integer totalScore;
    
    private Boolean active;
} 