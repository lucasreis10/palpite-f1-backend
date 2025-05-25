package com.lucasreis.palpitef1backend.domain.grandprix;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "grand_prix", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"season", "round"}),
    @UniqueConstraint(columnNames = {"season", "name"})
})
public class GrandPrix {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Integer season; // Temporada (ex: 2025)
    
    @Column(nullable = false)
    private Integer round; // Rodada da temporada (1, 2, 3...)
    
    @Column(nullable = false, length = 100)
    private String name; // Nome do GP (ex: "Monaco Grand Prix")
    
    @Column(nullable = false, length = 100)
    private String country; // País (ex: "Monaco")
    
    @Column(nullable = false, length = 100)
    private String city; // Cidade (ex: "Monte Carlo")
    
    @Column(nullable = false, length = 100)
    private String circuitName; // Nome do circuito (ex: "Circuit de Monaco")
    
    @Column(length = 500)
    private String circuitUrl; // URL da Wikipedia do circuito
    
    // Horários dos eventos (em UTC)
    @Column(name = "practice1_datetime")
    private LocalDateTime practice1DateTime;
    
    @Column(name = "practice2_datetime")
    private LocalDateTime practice2DateTime;
    
    @Column(name = "practice3_datetime")
    private LocalDateTime practice3DateTime;
    
    @Column(name = "qualifying_datetime")
    private LocalDateTime qualifyingDateTime;
    
    @Column(name = "sprint_datetime")
    private LocalDateTime sprintDateTime; // Opcional, nem todos os GPs têm sprint
    
    @Column(name = "race_datetime", nullable = false)
    private LocalDateTime raceDateTime;
    
    // Informações adicionais
    @Column(length = 10)
    private String timezone; // Fuso horário local (ex: "UTC+1")
    
    @Column
    private Integer laps; // Número de voltas da corrida
    
    @Column
    private Double circuitLength; // Comprimento do circuito em km
    
    @Column(length = 1000)
    private String description; // Descrição do GP
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true; // Se o GP está ativo no calendário
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean completed = false; // Se o GP já foi realizado
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // Método para verificar se é um fim de semana de sprint
    public boolean isSprintWeekend() {
        return sprintDateTime != null;
    }
    
    // Método para obter o nome completo do GP
    public String getFullName() {
        return name + " - " + country;
    }
} 