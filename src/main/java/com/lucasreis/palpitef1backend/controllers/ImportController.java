package com.lucasreis.palpitef1backend.controllers;

import com.lucasreis.palpitef1backend.domain.imports.ExcelImportService;
import com.lucasreis.palpitef1backend.domain.imports.ImportResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/import")
@RequiredArgsConstructor
public class ImportController {

    private final ExcelImportService excelImportService;

    @PostMapping("/excel")
    public ResponseEntity<ImportResponse> importExcel(@RequestParam("excel") MultipartFile file) {
        log.info("📤 Recebendo arquivo Excel para import: {} ({} bytes)", 
            file.getOriginalFilename(), file.getSize());
        
        ImportResponse response = excelImportService.processExcelFile(file);
        
        if (response.isSuccess()) {
            log.info("✅ Import realizado com sucesso: {}", response.getMessage());
            return ResponseEntity.ok(response);
        } else {
            log.error("❌ Falha no import: {}", response.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> testService() {
        return ResponseEntity.ok(Map.of(
            "status", "OK",
            "service", "Excel Import Service",
            "version", "1.0.0",
            "timestamp", System.currentTimeMillis()
        ));
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getServiceInfo() {
        return ResponseEntity.ok(Map.of(
            "service", "Excel Import Service",
            "version", "1.0.0",
            "supportedFormats", new String[]{".xlsx", ".xls"},
            "requiredSheet", "Palpites", // Atualizado para "Palpites" (plural)
            "description", "Importa palpites históricos de arquivos Excel",
            "maxFileSizeMB", 50, // Atualizado para 50MB
            "status", "ACTIVE",
            "timestamp", System.currentTimeMillis()
        ));
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getServiceStatus() {
        return ResponseEntity.ok(Map.of(
            "status", "HEALTHY",
            "uptime", "Running",
            "lastCheck", LocalDateTime.now(),
            "dependencies", Map.of(
                "apache-poi", "5.2.4",
                "spring-boot", "3.2.3"
            )
        ));
    }
} 