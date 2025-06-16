package com.lucasreis.palpitef1backend.domain.guess;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CalculatorRequest {
    
    @NotNull(message = "Tipo de palpite é obrigatório")
    private GuessType guessType;
    
    @NotNull(message = "Palpite do usuário é obrigatório")
    @Size(min = 1, max = 14, message = "Palpite deve ter entre 1 e 14 posições")
    private List<Long> userGuess;
    
    @NotNull(message = "Resultado real é obrigatório")
    @Size(min = 1, max = 14, message = "Resultado real deve ter entre 1 e 14 posições")
    private List<Long> actualResult;
} 