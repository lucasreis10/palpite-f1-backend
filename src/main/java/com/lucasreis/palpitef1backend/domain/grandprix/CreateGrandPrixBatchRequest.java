package com.lucasreis.palpitef1backend.domain.grandprix;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class CreateGrandPrixBatchRequest {
    
    @NotEmpty(message = "Lista de Grandes Prêmios não pode estar vazia")
    @Valid
    private List<CreateGrandPrixRequest> grandPrix;
} 