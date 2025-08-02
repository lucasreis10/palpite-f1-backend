package com.lucasreis.palpitef1backend.domain.imports;

import java.time.LocalDateTime;
import java.util.List;

public class ImportResponse {
    
    private boolean success;
    private String message;
    private LocalDateTime processedAt;
    private ImportStats stats;
    private List<String> warnings;
    private List<String> errors;
    
    // Construtor padrão
    public ImportResponse() {}
    
    // Construtor completo
    public ImportResponse(boolean success, String message, LocalDateTime processedAt, 
                         ImportStats stats, List<String> warnings, List<String> errors) {
        this.success = success;
        this.message = message;
        this.processedAt = processedAt;
        this.stats = stats;
        this.warnings = warnings;
        this.errors = errors;
    }
    
    // Getters e Setters
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public LocalDateTime getProcessedAt() {
        return processedAt;
    }
    
    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }
    
    public ImportStats getStats() {
        return stats;
    }
    
    public void setStats(ImportStats stats) {
        this.stats = stats;
    }
    
    public List<String> getWarnings() {
        return warnings;
    }
    
    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }
    
    public List<String> getErrors() {
        return errors;
    }
    
    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
    
    // Classe interna para estatísticas
    public static class ImportStats {
        private int totalUsers;
        private int totalGuesses;
        private int totalRaces;
        private int qualifyingGuesses;
        private int raceGuesses;
        private int unmappedPilots;
        private int createdUsers;
        private int createdGuesses;
        private long processingTimeMs;
        
        // Construtor padrão
        public ImportStats() {}
        
        // Construtor completo
        public ImportStats(int totalUsers, int totalGuesses, int totalRaces, 
                          int qualifyingGuesses, int raceGuesses, int unmappedPilots,
                          int createdUsers, int createdGuesses, long processingTimeMs) {
            this.totalUsers = totalUsers;
            this.totalGuesses = totalGuesses;
            this.totalRaces = totalRaces;
            this.qualifyingGuesses = qualifyingGuesses;
            this.raceGuesses = raceGuesses;
            this.unmappedPilots = unmappedPilots;
            this.createdUsers = createdUsers;
            this.createdGuesses = createdGuesses;
            this.processingTimeMs = processingTimeMs;
        }
        
        // Getters e Setters
        public int getTotalUsers() { return totalUsers; }
        public void setTotalUsers(int totalUsers) { this.totalUsers = totalUsers; }
        
        public int getTotalGuesses() { return totalGuesses; }
        public void setTotalGuesses(int totalGuesses) { this.totalGuesses = totalGuesses; }
        
        public int getTotalRaces() { return totalRaces; }
        public void setTotalRaces(int totalRaces) { this.totalRaces = totalRaces; }
        
        public int getQualifyingGuesses() { return qualifyingGuesses; }
        public void setQualifyingGuesses(int qualifyingGuesses) { this.qualifyingGuesses = qualifyingGuesses; }
        
        public int getRaceGuesses() { return raceGuesses; }
        public void setRaceGuesses(int raceGuesses) { this.raceGuesses = raceGuesses; }
        
        public int getUnmappedPilots() { return unmappedPilots; }
        public void setUnmappedPilots(int unmappedPilots) { this.unmappedPilots = unmappedPilots; }
        
        public int getCreatedUsers() { return createdUsers; }
        public void setCreatedUsers(int createdUsers) { this.createdUsers = createdUsers; }
        
        public int getCreatedGuesses() { return createdGuesses; }
        public void setCreatedGuesses(int createdGuesses) { this.createdGuesses = createdGuesses; }
        
        public long getProcessingTimeMs() { return processingTimeMs; }
        public void setProcessingTimeMs(long processingTimeMs) { this.processingTimeMs = processingTimeMs; }
    }
    
    // Métodos estáticos para criar respostas
    public static ImportResponse success(String message, ImportStats stats) {
        ImportResponse response = new ImportResponse();
        response.setSuccess(true);
        response.setMessage(message);
        response.setProcessedAt(LocalDateTime.now());
        response.setStats(stats);
        return response;
    }
    
    public static ImportResponse error(String message) {
        ImportResponse response = new ImportResponse();
        response.setSuccess(false);
        response.setMessage(message);
        response.setProcessedAt(LocalDateTime.now());
        response.setStats(new ImportStats());
        return response;
    }
    
    public static ImportResponse successWithWarnings(String message, ImportStats stats, List<String> warnings) {
        ImportResponse response = new ImportResponse();
        response.setSuccess(true);
        response.setMessage(message);
        response.setProcessedAt(LocalDateTime.now());
        response.setStats(stats);
        response.setWarnings(warnings);
        return response;
    }
} 