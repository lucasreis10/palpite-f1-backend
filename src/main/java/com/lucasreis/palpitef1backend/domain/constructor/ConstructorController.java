package com.lucasreis.palpitef1backend.domain.constructor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/constructors")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ConstructorController {
    
    private final ConstructorService constructorService;
    
    @GetMapping
    public ResponseEntity<List<ConstructorResponse>> getAllConstructors() {
        log.debug("GET /api/constructors - Buscando todas as escuderias");
        List<ConstructorResponse> constructors = constructorService.getAllConstructors();
        return ResponseEntity.ok(constructors);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<ConstructorResponse>> getAllActiveConstructors() {
        log.debug("GET /api/constructors/active - Buscando escuderias ativas");
        List<ConstructorResponse> constructors = constructorService.getAllActiveConstructors();
        return ResponseEntity.ok(constructors);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ConstructorResponse> getConstructorById(@PathVariable Long id) {
        log.debug("GET /api/constructors/{} - Buscando escuderia por ID", id);
        ConstructorResponse constructor = constructorService.getConstructorById(id);
        return ResponseEntity.ok(constructor);
    }
    
    @GetMapping("/constructor-id/{constructorId}")
    public ResponseEntity<ConstructorResponse> getConstructorByConstructorId(@PathVariable String constructorId) {
        log.debug("GET /api/constructors/constructor-id/{} - Buscando escuderia por constructorId", constructorId);
        ConstructorResponse constructor = constructorService.getConstructorByConstructorId(constructorId);
        return ResponseEntity.ok(constructor);
    }
    
    @GetMapping("/nationality/{nationality}")
    public ResponseEntity<List<ConstructorResponse>> getConstructorsByNationality(@PathVariable String nationality) {
        log.debug("GET /api/constructors/nationality/{} - Buscando escuderias por nacionalidade", nationality);
        List<ConstructorResponse> constructors = constructorService.getConstructorsByNationality(nationality);
        return ResponseEntity.ok(constructors);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<ConstructorResponse>> searchConstructorsByName(@RequestParam String name) {
        log.debug("GET /api/constructors/search?name={} - Buscando escuderias por nome", name);
        List<ConstructorResponse> constructors = constructorService.searchConstructorsByName(name);
        return ResponseEntity.ok(constructors);
    }
} 