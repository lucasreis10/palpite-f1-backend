package com.lucasreis.palpitef1backend.domain.guess;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
    List<Guess> findByGrandPrixIdAndGuessTypeAndCalculatedTrue(@Param("grandPrixId") Long grandPrixId, @Param("guessType") String guessType);
} 