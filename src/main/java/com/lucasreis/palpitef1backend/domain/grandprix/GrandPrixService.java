package com.lucasreis.palpitef1backend.domain.grandprix;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GrandPrixService {
    
    private final GrandPrixRepository grandPrixRepository;
    
    public List<GrandPrixResponse> getAllGrandPrix() {
        log.debug("Buscando todos os Grandes Prêmios");
        return grandPrixRepository.findAll()
                .stream()
                .map(GrandPrixResponse::fromGrandPrix)
                .collect(Collectors.toList());
    }
    
    public List<GrandPrixResponse> getGrandPrixBySeason(Integer season) {
        log.debug("Buscando Grandes Prêmios da temporada: {}", season);
        return grandPrixRepository.findBySeasonOrderByRound(season)
                .stream()
                .map(GrandPrixResponse::fromGrandPrix)
                .collect(Collectors.toList());
    }
    
    public List<GrandPrixResponse> getActiveGrandPrixBySeason(Integer season) {
        log.debug("Buscando Grandes Prêmios ativos da temporada: {}", season);
        return grandPrixRepository.findBySeasonAndActiveOrderByRound(season, true)
                .stream()
                .map(GrandPrixResponse::fromGrandPrix)
                .collect(Collectors.toList());
    }
    
    public GrandPrixResponse getGrandPrixById(Long id) {
        log.debug("Buscando Grande Prêmio por ID: {}", id);
        GrandPrix grandPrix = grandPrixRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grande Prêmio não encontrado com ID: " + id));
        return GrandPrixResponse.fromGrandPrix(grandPrix);
    }
    
    public GrandPrixResponse getGrandPrixBySeasonAndRound(Integer season, Integer round) {
        log.debug("Buscando Grande Prêmio da temporada {} rodada {}", season, round);
        GrandPrix grandPrix = grandPrixRepository.findBySeasonAndRound(season, round)
                .orElseThrow(() -> new RuntimeException("Grande Prêmio não encontrado para temporada " + season + " rodada " + round));
        return GrandPrixResponse.fromGrandPrix(grandPrix);
    }
    
    public List<GrandPrixResponse> getGrandPrixByCountry(String country) {
        log.debug("Buscando Grandes Prêmios do país: {}", country);
        return grandPrixRepository.findByCountryIgnoreCaseOrderBySeasonDescRoundAsc(country)
                .stream()
                .map(GrandPrixResponse::fromGrandPrix)
                .collect(Collectors.toList());
    }
    
    public List<GrandPrixResponse> getUpcomingGrandPrix() {
        log.debug("Buscando próximos Grandes Prêmios");
        return grandPrixRepository.findUpcomingGrandPrix(LocalDateTime.now())
                .stream()
                .map(GrandPrixResponse::fromGrandPrix)
                .collect(Collectors.toList());
    }
    
    public GrandPrixResponse getNextGrandPrix() {
        log.debug("Buscando próximo Grande Prêmio");
        GrandPrix grandPrix = grandPrixRepository.findNextGrandPrix(LocalDateTime.now())
                .orElseThrow(() -> new RuntimeException("Nenhum Grande Prêmio futuro encontrado"));
        return GrandPrixResponse.fromGrandPrix(grandPrix);
    }
    
    public List<GrandPrixResponse> getCompletedGrandPrixBySeason(Integer season) {
        log.debug("Buscando Grandes Prêmios concluídos da temporada: {}", season);
        return grandPrixRepository.findBySeasonAndCompletedOrderByRound(season, true)
                .stream()
                .map(GrandPrixResponse::fromGrandPrix)
                .collect(Collectors.toList());
    }
    
    public List<GrandPrixResponse> getPendingGrandPrixBySeason(Integer season) {
        log.debug("Buscando Grandes Prêmios pendentes da temporada: {}", season);
        return grandPrixRepository.findBySeasonAndCompletedOrderByRound(season, false)
                .stream()
                .map(GrandPrixResponse::fromGrandPrix)
                .collect(Collectors.toList());
    }
    
    public List<GrandPrixResponse> getSprintWeekendsBySeason(Integer season) {
        log.debug("Buscando fins de semana de sprint da temporada: {}", season);
        return grandPrixRepository.findSprintWeekendsBySeason(season)
                .stream()
                .map(GrandPrixResponse::fromGrandPrix)
                .collect(Collectors.toList());
    }
    
    public List<GrandPrixResponse> searchByCircuitName(String circuitName) {
        log.debug("Buscando Grandes Prêmios por nome do circuito: {}", circuitName);
        return grandPrixRepository.findByCircuitNameContainingIgnoreCase(circuitName)
                .stream()
                .map(GrandPrixResponse::fromGrandPrix)
                .collect(Collectors.toList());
    }
    
    public List<GrandPrixResponse> getRecentCompletedGrandPrix() {
        log.debug("Buscando Grandes Prêmios recentemente concluídos");
        return grandPrixRepository.findRecentCompletedGrandPrix()
                .stream()
                .limit(10) // Limitar aos últimos 10
                .map(GrandPrixResponse::fromGrandPrix)
                .collect(Collectors.toList());
    }
    
    public List<Integer> getAvailableSeasons() {
        log.debug("Buscando temporadas disponíveis");
        return grandPrixRepository.findAvailableSeasons();
    }
    
    @Transactional
    public GrandPrixResponse createGrandPrix(CreateGrandPrixRequest request) {
        log.debug("Criando novo Grande Prêmio: {}", request.getName());
        
        // Validações
        validateCreateGrandPrixRequest(request);
        
        GrandPrix grandPrix = GrandPrix.builder()
                .season(request.getSeason())
                .round(request.getRound())
                .name(request.getName())
                .country(request.getCountry())
                .city(request.getCity())
                .circuitName(request.getCircuitName())
                .circuitUrl(request.getCircuitUrl())
                .practice1DateTime(request.getPractice1DateTime())
                .practice2DateTime(request.getPractice2DateTime())
                .practice3DateTime(request.getPractice3DateTime())
                .qualifyingDateTime(request.getQualifyingDateTime())
                .sprintDateTime(request.getSprintDateTime())
                .raceDateTime(request.getRaceDateTime())
                .timezone(request.getTimezone())
                .laps(request.getLaps())
                .circuitLength(request.getCircuitLength())
                .description(request.getDescription())
                .active(true)
                .completed(false)
                .build();
        
        GrandPrix savedGrandPrix = grandPrixRepository.save(grandPrix);
        log.debug("Grande Prêmio criado com sucesso: {}", savedGrandPrix.getId());
        
        return GrandPrixResponse.fromGrandPrix(savedGrandPrix);
    }
    
    @Transactional
    public CreateGrandPrixBatchResponse createGrandPrixBatch(CreateGrandPrixBatchRequest request) {
        log.debug("Criando {} Grandes Prêmios em lote", request.getGrandPrix().size());
        
        List<GrandPrixResponse> created = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        
        for (CreateGrandPrixRequest gpRequest : request.getGrandPrix()) {
            try {
                GrandPrixResponse response = createGrandPrix(gpRequest);
                created.add(response);
                log.debug("Grande Prêmio criado: {} - {}", response.getName(), response.getCountry());
            } catch (Exception e) {
                String errorMsg = String.format("Erro ao criar GP '%s': %s", 
                    gpRequest.getName(), e.getMessage());
                errors.add(errorMsg);
                log.error("Erro ao criar Grande Prêmio: {}", errorMsg);
            }
        }
        
        CreateGrandPrixBatchResponse response = CreateGrandPrixBatchResponse.builder()
                .totalRequested(request.getGrandPrix().size())
                .totalCreated(created.size())
                .totalErrors(errors.size())
                .created(created)
                .errors(errors)
                .build();
        
        log.debug("Criação em lote concluída: {} criados, {} erros", 
            response.getTotalCreated(), response.getTotalErrors());
        
        return response;
    }
    
    @Transactional
    public GrandPrixResponse updateGrandPrix(Long id, UpdateGrandPrixRequest request) {
        log.debug("Atualizando Grande Prêmio ID: {}", id);
        
        GrandPrix grandPrix = grandPrixRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grande Prêmio não encontrado com ID: " + id));
        
        // Atualizar campos se fornecidos
        if (request.getName() != null) {
            // Verificar se o novo nome já existe na mesma temporada
            if (!grandPrix.getName().equals(request.getName()) && 
                grandPrixRepository.existsByNameAndSeason(request.getName(), grandPrix.getSeason())) {
                throw new RuntimeException("Já existe um Grande Prêmio com o nome '" + request.getName() + "' na temporada " + grandPrix.getSeason());
            }
            grandPrix.setName(request.getName());
        }
        
        if (request.getCountry() != null) {
            grandPrix.setCountry(request.getCountry());
        }
        if (request.getCity() != null) {
            grandPrix.setCity(request.getCity());
        }
        if (request.getCircuitName() != null) {
            grandPrix.setCircuitName(request.getCircuitName());
        }
        if (request.getCircuitUrl() != null) {
            grandPrix.setCircuitUrl(request.getCircuitUrl());
        }
        if (request.getPractice1DateTime() != null) {
            grandPrix.setPractice1DateTime(request.getPractice1DateTime());
        }
        if (request.getPractice2DateTime() != null) {
            grandPrix.setPractice2DateTime(request.getPractice2DateTime());
        }
        if (request.getPractice3DateTime() != null) {
            grandPrix.setPractice3DateTime(request.getPractice3DateTime());
        }
        if (request.getQualifyingDateTime() != null) {
            grandPrix.setQualifyingDateTime(request.getQualifyingDateTime());
        }
        if (request.getSprintDateTime() != null) {
            grandPrix.setSprintDateTime(request.getSprintDateTime());
        }
        if (request.getRaceDateTime() != null) {
            grandPrix.setRaceDateTime(request.getRaceDateTime());
        }
        if (request.getTimezone() != null) {
            grandPrix.setTimezone(request.getTimezone());
        }
        if (request.getLaps() != null) {
            grandPrix.setLaps(request.getLaps());
        }
        if (request.getCircuitLength() != null) {
            grandPrix.setCircuitLength(request.getCircuitLength());
        }
        if (request.getDescription() != null) {
            grandPrix.setDescription(request.getDescription());
        }
        if (request.getActive() != null) {
            grandPrix.setActive(request.getActive());
        }
        if (request.getCompleted() != null) {
            grandPrix.setCompleted(request.getCompleted());
        }
        
        GrandPrix updatedGrandPrix = grandPrixRepository.save(grandPrix);
        log.debug("Grande Prêmio atualizado com sucesso: {}", updatedGrandPrix.getId());
        
        return GrandPrixResponse.fromGrandPrix(updatedGrandPrix);
    }
    
    @Transactional
    public void deleteGrandPrix(Long id) {
        log.debug("Deletando Grande Prêmio ID: {}", id);
        
        if (!grandPrixRepository.existsById(id)) {
            throw new RuntimeException("Grande Prêmio não encontrado com ID: " + id);
        }
        
        grandPrixRepository.deleteById(id);
        log.debug("Grande Prêmio deletado com sucesso: {}", id);
    }
    
    @Transactional
    public GrandPrixResponse markAsCompleted(Long id) {
        log.debug("Marcando Grande Prêmio como concluído ID: {}", id);
        
        GrandPrix grandPrix = grandPrixRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grande Prêmio não encontrado com ID: " + id));
        
        grandPrix.setCompleted(true);
        GrandPrix updatedGrandPrix = grandPrixRepository.save(grandPrix);
        log.debug("Grande Prêmio marcado como concluído: {}", updatedGrandPrix.getId());
        
        return GrandPrixResponse.fromGrandPrix(updatedGrandPrix);
    }
    
    @Transactional
    public GrandPrixResponse markAsPending(Long id) {
        log.debug("Marcando Grande Prêmio como pendente ID: {}", id);
        
        GrandPrix grandPrix = grandPrixRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grande Prêmio não encontrado com ID: " + id));
        
        grandPrix.setCompleted(false);
        GrandPrix updatedGrandPrix = grandPrixRepository.save(grandPrix);
        log.debug("Grande Prêmio marcado como pendente: {}", updatedGrandPrix.getId());
        
        return GrandPrixResponse.fromGrandPrix(updatedGrandPrix);
    }
    
    private void validateCreateGrandPrixRequest(CreateGrandPrixRequest request) {
        // Verificar se já existe GP com o nome na temporada
        if (grandPrixRepository.existsByNameAndSeason(request.getName(), request.getSeason())) {
            throw new RuntimeException("Já existe um Grande Prêmio com o nome '" + request.getName() + "' na temporada " + request.getSeason());
        }
        
        // Verificar se já existe GP com a rodada na temporada
        if (grandPrixRepository.existsBySeasonAndRound(request.getSeason(), request.getRound())) {
            throw new RuntimeException("Já existe um Grande Prêmio na rodada " + request.getRound() + " da temporada " + request.getSeason());
        }
        
        // Validar se a data da corrida é futura (opcional, pode ser removido para GPs históricos)
        if (request.getRaceDateTime().isBefore(LocalDateTime.now().minusDays(1))) {
            log.warn("Data da corrida é no passado: {}", request.getRaceDateTime());
        }
    }
} 