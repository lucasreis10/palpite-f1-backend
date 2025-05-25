package com.lucasreis.palpitef1backend.domain.grandprix;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface GrandPrixRepository extends JpaRepository<GrandPrix, Long> {
    
    // Buscar por temporada
    List<GrandPrix> findBySeasonOrderByRound(Integer season);
    
    // Buscar por temporada e status ativo
    List<GrandPrix> findBySeasonAndActiveOrderByRound(Integer season, Boolean active);
    
    // Buscar por temporada e rodada
    Optional<GrandPrix> findBySeasonAndRound(Integer season, Integer round);
    
    // Buscar por nome e temporada
    Optional<GrandPrix> findByNameAndSeason(String name, Integer season);
    
    // Verificar se existe GP com nome na temporada
    boolean existsByNameAndSeason(String name, Integer season);
    
    // Verificar se existe GP com rodada na temporada
    boolean existsBySeasonAndRound(Integer season, Integer round);
    
    // Buscar por país
    List<GrandPrix> findByCountryIgnoreCaseOrderBySeasonDescRoundAsc(String country);
    
    // Buscar GPs por status de conclusão
    List<GrandPrix> findBySeasonAndCompletedOrderByRound(Integer season, Boolean completed);
    
    // Buscar próximos GPs (não concluídos e data da corrida futura)
    @Query("SELECT gp FROM GrandPrix gp WHERE gp.completed = false AND gp.raceDateTime > :now ORDER BY gp.raceDateTime ASC")
    List<GrandPrix> findUpcomingGrandPrix(@Param("now") LocalDateTime now);
    
    // Buscar próximo GP
    @Query("SELECT gp FROM GrandPrix gp WHERE gp.completed = false AND gp.raceDateTime > :now ORDER BY gp.raceDateTime ASC LIMIT 1")
    Optional<GrandPrix> findNextGrandPrix(@Param("now") LocalDateTime now);
    
    // Buscar GPs de uma temporada em um período
    @Query("SELECT gp FROM GrandPrix gp WHERE gp.season = :season AND gp.raceDateTime BETWEEN :startDate AND :endDate ORDER BY gp.round")
    List<GrandPrix> findBySeasonAndDateRange(@Param("season") Integer season, 
                                           @Param("startDate") LocalDateTime startDate, 
                                           @Param("endDate") LocalDateTime endDate);
    
    // Buscar GPs com sprint
    @Query("SELECT gp FROM GrandPrix gp WHERE gp.season = :season AND gp.sprintDateTime IS NOT NULL ORDER BY gp.round")
    List<GrandPrix> findSprintWeekendsBySeason(@Param("season") Integer season);
    
    // Buscar por nome do circuito
    @Query("SELECT gp FROM GrandPrix gp WHERE LOWER(gp.circuitName) LIKE LOWER(CONCAT('%', :circuitName, '%')) ORDER BY gp.season DESC, gp.round ASC")
    List<GrandPrix> findByCircuitNameContainingIgnoreCase(@Param("circuitName") String circuitName);
    
    // Buscar últimos GPs realizados
    @Query("SELECT gp FROM GrandPrix gp WHERE gp.completed = true ORDER BY gp.raceDateTime DESC")
    List<GrandPrix> findRecentCompletedGrandPrix();
    
    // Buscar temporadas disponíveis
    @Query("SELECT DISTINCT gp.season FROM GrandPrix gp ORDER BY gp.season DESC")
    List<Integer> findAvailableSeasons();
    
    // Contar GPs ativos
    long countByActiveTrue();
    
    // Buscar GPs concluídos ordenados por data (mais recente primeiro)
    List<GrandPrix> findByCompletedTrueOrderByRaceDateTimeDesc();
} 