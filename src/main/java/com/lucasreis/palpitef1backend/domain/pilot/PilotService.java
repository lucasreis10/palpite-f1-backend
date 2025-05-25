package com.lucasreis.palpitef1backend.domain.pilot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PilotService {
    
    private final PilotRepository pilotRepository;
    
    public List<PilotResponse> getAllPilots() {
        log.debug("Buscando todos os pilotos");
        return pilotRepository.findAllOrderByName()
                .stream()
                .map(PilotResponse::fromPilot)
                .collect(Collectors.toList());
    }
    
    public PilotResponse getPilotById(Long id) {
        log.debug("Buscando piloto por ID: {}", id);
        Pilot pilot = pilotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Piloto não encontrado com ID: " + id));
        return PilotResponse.fromPilot(pilot);
    }
    
    public PilotResponse getPilotByDriverId(String driverId) {
        log.debug("Buscando piloto por driverId: {}", driverId);
        Pilot pilot = pilotRepository.findByDriverId(driverId)
                .orElseThrow(() -> new RuntimeException("Piloto não encontrado com driverId: " + driverId));
        return PilotResponse.fromPilot(pilot);
    }
    
    public List<PilotResponse> getPilotsByNationality(String nationality) {
        log.debug("Buscando pilotos por nacionalidade: {}", nationality);
        return pilotRepository.findByNationalityIgnoreCase(nationality)
                .stream()
                .map(PilotResponse::fromPilot)
                .collect(Collectors.toList());
    }
    
    public List<PilotResponse> searchPilotsByName(String name) {
        log.debug("Buscando pilotos por nome: {}", name);
        return pilotRepository.findByNameContaining(name)
                .stream()
                .map(PilotResponse::fromPilot)
                .collect(Collectors.toList());
    }
    
    public PilotResponse createPilot(CreatePilotRequest request) {
        log.debug("Criando novo piloto: {}", request.getDriverId());
        
        if (pilotRepository.existsByDriverId(request.getDriverId())) {
            throw new RuntimeException("Já existe um piloto com o driverId: " + request.getDriverId());
        }
        
        Pilot pilot = Pilot.builder()
                .driverId(request.getDriverId())
                .givenName(request.getGivenName())
                .familyName(request.getFamilyName())
                .dateOfBirth(request.getDateOfBirth())
                .nationality(request.getNationality())
                .url(request.getUrl())
                .permanentNumber(request.getPermanentNumber())
                .code(request.getCode())
                .build();
        
        Pilot savedPilot = pilotRepository.save(pilot);
        log.debug("Piloto criado com sucesso: {}", savedPilot.getId());
        
        return PilotResponse.fromPilot(savedPilot);
    }
    
    public PilotResponse updatePilot(Long id, UpdatePilotRequest request) {
        log.debug("Atualizando piloto ID: {}", id);
        
        Pilot pilot = pilotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Piloto não encontrado com ID: " + id));
        
        if (request.getGivenName() != null) {
            pilot.setGivenName(request.getGivenName());
        }
        if (request.getFamilyName() != null) {
            pilot.setFamilyName(request.getFamilyName());
        }
        if (request.getDateOfBirth() != null) {
            pilot.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getNationality() != null) {
            pilot.setNationality(request.getNationality());
        }
        if (request.getUrl() != null) {
            pilot.setUrl(request.getUrl());
        }
        if (request.getPermanentNumber() != null) {
            pilot.setPermanentNumber(request.getPermanentNumber());
        }
        if (request.getCode() != null) {
            pilot.setCode(request.getCode());
        }
        if (request.getActive() != null) {
            pilot.setActive(request.getActive());
        }
        
        Pilot updatedPilot = pilotRepository.save(pilot);
        log.debug("Piloto atualizado com sucesso: {}", updatedPilot.getId());
        
        return PilotResponse.fromPilot(updatedPilot);
    }
    
    public void deletePilot(Long id) {
        log.debug("Deletando piloto ID: {}", id);
        
        if (!pilotRepository.existsById(id)) {
            throw new RuntimeException("Piloto não encontrado com ID: " + id);
        }
        
        pilotRepository.deleteById(id);
        log.debug("Piloto deletado com sucesso: {}", id);
    }
    
    public CreatePilotsResponse createPilots(CreatePilotsRequest request) {
        log.debug("Criando {} pilotos em lote", request.getPilots().size());
        
        List<PilotResponse> createdPilots = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        
        for (int i = 0; i < request.getPilots().size(); i++) {
            CreatePilotRequest pilotRequest = request.getPilots().get(i);
            try {
                // Verificar se já existe um piloto com o mesmo driverId
                if (pilotRepository.existsByDriverId(pilotRequest.getDriverId())) {
                    errors.add(String.format("Piloto %d: Já existe um piloto com o driverId '%s'", 
                        i + 1, pilotRequest.getDriverId()));
                    continue;
                }
                
                Pilot pilot = Pilot.builder()
                        .driverId(pilotRequest.getDriverId())
                        .givenName(pilotRequest.getGivenName())
                        .familyName(pilotRequest.getFamilyName())
                        .dateOfBirth(pilotRequest.getDateOfBirth())
                        .nationality(pilotRequest.getNationality())
                        .url(pilotRequest.getUrl())
                        .permanentNumber(pilotRequest.getPermanentNumber())
                        .code(pilotRequest.getCode())
                        .build();
                
                Pilot savedPilot = pilotRepository.save(pilot);
                createdPilots.add(PilotResponse.fromPilot(savedPilot));
                log.debug("Piloto {} criado com sucesso: {}", i + 1, savedPilot.getId());
                
            } catch (Exception e) {
                log.error("Erro ao criar piloto {}: {}", i + 1, e.getMessage());
                errors.add(String.format("Piloto %d: %s", i + 1, e.getMessage()));
            }
        }
        
        log.debug("Criação em lote finalizada: {} criados, {} erros", 
            createdPilots.size(), errors.size());
        
        return CreatePilotsResponse.of(createdPilots, errors, request.getPilots().size());
    }
    
    // Métodos específicos para pilotos ativos
    public List<PilotResponse> getAllActivePilots() {
        log.debug("Buscando todos os pilotos ativos");
        return pilotRepository.findAllActiveOrderByName()
                .stream()
                .map(PilotResponse::fromPilot)
                .collect(Collectors.toList());
    }
    
    public List<PilotResponse> getPilotsByStatus(Boolean active) {
        log.debug("Buscando pilotos por status ativo: {}", active);
        return pilotRepository.findByActiveOrderByFamilyNameAscGivenNameAsc(active)
                .stream()
                .map(PilotResponse::fromPilot)
                .collect(Collectors.toList());
    }
    
    public List<PilotResponse> getActivePilotsByNationality(String nationality) {
        log.debug("Buscando pilotos ativos por nacionalidade: {}", nationality);
        return pilotRepository.findByNationalityIgnoreCaseAndActive(nationality, true)
                .stream()
                .map(PilotResponse::fromPilot)
                .collect(Collectors.toList());
    }
    
    public List<PilotResponse> searchActivePilotsByName(String name) {
        log.debug("Buscando pilotos ativos por nome: {}", name);
        return pilotRepository.findByNameContainingAndActive(name, true)
                .stream()
                .map(PilotResponse::fromPilot)
                .collect(Collectors.toList());
    }
    
    // Métodos para ativar/inativar pilotos
    public PilotResponse activatePilot(Long id) {
        log.debug("Ativando piloto ID: {}", id);
        
        Pilot pilot = pilotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Piloto não encontrado com ID: " + id));
        
        pilot.setActive(true);
        Pilot updatedPilot = pilotRepository.save(pilot);
        log.debug("Piloto ativado com sucesso: {}", updatedPilot.getId());
        
        return PilotResponse.fromPilot(updatedPilot);
    }
    
    public PilotResponse deactivatePilot(Long id) {
        log.debug("Inativando piloto ID: {}", id);
        
        Pilot pilot = pilotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Piloto não encontrado com ID: " + id));
        
        pilot.setActive(false);
        Pilot updatedPilot = pilotRepository.save(pilot);
        log.debug("Piloto inativado com sucesso: {}", updatedPilot.getId());
        
        return PilotResponse.fromPilot(updatedPilot);
    }
} 