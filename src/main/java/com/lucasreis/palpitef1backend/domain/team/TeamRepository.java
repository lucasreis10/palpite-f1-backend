package com.lucasreis.palpitef1backend.domain.team;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    
    // Buscar equipes por ano
    List<Team> findByYearOrderByTotalScoreDesc(Integer year);
    
    // Buscar equipes ativas por ano
    List<Team> findByYearAndActiveOrderByTotalScoreDesc(Integer year, Boolean active);
    
    // Buscar equipe por nome e ano
    Optional<Team> findByNameAndYear(String name, Integer year);
    
    // Verificar se existe equipe com o nome no ano
    boolean existsByNameAndYear(String name, Integer year);
    
    // Buscar equipe de um usuário em um ano específico
    @Query("SELECT t FROM Team t WHERE (t.user1.id = :userId OR t.user2.id = :userId) AND t.year = :year")
    Optional<Team> findByUserIdAndYear(@Param("userId") Long userId, @Param("year") Integer year);
    
    // Buscar todas as equipes de um usuário
    @Query("SELECT t FROM Team t WHERE (t.user1.id = :userId OR t.user2.id = :userId) ORDER BY t.year DESC")
    List<Team> findByUserId(@Param("userId") Long userId);
    
    // Verificar se um usuário já está em uma equipe no ano
    @Query("SELECT COUNT(t) > 0 FROM Team t WHERE (t.user1.id = :userId OR t.user2.id = :userId) AND t.year = :year")
    boolean existsByUserIdAndYear(@Param("userId") Long userId, @Param("year") Integer year);
    
    // Buscar equipes por usuários específicos e ano
    @Query("SELECT t FROM Team t WHERE ((t.user1.id = :user1Id AND t.user2.id = :user2Id) OR (t.user1.id = :user2Id AND t.user2.id = :user1Id)) AND t.year = :year")
    Optional<Team> findByUsersAndYear(@Param("user1Id") Long user1Id, @Param("user2Id") Long user2Id, @Param("year") Integer year);
    
    // Buscar ranking de equipes por ano (top N)
    @Query("SELECT t FROM Team t WHERE t.year = :year AND t.active = true ORDER BY t.totalScore DESC")
    List<Team> findTopTeamsByYear(@Param("year") Integer year);
    
    // Buscar equipes por nome (busca parcial)
    @Query("SELECT t FROM Team t WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY t.year DESC, t.totalScore DESC")
    List<Team> findByNameContainingIgnoreCase(@Param("name") String name);
} 