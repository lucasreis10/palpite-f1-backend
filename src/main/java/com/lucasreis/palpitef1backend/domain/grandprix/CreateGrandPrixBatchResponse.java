package com.lucasreis.palpitef1backend.domain.grandprix;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateGrandPrixBatchResponse {
    
    private Integer totalRequested;
    private Integer totalCreated;
    private Integer totalErrors;
    private List<GrandPrixResponse> created;
    private List<String> errors;
} 