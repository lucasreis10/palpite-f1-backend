package com.lucasreis.palpitef1backend.domain.constructor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConstructorService {
    
    private final ConstructorRepository constructorRepository;
    
    public List<ConstructorResponse> getAllConstructors() {
        log.debug("Buscando todas as escuderias");
        return constructorRepository.findAllOrderByName()
                .stream()
                .map(ConstructorResponse::fromConstructor)
                .collect(Collectors.toList());
    }
    
    public List<ConstructorResponse> getAllActiveConstructors() {
        log.debug("Buscando todas as escuderias ativas");
        return constructorRepository.findAllActiveOrderByName()
                .stream()
                .map(ConstructorResponse::fromConstructor)
                .collect(Collectors.toList());
    }
    
    public ConstructorResponse getConstructorById(Long id) {
        log.debug("Buscando escuderia por ID: {}", id);
        Constructor constructor = constructorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Escuderia não encontrada com ID: " + id));
        return ConstructorResponse.fromConstructor(constructor);
    }
    
    public ConstructorResponse getConstructorByConstructorId(String constructorId) {
        log.debug("Buscando escuderia por constructorId: {}", constructorId);
        Constructor constructor = constructorRepository.findByConstructorId(constructorId)
                .orElseThrow(() -> new RuntimeException("Escuderia não encontrada com constructorId: " + constructorId));
        return ConstructorResponse.fromConstructor(constructor);
    }
    
    public List<ConstructorResponse> getConstructorsByNationality(String nationality) {
        log.debug("Buscando escuderias por nacionalidade: {}", nationality);
        return constructorRepository.findByNationalityIgnoreCase(nationality)
                .stream()
                .map(ConstructorResponse::fromConstructor)
                .collect(Collectors.toList());
    }
    
    public List<ConstructorResponse> searchConstructorsByName(String name) {
        log.debug("Buscando escuderias por nome: {}", name);
        return constructorRepository.findByNameContaining(name)
                .stream()
                .map(ConstructorResponse::fromConstructor)
                .collect(Collectors.toList());
    }
} 