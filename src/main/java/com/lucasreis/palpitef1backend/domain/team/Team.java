package com.lucasreis.palpitef1backend.domain.team;

import com.lucasreis.palpitef1backend.domain.user.User;
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
@Table(name = "teams", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name", "year"}),
    @UniqueConstraint(columnNames = {"user1_id", "year"}),
    @UniqueConstraint(columnNames = {"user2_id", "year"})
})
public class Team {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(nullable = false)
    private Integer year;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user1_id", nullable = false)
    private User user1;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user2_id", nullable = false)
    private User user2;
    
    @Column(nullable = false)
    @Builder.Default
    private Integer totalScore = 0;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // Método para verificar se um usuário faz parte da equipe
    public boolean containsUser(User user) {
        return user1.getId().equals(user.getId()) || user2.getId().equals(user.getId());
    }
    
    // Método para verificar se um usuário faz parte da equipe por ID
    public boolean containsUserId(Long userId) {
        return user1.getId().equals(userId) || user2.getId().equals(userId);
    }
    
    // Método para obter o parceiro de um usuário na equipe
    public User getPartner(User user) {
        if (user1.getId().equals(user.getId())) {
            return user2;
        } else if (user2.getId().equals(user.getId())) {
            return user1;
        }
        return null;
    }
} 