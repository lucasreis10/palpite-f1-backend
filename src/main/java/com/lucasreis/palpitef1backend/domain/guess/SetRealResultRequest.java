package com.lucasreis.palpitef1backend.domain.guess;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class SetRealResultRequest {
    
    @NotNull(message = "ID do Grande Prêmio é obrigatório")
    private Long grandPrixId;
    
    @NotNull(message = "Tipo do evento é obrigatório")
    private GuessType guessType;
    
    @NotEmpty(message = "Lista de pilotos do resultado real não pode estar vazia")
    @Size(min = 10, max = 20, message = "Lista deve conter entre 10 e 20 pilotos")
    private List<Long> realResultPilotIds;
} 