package com.lucasreis.palpitef1backend.domain.guess;

import com.lucasreis.palpitef1backend.domain.grandprix.GrandPrix;
import com.lucasreis.palpitef1backend.domain.grandprix.GrandPrixRepository;
import com.lucasreis.palpitef1backend.domain.guess.scoring.QualifyingScoreCalculator;
import com.lucasreis.palpitef1backend.domain.guess.scoring.RaceScoreCalculator;
import com.lucasreis.palpitef1backend.domain.pilot.Pilot;
import com.lucasreis.palpitef1backend.domain.pilot.PilotRepository;
import com.lucasreis.palpitef1backend.domain.pilot.PilotResponse;
import com.lucasreis.palpitef1backend.domain.user.User;
import com.lucasreis.palpitef1backend.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GuessService {
    
    private final GuessRepository guessRepository;
    private final UserRepository userRepository;
    private final GrandPrixRepository grandPrixRepository;
    private final PilotRepository pilotRepository;
    
    @Transactional
    public GuessResponse createGuess(Long userId, CreateGuessRequest request) {
        log.debug("Criando palpite para usuário {} no GP {} tipo {}", userId, request.getGrandPrixId(), request.getGuessType());
        
        // Validações
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + userId));
        
        GrandPrix grandPrix = grandPrixRepository.findById(request.getGrandPrixId())
                .orElseThrow(() -> new RuntimeException("Grande Prêmio não encontrado com ID: " + request.getGrandPrixId()));
        
        // Verificar se usuário já tem palpite para este GP e tipo
        if (guessRepository.existsByUserIdAndGrandPrixIdAndGuessType(userId, request.getGrandPrixId(), request.getGuessType())) {
            throw new RuntimeException("Usuário já possui palpite para este Grande Prêmio e tipo");
        }
        
        // Validar se todos os pilotos existem e estão ativos
        validatePilots(request.getPilotIds());
        
        // Criar palpite
        Guess guess = Guess.builder()
                .user(user)
                .grandPrix(grandPrix)
                .guessType(request.getGuessType())
                .pilotIds(new ArrayList<>(request.getPilotIds()))
                .calculated(false)
                .active(true)
                .build();
        
        Guess savedGuess = guessRepository.save(guess);
        log.debug("Palpite criado com sucesso: {}", savedGuess.getId());
        
        return buildGuessResponse(savedGuess);
    }
    
    @Transactional
    public GuessResponse updateGuess(Long userId, Long guessId, UpdateGuessRequest request) {
        log.debug("Atualizando palpite {} do usuário {}", guessId, userId);
        
        Guess guess = guessRepository.findById(guessId)
                .orElseThrow(() -> new RuntimeException("Palpite não encontrado com ID: " + guessId));
        
        // Verificar se o palpite pertence ao usuário
        if (!guess.getUser().getId().equals(userId)) {
            throw new RuntimeException("Palpite não pertence ao usuário");
        }
        
        // Verificar se o palpite já foi calculado
        if (guess.isCalculated()) {
            throw new RuntimeException("Não é possível alterar palpite já calculado");
        }
        
        // Validar pilotos
        validatePilots(request.getPilotIds());
        
        // Atualizar palpite
        guess.setPilotIds(new ArrayList<>(request.getPilotIds()));
        
        Guess updatedGuess = guessRepository.save(guess);
        log.debug("Palpite atualizado com sucesso: {}", updatedGuess.getId());
        
        return buildGuessResponse(updatedGuess);
    }
    
    public GuessResponse getGuessById(Long guessId) {
        log.debug("Buscando palpite por ID: {}", guessId);
        
        Guess guess = guessRepository.findById(guessId)
                .orElseThrow(() -> new RuntimeException("Palpite não encontrado com ID: " + guessId));
        
        return buildGuessResponse(guess);
    }
    
    public GuessResponse getUserGuessForGrandPrix(Long userId, Long grandPrixId, GuessType guessType) {
        log.debug("Buscando palpite do usuário {} para GP {} tipo {}", userId, grandPrixId, guessType);
        
        Optional<Guess> guessOptional = guessRepository.findByUserIdAndGrandPrixIdAndGuessType(userId, grandPrixId, guessType);
        
        if (guessOptional.isEmpty()) {
            log.debug("Palpite não encontrado para usuário {} GP {} tipo {}", userId, grandPrixId, guessType);
            return null;
        }
        
        return buildGuessResponse(guessOptional.get());
    }
    
    public List<GuessResponse> getUserGuesses(Long userId) {
        log.debug("Buscando todos os palpites do usuário: {}", userId);
        
        List<Guess> guesses = guessRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return guesses.stream()
                .map(this::buildGuessResponse)
                .collect(Collectors.toList());
    }
    
    public List<GuessResponse> getUserGuessesBySeason(Long userId, Integer season) {
        log.debug("Buscando palpites do usuário {} na temporada {}", userId, season);
        
        List<Guess> guesses = guessRepository.findByUserIdAndSeason(userId, season);
        return guesses.stream()
                .map(this::buildGuessResponse)
                .collect(Collectors.toList());
    }
    
    public List<GuessResponse> getGrandPrixGuesses(Long grandPrixId, GuessType guessType) {
        log.debug("Buscando palpites do GP {} tipo {}", grandPrixId, guessType);
        
        List<Guess> guesses = guessRepository.findByGrandPrixIdAndGuessTypeOrderByCreatedAt(grandPrixId, guessType);
        return guesses.stream()
                .map(this::buildGuessResponse)
                .collect(Collectors.toList());
    }
    
    public List<GuessResponse> getGrandPrixRanking(Long grandPrixId, GuessType guessType) {
        log.debug("Buscando ranking do GP {} tipo {}", grandPrixId, guessType);
        
        List<Guess> guesses = guessRepository.findByGrandPrixIdAndGuessTypeCalculatedOrderByScore(grandPrixId, guessType);
        return guesses.stream()
                .map(this::buildGuessResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public CalculateScoresResponse setRealResultAndCalculateScores(SetRealResultRequest request) {
        log.debug("Definindo resultado real e calculando pontuações para GP {} tipo {}", 
                request.getGrandPrixId(), request.getGuessType());
        
        GrandPrix grandPrix = grandPrixRepository.findById(request.getGrandPrixId())
                .orElseThrow(() -> new RuntimeException("Grande Prêmio não encontrado com ID: " + request.getGrandPrixId()));
        
        // Validar pilotos do resultado real
        validatePilots(request.getRealResultPilotIds());
        
        // Buscar todos os palpites não calculados para este GP e tipo
        List<Guess> guesses = guessRepository.findByGrandPrixIdAndGuessTypeAndCalculatedFalse(
                request.getGrandPrixId(), request.getGuessType());
        
        if (guesses.isEmpty()) {
            throw new RuntimeException("Nenhum palpite encontrado para calcular");
        }
        
        List<GuessResponse> calculatedGuesses = new ArrayList<>();
        
        // Calcular pontuação para cada palpite
        for (Guess guess : guesses) {
            try {
                // Definir resultado real
                guess.setRealResultPilotIds(new ArrayList<>(request.getRealResultPilotIds()));
                
                // Calcular pontuação
                BigDecimal score = calculateScore(guess.getPilotIds(), request.getRealResultPilotIds(), request.getGuessType());
                guess.setScore(score);
                guess.setCalculated(true);
                
                Guess savedGuess = guessRepository.save(guess);
                calculatedGuesses.add(buildGuessResponse(savedGuess));
                
                log.debug("Pontuação calculada para palpite {}: {}", guess.getId(), score);
                
            } catch (Exception e) {
                log.error("Erro ao calcular pontuação para palpite {}: {}", guess.getId(), e.getMessage());
            }
        }
        
        // Ordenar por pontuação (maior primeiro)
        calculatedGuesses.sort((a, b) -> b.getScore().compareTo(a.getScore()));
        
        return CalculateScoresResponse.builder()
                .grandPrixId(request.getGrandPrixId())
                .grandPrixName(grandPrix.getName())
                .guessType(request.getGuessType())
                .totalGuesses(guesses.size())
                .calculatedGuesses(calculatedGuesses.size())
                .results(calculatedGuesses)
                .message("Pontuações calculadas com sucesso")
                .build();
    }
    
    @Transactional
    public void deleteGuess(Long userId, Long guessId) {
        log.debug("Deletando palpite {} do usuário {}", guessId, userId);
        
        Guess guess = guessRepository.findById(guessId)
                .orElseThrow(() -> new RuntimeException("Palpite não encontrado com ID: " + guessId));
        
        // Verificar se o palpite pertence ao usuário
        if (!guess.getUser().getId().equals(userId)) {
            throw new RuntimeException("Palpite não pertence ao usuário");
        }
        
        // Verificar se o palpite já foi calculado
        if (guess.isCalculated()) {
            throw new RuntimeException("Não é possível deletar palpite já calculado");
        }
        
        guessRepository.delete(guess);
        log.debug("Palpite deletado com sucesso: {}", guessId);
    }
    
    private void validatePilots(List<Long> pilotIds) {
        // Verificar se todos os pilotos existem
        List<Pilot> pilots = pilotRepository.findAllById(pilotIds);
        if (pilots.size() != pilotIds.size()) {
            throw new RuntimeException("Um ou mais pilotos não foram encontrados");
        }
        
        // Verificar se todos os pilotos estão ativos
        List<Pilot> inactivePilots = pilots.stream()
                .filter(pilot -> !pilot.getActive())
                .collect(Collectors.toList());
        
        if (!inactivePilots.isEmpty()) {
            String inactiveNames = inactivePilots.stream()
                    .map(pilot -> pilot.getGivenName() + " " + pilot.getFamilyName())
                    .collect(Collectors.joining(", "));
            throw new RuntimeException("Os seguintes pilotos estão inativos: " + inactiveNames);
        }
        
        // Verificar se não há pilotos duplicados
        long uniquePilots = pilotIds.stream().distinct().count();
        if (uniquePilots != pilotIds.size()) {
            throw new RuntimeException("Lista de pilotos contém duplicatas");
        }
    }
    
    private BigDecimal calculateScore(List<Long> guessPilotIds, List<Long> realResultPilotIds, GuessType guessType) {
        if (guessType == GuessType.QUALIFYING) {
            QualifyingScoreCalculator calculator = new QualifyingScoreCalculator(realResultPilotIds, guessPilotIds);
            return calculator.calculate();
        } else if (guessType == GuessType.RACE) {
            RaceScoreCalculator calculator = new RaceScoreCalculator(realResultPilotIds, guessPilotIds);
            return calculator.calculate();
        } else {
            throw new RuntimeException("Tipo de palpite não suportado: " + guessType);
        }
    }
    
    private GuessResponse buildGuessResponse(Guess guess) {
        // Buscar informações dos pilotos do palpite
        List<PilotResponse> pilots = getPilotResponses(guess.getPilotIds());
        
        // Buscar informações dos pilotos do resultado real (se disponível)
        List<PilotResponse> realResultPilots = null;
        if (guess.hasRealResult()) {
            realResultPilots = getPilotResponses(guess.getRealResultPilotIds());
        }
        
        return GuessResponse.fromGuess(guess, pilots, realResultPilots);
    }
    
    private List<PilotResponse> getPilotResponses(List<Long> pilotIds) {
        if (pilotIds == null || pilotIds.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<Pilot> pilots = pilotRepository.findAllById(pilotIds);
        
        // Manter a ordem original dos IDs
        return pilotIds.stream()
                .map(id -> pilots.stream()
                        .filter(pilot -> pilot.getId().equals(id))
                        .findFirst()
                        .map(PilotResponse::fromPilot)
                        .orElse(null))
                .filter(pilot -> pilot != null)
                .collect(Collectors.toList());
    }
}