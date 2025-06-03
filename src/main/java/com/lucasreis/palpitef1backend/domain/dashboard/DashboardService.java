package com.lucasreis.palpitef1backend.domain.dashboard;

import com.lucasreis.palpitef1backend.domain.grandprix.GrandPrix;
import com.lucasreis.palpitef1backend.domain.grandprix.GrandPrixRepository;
import com.lucasreis.palpitef1backend.domain.guess.Guess;
import com.lucasreis.palpitef1backend.domain.guess.GuessRepository;
import com.lucasreis.palpitef1backend.domain.guess.GuessType;
import com.lucasreis.palpitef1backend.domain.pilot.Pilot;
import com.lucasreis.palpitef1backend.domain.pilot.PilotRepository;
import com.lucasreis.palpitef1backend.domain.team.Team;
import com.lucasreis.palpitef1backend.domain.team.TeamRepository;
import com.lucasreis.palpitef1backend.domain.user.User;
import com.lucasreis.palpitef1backend.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardService {
    
    private final GuessRepository guessRepository;
    private final UserRepository userRepository;
    private final GrandPrixRepository grandPrixRepository;
    private final PilotRepository pilotRepository;
    private final TeamRepository teamRepository;
    
    public DashboardStatsResponse getDashboardStats() {
        log.debug("Calculando estatísticas da dashboard");
        
        // Total de palpites
        long totalGuesses = guessRepository.count();
        
        // Total de usuários
        long totalUsers = userRepository.count();
        
        // Total de corridas (GPs ativos)
        long totalRaces = grandPrixRepository.countByActiveTrue();
        
        // Média de palpites por corrida
        BigDecimal averageGuessesPerRace = totalRaces > 0 ? 
            BigDecimal.valueOf(totalGuesses).divide(BigDecimal.valueOf(totalRaces), 2, RoundingMode.HALF_UP) : 
            BigDecimal.ZERO;
        
        // Buscar melhor pontuação real do banco de dados
        BestScoreInfo bestScore = getBestScoreFromDatabase();
        
        return new DashboardStatsResponse(
            bestScore,
            totalGuesses,
            averageGuessesPerRace,
            totalUsers,
            totalRaces
        );
    }
    
    public List<TopUserResponse> getTopUsers(Integer limit, Integer season) {
        log.debug("Buscando top {} usuários da temporada {}", limit, season);
        
        // Buscar ranking real baseado nos palpites calculados
        return getTopUsersFromDatabase(limit, season);
    }
    
    public LastResultResponse getLastResult() {
        log.debug("Buscando último resultado");
        
        // Buscar o GP mais recente que foi concluído
        List<GrandPrix> recentCompleted = grandPrixRepository.findByCompletedTrueOrderByRaceDateTimeDesc();
        
        if (recentCompleted.isEmpty()) {
            return new LastResultResponse("Nenhum resultado disponível", List.of(), List.of(), List.of(), List.of());
        }
        
        GrandPrix lastGP = recentCompleted.get(0);
        
        // Buscar resultados reais do GP do banco de dados
        List<PilotResultInfo> qualifyingResults = getLastResultFromDatabase(lastGP.getId(), GuessType.QUALIFYING);
        List<PilotResultInfo> raceResults = getLastResultFromDatabase(lastGP.getId(), GuessType.RACE);
        
        // Buscar palpites dos usuários com pontuações
        List<LastResultResponse.ParticipantGuess> qualifyingGuesses = getUserGuesses(lastGP.getId(), GuessType.QUALIFYING);
        List<LastResultResponse.ParticipantGuess> raceGuesses = getUserGuesses(lastGP.getId(), GuessType.RACE);
        
        return new LastResultResponse(lastGP.getName(), qualifyingResults, raceResults, qualifyingGuesses, raceGuesses);
    }

    private BestScoreInfo getBestScoreFromDatabase() {
        try {
            // Buscar o palpite com maior pontuação
            List<Guess> topGuesses = guessRepository.findTop1ByCalculatedTrueOrderByScoreDesc();
            
            if (topGuesses.isEmpty()) {
                return new BestScoreInfo(BigDecimal.ZERO, "Nenhum palpite", "Nenhum GP");
            }
            
            Guess bestGuess = topGuesses.get(0);
            User user = bestGuess.getUser();
            GrandPrix grandPrix = bestGuess.getGrandPrix();
            
            String userName = user != null ? user.getName() : "Usuário desconhecido";
            String grandPrixName = grandPrix != null ? grandPrix.getName() : "GP desconhecido";
            
            return new BestScoreInfo(bestGuess.getScore(), userName, grandPrixName);
        } catch (Exception e) {
            log.error("Erro ao buscar melhor pontuação", e);
            return new BestScoreInfo(BigDecimal.ZERO, "Erro", "Erro");
        }
    }
    
    private List<TopUserResponse> getTopUsersFromDatabase(Integer limit, Integer season) {
        try {
            // Buscar todos os usuários que fizeram palpites na temporada
            List<Guess> seasonGuesses = guessRepository.findBySeasonAndCalculatedTrue(season);
            
            if (seasonGuesses.isEmpty()) {
                return new ArrayList<>();
            }
            
            // Agrupar por usuário e somar pontuações
            Map<User, BigDecimal> userScores = seasonGuesses.stream()
                .collect(Collectors.groupingBy(
                    Guess::getUser,
                    Collectors.reducing(BigDecimal.ZERO, Guess::getScore, BigDecimal::add)
                ));
            
            // Buscar todas as equipes ativas da temporada
            List<Team> activeTeams = teamRepository.findByYearAndActiveOrderByTotalScoreDesc(season, true);
            Map<Long, Team> userTeamMap = new HashMap<>();
            
            // Mapear usuários para suas equipes
            for (Team team : activeTeams) {
                userTeamMap.put(team.getUser1().getId(), team);
                userTeamMap.put(team.getUser2().getId(), team);
            }
            
            // Criar ranking
            List<TopUserResponse> ranking = userScores.entrySet().stream()
                .map(entry -> {
                    User user = entry.getKey();
                    Team userTeam = userTeamMap.get(user.getId());
                    
                    return TopUserResponse.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .totalScore(entry.getValue())
                        .position(0) // posição será definida depois
                        .teamName(userTeam != null ? userTeam.getName() : "Sem Equipe")
                        .teamId(userTeam != null ? userTeam.getId() : null)
                        .build();
                })
                .sorted((a, b) -> b.getTotalScore().compareTo(a.getTotalScore()))
                .limit(limit)
                .collect(Collectors.toList());
            
            // Definir posições
            for (int i = 0; i < ranking.size(); i++) {
                ranking.get(i).setPosition(i + 1);
            }
            
            return ranking;
        } catch (Exception e) {
            log.error("Erro ao buscar top usuários", e);
            return new ArrayList<>();
        }
    }
    
    private List<PilotResultInfo> getLastResultFromDatabase(Long grandPrixId, GuessType guessType) {
        try {
            // Buscar palpites que têm resultado real para este GP e tipo
            List<Guess> guessesWithResult = guessRepository.findByGrandPrixIdAndGuessTypeAndCalculatedTrue(
                grandPrixId, guessType);
            
            if (guessesWithResult.isEmpty()) {
                return new ArrayList<>();
            }
            
            // Pegar o primeiro palpite que tem resultado real (todos devem ter o mesmo resultado)
            Guess guessWithResult = guessesWithResult.get(0);
            List<Long> realResultPilotIds = guessWithResult.getRealResultPilotIds();
            
            if (realResultPilotIds == null || realResultPilotIds.isEmpty()) {
                return new ArrayList<>();
            }
            
            // Buscar informações dos pilotos
            List<Pilot> pilots = pilotRepository.findAllById(realResultPilotIds);
            Map<Long, Pilot> pilotMap = pilots.stream()
                .collect(Collectors.toMap(Pilot::getId, pilot -> pilot));
            
            // Criar lista de resultados mantendo a ordem
            List<PilotResultInfo> results = new ArrayList<>();
            for (int i = 0; i < realResultPilotIds.size(); i++) {
                Long pilotId = realResultPilotIds.get(i);
                Pilot pilot = pilotMap.get(pilotId);
                
                if (pilot != null) {
                    String teamName = pilot.getConstructor() != null ? 
                        pilot.getConstructor().getName() : "Sem equipe";
                    
                    results.add(new PilotResultInfo(
                        i + 1, // posição
                        pilot.getFullName(),
                        teamName
                    ));
                }
            }
            
            return results;
        } catch (Exception e) {
            log.error("Erro ao buscar último resultado", e);
            return new ArrayList<>();
        }
    }

    private List<LastResultResponse.ParticipantGuess> getUserGuesses(Long grandPrixId, GuessType guessType) {
        try {
            // Buscar palpites calculados ordenados por pontuação
            List<Guess> calculatedGuesses = guessRepository.findByGrandPrixIdAndGuessTypeCalculatedOrderByScore(
                grandPrixId, guessType);
            
            if (calculatedGuesses.isEmpty()) {
                return new ArrayList<>();
            }
            
            // Buscar todas as equipes ativas para mapear usuários
            GrandPrix grandPrix = grandPrixRepository.findById(grandPrixId).orElse(null);
            Integer season = grandPrix != null ? grandPrix.getSeason() : 2025;
            
            List<Team> activeTeams = teamRepository.findByYearAndActiveOrderByTotalScoreDesc(season, true);
            Map<Long, Team> userTeamMap = new HashMap<>();
            
            // Mapear usuários para suas equipes
            for (Team team : activeTeams) {
                if (team.getUser1() != null) {
                    userTeamMap.put(team.getUser1().getId(), team);
                }
                if (team.getUser2() != null) {
                    userTeamMap.put(team.getUser2().getId(), team);
                }
            }
            
            // Converter para ParticipantGuess
            List<LastResultResponse.ParticipantGuess> guesses = new ArrayList<>();
            for (int i = 0; i < calculatedGuesses.size(); i++) {
                Guess guess = calculatedGuesses.get(i);
                User user = guess.getUser();
                
                if (user != null) {
                    Team userTeam = userTeamMap.get(user.getId());
                    String teamName = userTeam != null ? userTeam.getName() : "Sem Equipe";
                    
                    guesses.add(new LastResultResponse.ParticipantGuess(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        teamName,
                        guess.getScore(),
                        i + 1, // posição no ranking
                        true // tem palpite
                    ));
                }
            }
            
            return guesses;
        } catch (Exception e) {
            log.error("Erro ao buscar palpites dos usuários", e);
            return new ArrayList<>();
        }
    }
} 