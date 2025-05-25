package com.lucasreis.palpitef1backend.domain.grandprix;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateGrandPrixRequest {
    
    @NotNull(message = "Temporada é obrigatória")
    @Min(value = 2020, message = "Temporada deve ser maior ou igual a 2020")
    @Max(value = 2030, message = "Temporada deve ser menor ou igual a 2030")
    private Integer season;
    
    @NotNull(message = "Rodada é obrigatória")
    @Min(value = 1, message = "Rodada deve ser maior que 0")
    @Max(value = 30, message = "Rodada deve ser menor ou igual a 30")
    private Integer round;
    
    @NotBlank(message = "Nome do GP é obrigatório")
    @Size(min = 3, max = 100, message = "Nome do GP deve ter entre 3 e 100 caracteres")
    private String name;
    
    @NotBlank(message = "País é obrigatório")
    @Size(min = 2, max = 100, message = "País deve ter entre 2 e 100 caracteres")
    private String country;
    
    @NotBlank(message = "Cidade é obrigatória")
    @Size(min = 2, max = 100, message = "Cidade deve ter entre 2 e 100 caracteres")
    private String city;
    
    @NotBlank(message = "Nome do circuito é obrigatório")
    @Size(min = 3, max = 100, message = "Nome do circuito deve ter entre 3 e 100 caracteres")
    private String circuitName;
    
    @Size(max = 500, message = "URL do circuito deve ter no máximo 500 caracteres")
    private String circuitUrl;
    
    // Horários dos eventos
    private LocalDateTime practice1DateTime;
    private LocalDateTime practice2DateTime;
    private LocalDateTime practice3DateTime;
    private LocalDateTime qualifyingDateTime;
    private LocalDateTime sprintDateTime;
    
    @NotNull(message = "Data e hora da corrida são obrigatórias")
    private LocalDateTime raceDateTime;
    
    // Informações adicionais
    @Size(max = 10, message = "Fuso horário deve ter no máximo 10 caracteres")
    private String timezone;
    
    @Min(value = 1, message = "Número de voltas deve ser maior que 0")
    @Max(value = 100, message = "Número de voltas deve ser menor ou igual a 100")
    private Integer laps;
    
    @DecimalMin(value = "0.1", message = "Comprimento do circuito deve ser maior que 0")
    @DecimalMax(value = "10.0", message = "Comprimento do circuito deve ser menor ou igual a 10 km")
    private Double circuitLength;
    
    @Size(max = 1000, message = "Descrição deve ter no máximo 1000 caracteres")
    private String description;
} 