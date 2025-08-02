package com.lucasreis.palpitef1backend.domain.imports;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportResponse {
    
    private boolean success;
    private String message;
    private LocalDateTime processedAt;
    private ImportStats stats;
    private List<String> warnings;
    private List<String> errors;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImportStats {
        private int totalUsers;
        private int totalGuesses;
        private int qualifyingGuesses;
        private int raceGuesses;
        private int processedRaces;
        private long processingTimeMs;
        private String fileName;
        private long fileSizeBytes;
    }
    
    // Factory methods para criar respostas
    public static ImportResponse success(String message, ImportStats stats) {
        return ImportResponse.builder()
                .success(true)
                .message(message)
                .processedAt(LocalDateTime.now())
                .stats(stats)
                .warnings(new ArrayList<>())
                .errors(new ArrayList<>())
                .build();
    }
    
    public static ImportResponse error(String message) {
        return ImportResponse.builder()
                .success(false)
                .message(message)
                .processedAt(LocalDateTime.now())
                .stats(ImportStats.builder().build())
                .warnings(new ArrayList<>())
                .errors(List.of(message))
                .build();
    }
    
    public static ImportResponse error(String message, List<String> errors) {
        return ImportResponse.builder()
                .success(false)
                .message(message)
                .processedAt(LocalDateTime.now())
                .stats(ImportStats.builder().build())
                .warnings(new ArrayList<>())
                .errors(errors != null ? errors : List.of(message))
                .build();
    }
}
