package com.lucasreis.palpitef1backend.domain.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PilotResultInfo {
    private Integer position;
    private String pilotName;
    private String teamName;
} 