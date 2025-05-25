package com.lucasreis.palpitef1backend.domain.pilot;

import com.lucasreis.palpitef1backend.domain.constructor.Constructor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pilots")
public class Pilot {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String driverId; // ID único da API Ergast (ex: "hamilton")
    
    @Column(nullable = false)
    private String givenName; // Nome (ex: "Lewis")
    
    @Column(nullable = false)
    private String familyName; // Sobrenome (ex: "Hamilton")
    
    @Column(nullable = false)
    private LocalDate dateOfBirth;
    
    @Column(nullable = false)
    private String nationality;
    
    @Column
    private String url; // URL da Wikipedia
    
    @Column
    private Integer permanentNumber; // Número permanente do piloto
    
    @Column
    private String code; // Código de 3 letras (ex: "HAM")
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "constructor_id")
    private Constructor constructor; // Escuderia do piloto
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true; // Status ativo/inativo do piloto
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public String getFullName() {
        return givenName + " " + familyName;
    }
} 