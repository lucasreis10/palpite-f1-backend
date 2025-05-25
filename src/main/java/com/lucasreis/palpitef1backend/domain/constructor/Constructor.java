package com.lucasreis.palpitef1backend.domain.constructor;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "constructors")
public class Constructor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String constructorId; // ID único da API Ergast (ex: "mercedes")
    
    @Column(nullable = false)
    private String name; // Nome da escuderia (ex: "Mercedes")
    
    @Column
    private String nationality; // Nacionalidade da escuderia
    
    @Column
    private String url; // URL da Wikipedia
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true; // Status ativo/inativo da escuderia
    
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
} 