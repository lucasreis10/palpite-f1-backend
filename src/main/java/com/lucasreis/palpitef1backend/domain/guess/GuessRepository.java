package com.lucasreis.palpitef1backend.domain.guess;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface GuessRepository extends JpaRepository<Guess, Long> {
    
    // Buscar palpite específico do usuário para um GP e tipo
    Optional<Guess> findByUserIdAndGrandPrixIdAndGuessType(Long userId, Long grandPrixId, GuessType guessType);
    
    // Verificar se usuário já tem palpite para um GP e tipo
    boolean existsByUserIdAndGrandPrixIdAndGuessType(Long userId, Long grandPrixId, GuessType guessType);
    
    // Buscar todos os palpites de um usuário
    List<Guess> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    // Buscar palpites de um usuário por temporada
    @Query("SELECT g FROM Guess g WHERE g.user.id = :userId AND g.grandPrix.season = :season ORDER BY g.grandPrix.round, g.guessType")
    List<Guess> findByUserIdAndSeason(@Param("userId") Long userId, @Param("season") Integer season);
    
    // Buscar todos os palpites de um GP específico
    List<Guess> findByGrandPrixIdAndGuessTypeOrderByCreatedAt(Long grandPrixId, GuessType guessType);
    
    // Buscar palpites não calculados de um GP e tipo
    List<Guess> findByGrandPrixIdAndGuessTypeAndCalculatedFalse(Long grandPrixId, GuessType guessType);
    
    // Buscar palpites calculados de um GP e tipo ordenados por pontuação
    @Query("SELECT g FROM Guess g WHERE g.grandPrix.id = :grandPrixId AND g.guessType = :guessType AND g.calculated = true ORDER BY g.score DESC")
    List<Guess> findByGrandPrixIdAndGuessTypeCalculatedOrderByScore(@Param("grandPrixId") Long grandPrixId, @Param("guessType") GuessType guessType);
    
    // Buscar ranking geral de uma temporada por tipo de palpite
    @Query("SELECT g FROM Guess g WHERE g.grandPrix.season = :season AND g.guessType = :guessType AND g.calculated = true ORDER BY g.score DESC")
    List<Guess> findSeasonRankingByGuessType(@Param("season") Integer season, @Param("guessType") GuessType guessType);
    
    // Buscar pontuação total de um usuário em uma temporada
    @Query("SELECT COALESCE(SUM(g.score), 0) FROM Guess g WHERE g.user.id = :userId AND g.grandPrix.season = :season AND g.calculated = true")
    Double getTotalScoreByUserAndSeason(@Param("userId") Long userId, @Param("season") Integer season);
    
    // Buscar pontuação total de um usuário em uma temporada (retorna BigDecimal)
    @Query("SELECT COALESCE(SUM(g.score), 0) FROM Guess g WHERE g.user.id = :userId AND g.grandPrix.season = :season AND g.calculated = true")
    Optional<BigDecimal> findTotalScoreByUserAndSeason(@Param("userId") Long userId, @Param("season") Integer season);
    
    // Buscar pontuação total de um usuário em uma temporada por tipo
    @Query("SELECT COALESCE(SUM(g.score), 0) FROM Guess g WHERE g.user.id = :userId AND g.grandPrix.season = :season AND g.guessType = :guessType AND g.calculated = true")
    Double getTotalScoreByUserSeasonAndType(@Param("userId") Long userId, @Param("season") Integer season, @Param("guessType") GuessType guessType);
    
    // Buscar ranking geral de uma temporada (soma de qualifying + race)
    @Query("SELECT g.user.id, g.user.name, COALESCE(SUM(g.score), 0) as totalScore " +
           "FROM Guess g " +
           "WHERE g.grandPrix.season = :season AND g.calculated = true " +
           "GROUP BY g.user.id, g.user.name " +
           "ORDER BY totalScore DESC")
    List<Object[]> getSeasonGeneralRanking(@Param("season") Integer season);
    
    // Buscar palpites de um usuário para um GP específico
    List<Guess> findByUserIdAndGrandPrixIdOrderByGuessType(Long userId, Long grandPrixId);
    
    // Buscar estatísticas de palpites de um usuário
    @Query("SELECT COUNT(g) FROM Guess g WHERE g.user.id = :userId")
    Long countGuessesByUser(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(g) FROM Guess g WHERE g.user.id = :userId AND g.calculated = true")
    Long countCalculatedGuessesByUser(@Param("userId") Long userId);
    
    // Buscar palpites ativos
    List<Guess> findByActiveTrue();
    
    // Buscar palpites por equipe (se usuário faz parte de uma equipe)
    @Query("SELECT g FROM Guess g JOIN Team t ON (t.user1.id = g.user.id OR t.user2.id = g.user.id) " +
           "WHERE t.id = :teamId AND g.grandPrix.season = :season AND g.calculated = true " +
           "ORDER BY g.grandPrix.round, g.guessType")
    List<Guess> findByTeamIdAndSeason(@Param("teamId") Long teamId, @Param("season") Integer season);
    
    // Buscar o palpite com maior pontuação
    @Query("SELECT g FROM Guess g WHERE g.calculated = true ORDER BY g.score DESC")
    List<Guess> findTop1ByCalculatedTrueOrderByScoreDesc();
    
    // Buscar palpites de uma temporada que foram calculados
    @Query("SELECT g FROM Guess g WHERE g.grandPrix.season = :season AND g.calculated = true")
    List<Guess> findBySeasonAndCalculatedTrue(@Param("season") Integer season);
    
    // Buscar palpites calculados por GP, tipo e que têm resultado real
    @Query("SELECT g FROM Guess g WHERE g.grandPrix.id = :grandPrixId AND g.guessType = :guessType AND g.calculated = true AND g.realResultPilotIds IS NOT NULL")
    List<Guess> findByGrandPrixIdAndGuessTypeAndCalculatedTrue(@Param("grandPrixId") Long grandPrixId, @Param("guessType") GuessType guessType);
    
    // ========== MÉTODOS PARA HISTÓRICO E RANKING DETALHADO ==========
    
    // Buscar todos os palpites calculados de um GP (qualifying + race)
    @Query("SELECT g FROM Guess g WHERE g.grandPrix.id = :grandPrixId AND g.calculated = true ORDER BY g.guessType, g.score DESC")
    List<Guess> findAllCalculatedGuessesByGrandPrix(@Param("grandPrixId") Long grandPrixId);
    
    // Buscar ranking combinado de um GP (soma qualifying + race por usuário)
    @Query("SELECT g.user.id, g.user.name, g.user.email, " +
           "COALESCE(SUM(CASE WHEN g.guessType = 'QUALIFYING' THEN g.score ELSE 0 END), 0) as qualifyingScore, " +
           "COALESCE(SUM(CASE WHEN g.guessType = 'RACE' THEN g.score ELSE 0 END), 0) as raceScore, " +
           "COALESCE(SUM(g.score), 0) as totalScore, " +
           "COUNT(CASE WHEN g.guessType = 'QUALIFYING' THEN 1 END) as qualifyingGuesses, " +
           "COUNT(CASE WHEN g.guessType = 'RACE' THEN 1 END) as raceGuesses, " +
           "COUNT(g) as totalGuesses " +
           "FROM Guess g " +
           "WHERE g.grandPrix.id = :grandPrixId AND g.calculated = true " +
           "GROUP BY g.user.id, g.user.name, g.user.email " +
           "ORDER BY totalScore DESC")
    List<Object[]> getGrandPrixCombinedRanking(@Param("grandPrixId") Long grandPrixId);
    
    // Buscar estatísticas de um GP
    @Query("SELECT " +
           "COUNT(DISTINCT g.user.id) as totalParticipants, " +
           "COUNT(CASE WHEN g.guessType = 'QUALIFYING' THEN 1 END) as qualifyingParticipants, " +
           "COUNT(CASE WHEN g.guessType = 'RACE' THEN 1 END) as raceParticipants, " +
           "COALESCE(AVG(CASE WHEN g.guessType = 'QUALIFYING' THEN g.score END), 0) as avgQualifyingScore, " +
           "COALESCE(AVG(CASE WHEN g.guessType = 'RACE' THEN g.score END), 0) as avgRaceScore, " +
           "COALESCE(AVG(g.score), 0) as avgCombinedScore, " +
           "COALESCE(MAX(CASE WHEN g.guessType = 'QUALIFYING' THEN g.score END), 0) as maxQualifyingScore, " +
           "COALESCE(MAX(CASE WHEN g.guessType = 'RACE' THEN g.score END), 0) as maxRaceScore, " +
           "COALESCE(MAX(g.score), 0) as maxCombinedScore " +
           "FROM Guess g " +
           "WHERE g.grandPrix.id = :grandPrixId AND g.calculated = true")
    List<Object[]> getGrandPrixStatistics(@Param("grandPrixId") Long grandPrixId);
    
    // Buscar melhor performer de um GP
    @Query("SELECT g.user.name, COALESCE(SUM(g.score), 0) as totalScore " +
           "FROM Guess g " +
           "WHERE g.grandPrix.id = :grandPrixId AND g.calculated = true " +
           "GROUP BY g.user.id, g.user.name " +
           "ORDER BY totalScore DESC " +
           "LIMIT 1")
    List<Object[]> getGrandPrixTopPerformer(@Param("grandPrixId") Long grandPrixId);
    
    // Buscar ranking detalhado da temporada com histórico por evento
    @Query("SELECT g.user.id, g.user.name, g.user.email, " +
           "COALESCE(SUM(g.score), 0) as totalScore, " +
           "COALESCE(SUM(CASE WHEN g.guessType = 'QUALIFYING' THEN g.score ELSE 0 END), 0) as qualifyingScore, " +
           "COALESCE(SUM(CASE WHEN g.guessType = 'RACE' THEN g.score ELSE 0 END), 0) as raceScore, " +
           "COUNT(g) as totalGuesses, " +
           "COUNT(CASE WHEN g.guessType = 'QUALIFYING' THEN 1 END) as qualifyingGuesses, " +
           "COUNT(CASE WHEN g.guessType = 'RACE' THEN 1 END) as raceGuesses, " +
           "COALESCE(AVG(g.score), 0) as averageScore, " +
           "COALESCE(MAX(g.score), 0) as bestEventScore, " +
           "COUNT(DISTINCT g.grandPrix.id) as eventsParticipated " +
           "FROM Guess g " +
           "WHERE g.grandPrix.season = :season AND g.calculated = true " +
           "GROUP BY g.user.id, g.user.name, g.user.email " +
           "ORDER BY totalScore DESC")
    List<Object[]> getSeasonDetailedRanking(@Param("season") Integer season);
    
    // Buscar histórico de eventos de um usuário em uma temporada
    @Query("SELECT gp.id, gp.name, gp.country, gp.round, " +
           "COALESCE(SUM(CASE WHEN g.guessType = 'QUALIFYING' THEN g.score ELSE 0 END), 0) as qualifyingScore, " +
           "COALESCE(SUM(CASE WHEN g.guessType = 'RACE' THEN g.score ELSE 0 END), 0) as raceScore, " +
           "COALESCE(SUM(g.score), 0) as totalScore, " +
           "COUNT(CASE WHEN g.guessType = 'QUALIFYING' THEN 1 END) > 0 as hasQualifyingGuess, " +
           "COUNT(CASE WHEN g.guessType = 'RACE' THEN 1 END) > 0 as hasRaceGuess " +
           "FROM GrandPrix gp " +
           "LEFT JOIN Guess g ON g.grandPrix.id = gp.id AND g.user.id = :userId AND g.calculated = true " +
           "WHERE gp.season = :season " +
           "GROUP BY gp.id, gp.name, gp.country, gp.round " +
           "ORDER BY gp.round")
    List<Object[]> getUserSeasonEventHistory(@Param("userId") Long userId, @Param("season") Integer season);
    
    // Buscar estatísticas da temporada
    @Query("SELECT " +
           "COUNT(DISTINCT g.user.id) as totalParticipants, " +
           "COUNT(DISTINCT g.grandPrix.id) as totalEvents, " +
           "COUNT(DISTINCT CASE WHEN g.grandPrix.completed = true THEN g.grandPrix.id END) as completedEvents, " +
           "COALESCE(AVG(g.score), 0) as averageScore, " +
           "COALESCE(MAX(g.score), 0) as highestEventScore " +
           "FROM Guess g " +
           "WHERE g.grandPrix.season = :season AND g.calculated = true")
    List<Object[]> getSeasonStatistics(@Param("season") Integer season);
    
    // Buscar participante mais ativo da temporada
    @Query("SELECT g.user.name, COUNT(g) as totalGuesses " +
           "FROM Guess g " +
           "WHERE g.grandPrix.season = :season " +
           "GROUP BY g.user.id, g.user.name " +
           "ORDER BY totalGuesses DESC " +
           "LIMIT 1")
    List<Object[]> getSeasonMostActiveParticipant(@Param("season") Integer season);
    
    // Buscar melhor performer da temporada
    @Query("SELECT g.user.name, COALESCE(SUM(g.score), 0) as totalScore " +
           "FROM Guess g " +
           "WHERE g.grandPrix.season = :season AND g.calculated = true " +
           "GROUP BY g.user.id, g.user.name " +
           "ORDER BY totalScore DESC " +
           "LIMIT 1")
    List<Object[]> getSeasonTopPerformer(@Param("season") Integer season);
    
    // Buscar nome do GP com melhor pontuação de um usuário
    @Query("SELECT gp.name " +
           "FROM Guess g JOIN g.grandPrix gp " +
           "WHERE g.user.id = :userId AND g.grandPrix.season = :season AND g.calculated = true " +
           "ORDER BY g.score DESC " +
           "LIMIT 1")
    List<String> getUserBestEventName(@Param("userId") Long userId, @Param("season") Integer season);
} 