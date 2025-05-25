package com.lucasreis.palpitef1backend.domain.guess;

import com.lucasreis.palpitef1backend.domain.grandprix.GrandPrix;
import com.lucasreis.palpitef1backend.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "guesses", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "grand_prix_id", "guess_type"})
})
public class Guess {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grand_prix_id", nullable = false)
    private GrandPrix grandPrix;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "guess_type", nullable = false)
    private GuessType guessType; // QUALIFYING ou RACE
    
    // Palpite do usuário - lista de IDs dos pilotos em ordem de posição
    @ElementCollection
    @CollectionTable(name = "guess_pilots", joinColumns = @JoinColumn(name = "guess_id"))
    @Column(name = "pilot_id")
    @OrderColumn(name = "position")
    private List<Long> pilotIds;
    
    // Resultado real - preenchido pelo admin após o evento
    @ElementCollection
    @CollectionTable(name = "guess_real_results", joinColumns = @JoinColumn(name = "guess_id"))
    @Column(name = "pilot_id")
    @OrderColumn(name = "position")
    private List<Long> realResultPilotIds;
    
    @Column(name = "score", precision = 10, scale = 3)
    private BigDecimal score;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean calculated = false; // Se a pontuação já foi calculada
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // Métodos auxiliares
    public boolean isQualifying() {
        return GuessType.QUALIFYING.equals(this.guessType);
    }
    
    public boolean isRace() {
        return GuessType.RACE.equals(this.guessType);
    }
    
    public boolean hasRealResult() {
        return realResultPilotIds != null && !realResultPilotIds.isEmpty();
    }
    
    public boolean isCalculated() {
        return calculated != null && calculated;
    }
} 