package com.lucasreis.palpitef1backend.domain.team;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateTeamRequest {
    
    @NotBlank(message = "Nome da equipe é obrigatório")
    @Size(min = 3, max = 100, message = "Nome da equipe deve ter entre 3 e 100 caracteres")
    private String name;
    
    @NotNull(message = "Ano é obrigatório")
    @Min(value = 2020, message = "Ano deve ser maior ou igual a 2020")
    @Max(value = 2030, message = "Ano deve ser menor ou igual a 2030")
    private Integer year;
    
    @NotNull(message = "ID do primeiro usuário é obrigatório")
    private Long user1Id;
    
    @NotNull(message = "ID do segundo usuário é obrigatório")
    private Long user2Id;
} 