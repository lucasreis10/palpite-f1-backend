package com.lucasreis.palpitef1backend.domain.guess;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CreateGuessRequest {
    
    @NotNull(message = "ID do Grande Prêmio é obrigatório")
    private Long grandPrixId;
    
    @NotNull(message = "Tipo do palpite é obrigatório")
    private GuessType guessType;
    
    @NotEmpty(message = "Lista de pilotos não pode estar vazia")
    @Size(min = 10, max = 20, message = "Lista deve conter entre 10 e 20 pilotos")
    private List<Long> pilotIds;
} 