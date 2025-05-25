package com.lucasreis.palpitef1backend.domain.pilot;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class CreatePilotsRequest {
    
    @NotEmpty(message = "A lista de pilotos não pode estar vazia")
    @Valid
    private List<CreatePilotRequest> pilots;
} 