package com.lucasreis.palpitef1backend.domain.timeline;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TimelineRepository extends JpaRepository<TimelineEvent, Long> {
    
    // Buscar eventos por usuário e temporada
    @Query("SELECT t FROM TimelineEvent t " +
           "WHERE t.user.id = :userId AND t.season = :season " +
           "ORDER BY t.createdAt DESC")
    List<TimelineEvent> findByUserIdAndSeasonOrderByCreatedAtDesc(
        @Param("userId") Long userId, 
        @Param("season") Integer season
    );
    
    // Buscar eventos recentes do usuário
    @Query("SELECT t FROM TimelineEvent t " +
           "WHERE t.user.id = :userId " +
           "ORDER BY t.createdAt DESC")
    List<TimelineEvent> findByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);
    
    // Buscar eventos por tipo
    @Query("SELECT t FROM TimelineEvent t " +
           "WHERE t.user.id = :userId AND t.eventType = :eventType " +
           "ORDER BY t.createdAt DESC")
    List<TimelineEvent> findByUserIdAndEventType(
        @Param("userId") Long userId, 
        @Param("eventType") TimelineEventType eventType
    );
    
    // Buscar marcos de uma temporada específica
    @Query("SELECT t FROM TimelineEvent t " +
           "WHERE t.user.id = :userId AND t.season = :season " +
           "AND t.eventType IN :milestoneTypes " +
           "ORDER BY t.createdAt ASC")
    List<TimelineEvent> findMilestonesByUserAndSeason(
        @Param("userId") Long userId,
        @Param("season") Integer season,
        @Param("milestoneTypes") List<TimelineEventType> milestoneTypes
    );
    
    // Buscar eventos em um período
    @Query("SELECT t FROM TimelineEvent t " +
           "WHERE t.user.id = :userId " +
           "AND t.createdAt BETWEEN :startDate AND :endDate " +
           "ORDER BY t.createdAt DESC")
    List<TimelineEvent> findByUserIdAndDateRange(
        @Param("userId") Long userId,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );
    
    // Contar eventos por tipo
    @Query("SELECT COUNT(t) FROM TimelineEvent t " +
           "WHERE t.user.id = :userId AND t.eventType = :eventType")
    Long countByUserIdAndEventType(
        @Param("userId") Long userId, 
        @Param("eventType") TimelineEventType eventType
    );
    
    // Verificar se evento já existe (evitar duplicatas)
    @Query("SELECT COUNT(t) > 0 FROM TimelineEvent t " +
           "WHERE t.user.id = :userId " +
           "AND t.eventType = :eventType " +
           "AND t.grandPrix.id = :grandPrixId")
    boolean existsByUserIdAndEventTypeAndGrandPrixId(
        @Param("userId") Long userId,
        @Param("eventType") TimelineEventType eventType,
        @Param("grandPrixId") Long grandPrixId
    );
} 