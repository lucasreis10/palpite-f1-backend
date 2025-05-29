package com.lucasreis.palpitef1backend.domain.guess;

import com.lucasreis.palpitef1backend.domain.grandprix.GrandPrix;
import com.lucasreis.palpitef1backend.domain.grandprix.GrandPrixRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class HistoryService {
    
    private final GuessRepository guessRepository;
    private final GrandPrixRepository grandPrixRepository;
    
    public GrandPrixHistoryResponse getGrandPrixHistory(Long grandPrixId) {
        log.debug("Buscando histórico completo do GP {}", grandPrixId);
        
        // Buscar informações do GP
        GrandPrix grandPrix = grandPrixRepository.findById(grandPrixId)
                .orElseThrow(() -> new RuntimeException("Grande Prêmio não encontrado com ID: " + grandPrixId));
        
        // Buscar rankings por tipo
        List<GrandPrixHistoryResponse.ParticipantRanking> qualifyingRanking = buildQualifyingRanking(grandPrixId);
        List<GrandPrixHistoryResponse.ParticipantRanking> raceRanking = buildRaceRanking(grandPrixId);
        List<GrandPrixHistoryResponse.ParticipantRanking> combinedRanking = buildCombinedRanking(grandPrixId);
        
        // Buscar estatísticas
        GrandPrixHistoryResponse.EventStatistics statistics = buildEventStatistics(grandPrixId);
        
        return GrandPrixHistoryResponse.builder()
                .grandPrixId(grandPrix.getId())
                .grandPrixName(grandPrix.getName())
                .country(grandPrix.getCountry())
                .season(grandPrix.getSeason())
                .round(grandPrix.getRound())
                .raceDate(grandPrix.getRaceDateTime())
                .completed(grandPrix.getCompleted())
                .qualifyingRanking(qualifyingRanking)
                .raceRanking(raceRanking)
                .combinedRanking(combinedRanking)
                .statistics(statistics)
                .build();
    }
    
    public SeasonRankingResponse getSeasonRanking(Integer season) {
        log.debug("Buscando ranking completo da temporada {}", season);
        
        // Buscar ranking detalhado da temporada
        List<Object[]> rankingData = guessRepository.getSeasonDetailedRanking(season);
        List<SeasonRankingResponse.SeasonParticipant> participants = new ArrayList<>();
        
        AtomicInteger position = new AtomicInteger(1);
        
        for (Object[] row : rankingData) {
            Long userId = (Long) row[0];
            String userName = (String) row[1];
            String userEmail = (String) row[2];
            BigDecimal totalScore = row[3] instanceof Double ? 
                BigDecimal.valueOf((Double) row[3]) : (BigDecimal) row[3];
            BigDecimal qualifyingScore = row[4] instanceof Double ? 
                BigDecimal.valueOf((Double) row[4]) : (BigDecimal) row[4];
            BigDecimal raceScore = row[5] instanceof Double ? 
                BigDecimal.valueOf((Double) row[5]) : (BigDecimal) row[5];
            Long totalGuesses = (Long) row[6];
            Long qualifyingGuesses = (Long) row[7];
            Long raceGuesses = (Long) row[8];
            BigDecimal averageScore = row[9] instanceof Double ? 
                BigDecimal.valueOf((Double) row[9]) : (BigDecimal) row[9];
            BigDecimal bestEventScore = row[10] instanceof Double ? 
                BigDecimal.valueOf((Double) row[10]) : (BigDecimal) row[10];
            Long eventsParticipated = (Long) row[11];
            
            // Buscar nome do melhor evento
            List<String> bestEventNames = guessRepository.getUserBestEventName(userId, season);
            String bestEventName = bestEventNames.isEmpty() ? null : bestEventNames.get(0);
            
            // Buscar histórico de eventos
            List<SeasonRankingResponse.EventParticipation> eventHistory = buildUserEventHistory(userId, season);
            
            SeasonRankingResponse.SeasonParticipant participant = SeasonRankingResponse.SeasonParticipant.builder()
                    .position(position.getAndIncrement())
                    .userId(userId)
                    .userName(userName)
                    .userEmail(userEmail)
                    .totalScore(totalScore)
                    .qualifyingScore(qualifyingScore)
                    .raceScore(raceScore)
                    .totalGuesses(totalGuesses.intValue())
                    .qualifyingGuesses(qualifyingGuesses.intValue())
                    .raceGuesses(raceGuesses.intValue())
                    .averageScore(averageScore)
                    .bestEventScore(bestEventScore)
                    .bestEventName(bestEventName)
                    .eventsParticipated(eventsParticipated.intValue())
                    .eventHistory(eventHistory)
                    .build();
            
            participants.add(participant);
        }
        
        // Buscar estatísticas da temporada
        SeasonRankingResponse.SeasonStatistics statistics = buildSeasonStatistics(season);
        
        return SeasonRankingResponse.builder()
                .season(season)
                .ranking(participants)
                .statistics(statistics)
                .build();
    }
    
    private List<GrandPrixHistoryResponse.ParticipantRanking> buildQualifyingRanking(Long grandPrixId) {
        List<Guess> qualifyingGuesses = guessRepository.findByGrandPrixIdAndGuessTypeCalculatedOrderByScore(grandPrixId, GuessType.QUALIFYING);
        List<GrandPrixHistoryResponse.ParticipantRanking> ranking = new ArrayList<>();
        
        AtomicInteger position = new AtomicInteger(1);
        
        for (Guess guess : qualifyingGuesses) {
            GrandPrixHistoryResponse.ParticipantRanking participant = GrandPrixHistoryResponse.ParticipantRanking.builder()
                    .position(position.getAndIncrement())
                    .userId(guess.getUser().getId())
                    .userName(guess.getUser().getName())
                    .userEmail(guess.getUser().getEmail())
                    .score(guess.getScore())
                    .qualifyingScore(guess.getScore())
                    .raceScore(BigDecimal.ZERO)
                    .hasQualifyingGuess(true)
                    .hasRaceGuess(false)
                    .totalGuesses(1)
                    .build();
            
            ranking.add(participant);
        }
        
        return ranking;
    }
    
    private List<GrandPrixHistoryResponse.ParticipantRanking> buildRaceRanking(Long grandPrixId) {
        List<Guess> raceGuesses = guessRepository.findByGrandPrixIdAndGuessTypeCalculatedOrderByScore(grandPrixId, GuessType.RACE);
        List<GrandPrixHistoryResponse.ParticipantRanking> ranking = new ArrayList<>();
        
        AtomicInteger position = new AtomicInteger(1);
        
        for (Guess guess : raceGuesses) {
            GrandPrixHistoryResponse.ParticipantRanking participant = GrandPrixHistoryResponse.ParticipantRanking.builder()
                    .position(position.getAndIncrement())
                    .userId(guess.getUser().getId())
                    .userName(guess.getUser().getName())
                    .userEmail(guess.getUser().getEmail())
                    .score(guess.getScore())
                    .qualifyingScore(BigDecimal.ZERO)
                    .raceScore(guess.getScore())
                    .hasQualifyingGuess(false)
                    .hasRaceGuess(true)
                    .totalGuesses(1)
                    .build();
            
            ranking.add(participant);
        }
        
        return ranking;
    }
    
    private List<GrandPrixHistoryResponse.ParticipantRanking> buildCombinedRanking(Long grandPrixId) {
        List<Object[]> combinedData = guessRepository.getGrandPrixCombinedRanking(grandPrixId);
        List<GrandPrixHistoryResponse.ParticipantRanking> ranking = new ArrayList<>();
        
        AtomicInteger position = new AtomicInteger(1);
        
        for (Object[] row : combinedData) {
            Long userId = (Long) row[0];
            String userName = (String) row[1];
            String userEmail = (String) row[2];
            BigDecimal qualifyingScore = row[3] instanceof Double ? 
                BigDecimal.valueOf((Double) row[3]) : (BigDecimal) row[3];
            BigDecimal raceScore = row[4] instanceof Double ? 
                BigDecimal.valueOf((Double) row[4]) : (BigDecimal) row[4];
            BigDecimal totalScore = row[5] instanceof Double ? 
                BigDecimal.valueOf((Double) row[5]) : (BigDecimal) row[5];
            Long qualifyingGuesses = (Long) row[6];
            Long raceGuesses = (Long) row[7];
            Long totalGuesses = (Long) row[8];
            
            GrandPrixHistoryResponse.ParticipantRanking participant = GrandPrixHistoryResponse.ParticipantRanking.builder()
                    .position(position.getAndIncrement())
                    .userId(userId)
                    .userName(userName)
                    .userEmail(userEmail)
                    .score(totalScore)
                    .qualifyingScore(qualifyingScore)
                    .raceScore(raceScore)
                    .hasQualifyingGuess(qualifyingGuesses > 0)
                    .hasRaceGuess(raceGuesses > 0)
                    .totalGuesses(totalGuesses.intValue())
                    .build();
            
            ranking.add(participant);
        }
        
        return ranking;
    }
    
    private GrandPrixHistoryResponse.EventStatistics buildEventStatistics(Long grandPrixId) {
        List<Object[]> statsData = guessRepository.getGrandPrixStatistics(grandPrixId);
        
        if (statsData.isEmpty()) {
            return GrandPrixHistoryResponse.EventStatistics.builder()
                    .totalParticipants(0)
                    .qualifyingParticipants(0)
                    .raceParticipants(0)
                    .averageQualifyingScore(BigDecimal.ZERO)
                    .averageRaceScore(BigDecimal.ZERO)
                    .averageCombinedScore(BigDecimal.ZERO)
                    .highestQualifyingScore(BigDecimal.ZERO)
                    .highestRaceScore(BigDecimal.ZERO)
                    .highestCombinedScore(BigDecimal.ZERO)
                    .topPerformerName("Nenhum")
                    .topPerformerScore(BigDecimal.ZERO)
                    .build();
        }
        
        Object[] row = statsData.get(0);
        Long totalParticipants = (Long) row[0];
        Long qualifyingParticipants = (Long) row[1];
        Long raceParticipants = (Long) row[2];
        BigDecimal avgQualifyingScore = row[3] instanceof Double ? 
            BigDecimal.valueOf((Double) row[3]) : (BigDecimal) row[3];
        BigDecimal avgRaceScore = row[4] instanceof Double ? 
            BigDecimal.valueOf((Double) row[4]) : (BigDecimal) row[4];
        BigDecimal avgCombinedScore = row[5] instanceof Double ? 
            BigDecimal.valueOf((Double) row[5]) : (BigDecimal) row[5];
        BigDecimal maxQualifyingScore = row[6] instanceof Double ? 
            BigDecimal.valueOf((Double) row[6]) : (BigDecimal) row[6];
        BigDecimal maxRaceScore = row[7] instanceof Double ? 
            BigDecimal.valueOf((Double) row[7]) : (BigDecimal) row[7];
        BigDecimal maxCombinedScore = row[8] instanceof Double ? 
            BigDecimal.valueOf((Double) row[8]) : (BigDecimal) row[8];
        
        // Buscar melhor performer
        List<Object[]> topPerformerData = guessRepository.getGrandPrixTopPerformer(grandPrixId);
        String topPerformerName = "Nenhum";
        BigDecimal topPerformerScore = BigDecimal.ZERO;
        
        if (!topPerformerData.isEmpty()) {
            Object[] topPerformerRow = topPerformerData.get(0);
            topPerformerName = (String) topPerformerRow[0];
            topPerformerScore = topPerformerRow[1] instanceof Double ? 
                BigDecimal.valueOf((Double) topPerformerRow[1]) : (BigDecimal) topPerformerRow[1];
        }
        
        return GrandPrixHistoryResponse.EventStatistics.builder()
                .totalParticipants(totalParticipants.intValue())
                .qualifyingParticipants(qualifyingParticipants.intValue())
                .raceParticipants(raceParticipants.intValue())
                .averageQualifyingScore(avgQualifyingScore.setScale(3, RoundingMode.HALF_UP))
                .averageRaceScore(avgRaceScore.setScale(3, RoundingMode.HALF_UP))
                .averageCombinedScore(avgCombinedScore.setScale(3, RoundingMode.HALF_UP))
                .highestQualifyingScore(maxQualifyingScore)
                .highestRaceScore(maxRaceScore)
                .highestCombinedScore(maxCombinedScore)
                .topPerformerName(topPerformerName)
                .topPerformerScore(topPerformerScore)
                .build();
    }
    
    private List<SeasonRankingResponse.EventParticipation> buildUserEventHistory(Long userId, Integer season) {
        List<Object[]> eventData = guessRepository.getUserSeasonEventHistory(userId, season);
        List<SeasonRankingResponse.EventParticipation> eventHistory = new ArrayList<>();
        
        for (Object[] row : eventData) {
            Long grandPrixId = (Long) row[0];
            String grandPrixName = (String) row[1];
            String country = (String) row[2];
            Integer round = (Integer) row[3];
            BigDecimal qualifyingScore = row[4] instanceof Double ? 
                BigDecimal.valueOf((Double) row[4]) : (BigDecimal) row[4];
            BigDecimal raceScore = row[5] instanceof Double ? 
                BigDecimal.valueOf((Double) row[5]) : (BigDecimal) row[5];
            BigDecimal totalScore = row[6] instanceof Double ? 
                BigDecimal.valueOf((Double) row[6]) : (BigDecimal) row[6];
            Boolean hasQualifyingGuess = (Boolean) row[7];
            Boolean hasRaceGuess = (Boolean) row[8];
            
            // TODO: Calcular posições no ranking (seria necessário uma query mais complexa)
            // Por enquanto, deixamos como null
            
            SeasonRankingResponse.EventParticipation participation = SeasonRankingResponse.EventParticipation.builder()
                    .grandPrixId(grandPrixId)
                    .grandPrixName(grandPrixName)
                    .country(country)
                    .round(round)
                    .qualifyingScore(qualifyingScore)
                    .raceScore(raceScore)
                    .totalScore(totalScore)
                    .qualifyingPosition(null) // TODO: Implementar cálculo de posição
                    .racePosition(null) // TODO: Implementar cálculo de posição
                    .combinedPosition(null) // TODO: Implementar cálculo de posição
                    .hasQualifyingGuess(hasQualifyingGuess)
                    .hasRaceGuess(hasRaceGuess)
                    .build();
            
            eventHistory.add(participation);
        }
        
        return eventHistory;
    }
    
    private SeasonRankingResponse.SeasonStatistics buildSeasonStatistics(Integer season) {
        List<Object[]> statsData = guessRepository.getSeasonStatistics(season);
        
        if (statsData.isEmpty()) {
            return SeasonRankingResponse.SeasonStatistics.builder()
                    .totalParticipants(0)
                    .totalEvents(0)
                    .completedEvents(0)
                    .averageParticipationRate(BigDecimal.ZERO)
                    .averageScore(BigDecimal.ZERO)
                    .highestEventScore(BigDecimal.ZERO)
                    .mostActiveParticipant("Nenhum")
                    .mostActiveParticipantGuesses(0)
                    .topPerformer("Nenhum")
                    .topPerformerScore(BigDecimal.ZERO)
                    .build();
        }
        
        Object[] row = statsData.get(0);
        Long totalParticipants = (Long) row[0];
        Long totalEvents = (Long) row[1];
        Long completedEvents = (Long) row[2];
        BigDecimal averageScore = row[3] instanceof Double ? 
            BigDecimal.valueOf((Double) row[3]) : (BigDecimal) row[3];
        BigDecimal highestEventScore = row[4] instanceof Double ? 
            BigDecimal.valueOf((Double) row[4]) : (BigDecimal) row[4];
        
        // Calcular taxa de participação média
        BigDecimal averageParticipationRate = totalEvents > 0 
                ? BigDecimal.valueOf(totalParticipants).divide(BigDecimal.valueOf(totalEvents), 3, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
        
        // Buscar participante mais ativo
        List<Object[]> mostActiveData = guessRepository.getSeasonMostActiveParticipant(season);
        String mostActiveParticipant = "Nenhum";
        Integer mostActiveParticipantGuesses = 0;
        
        if (!mostActiveData.isEmpty()) {
            Object[] mostActiveRow = mostActiveData.get(0);
            mostActiveParticipant = (String) mostActiveRow[0];
            mostActiveParticipantGuesses = ((Long) mostActiveRow[1]).intValue();
        }
        
        // Buscar melhor performer
        List<Object[]> topPerformerData = guessRepository.getSeasonTopPerformer(season);
        String topPerformer = "Nenhum";
        BigDecimal topPerformerScore = BigDecimal.ZERO;
        
        if (!topPerformerData.isEmpty()) {
            Object[] topPerformerRow = topPerformerData.get(0);
            topPerformer = (String) topPerformerRow[0];
            topPerformerScore = topPerformerRow[1] instanceof Double ? 
                BigDecimal.valueOf((Double) topPerformerRow[1]) : (BigDecimal) topPerformerRow[1];
        }
        
        return SeasonRankingResponse.SeasonStatistics.builder()
                .totalParticipants(totalParticipants.intValue())
                .totalEvents(totalEvents.intValue())
                .completedEvents(completedEvents.intValue())
                .averageParticipationRate(averageParticipationRate)
                .averageScore(averageScore.setScale(3, RoundingMode.HALF_UP))
                .highestEventScore(highestEventScore)
                .mostActiveParticipant(mostActiveParticipant)
                .mostActiveParticipantGuesses(mostActiveParticipantGuesses)
                .topPerformer(topPerformer)
                .topPerformerScore(topPerformerScore)
                .build();
    }
} 