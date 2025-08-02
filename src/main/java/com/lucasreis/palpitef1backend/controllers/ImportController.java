package com.lucasreis.palpitef1backend.controllers;

import com.lucasreis.palpitef1backend.domain.imports.ExcelImportService;
import com.lucasreis.palpitef1backend.domain.imports.ImportResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/import")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class ImportController {

    private final ExcelImportService excelImportService;

    /**
     * Endpoint para fazer upload e processar arquivo Excel com palpites históricos
     * 
     * @param file Arquivo Excel com aba "Palpite"
     * @return Resultado do processamento com estatísticas
     */
    @PostMapping("/excel")
    public ResponseEntity<ImportResponse> importExcel(@RequestParam("excel") MultipartFile file) {
        log.info("📁 Recebido arquivo Excel para import: {} ({})", 
            file.getOriginalFilename(), formatFileSize(file.getSize()));
        
        try {
            // Validações básicas
            if (file.isEmpty()) {
                log.warn("❌ Arquivo vazio recebido");
                return ResponseEntity.badRequest().body(
                    ImportResponse.error("Arquivo não pode estar vazio")
                );
            }
            
            String filename = file.getOriginalFilename();
            if (filename == null || !isExcelFile(filename)) {
                log.warn("❌ Formato de arquivo inválido: {}", filename);
                return ResponseEntity.badRequest().body(
                    ImportResponse.error("Arquivo deve ser Excel (.xlsx ou .xls)")
                );
            }
            
            // Validar tamanho do arquivo (máximo 10MB)
            if (file.getSize() > 10 * 1024 * 1024) {
                log.warn("❌ Arquivo muito grande: {} MB", file.getSize() / (1024 * 1024));
                return ResponseEntity.badRequest().body(
                    ImportResponse.error("Arquivo deve ter no máximo 10MB")
                );
            }
            
            // Processar arquivo Excel
            ImportResponse result = excelImportService.processExcelFile(file);
            
            if (result.isSuccess()) {
                log.info("✅ Import concluído com sucesso: {} usuários, {} palpites", 
                    result.getStats().getTotalUsers(), 
                    result.getStats().getTotalGuesses());
                return ResponseEntity.ok(result);
            } else {
                log.warn("⚠️ Import falhou: {}", result.getMessage());
                return ResponseEntity.badRequest().body(result);
            }
            
        } catch (Exception e) {
            log.error("❌ Erro interno durante import do Excel: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ImportResponse.error("Erro interno do servidor: " + e.getMessage())
            );
        }
    }
    
    /**
     * Endpoint para testar se o serviço de import está funcionando
     */
    @GetMapping("/test")
    public ResponseEntity<Object> testImport() {
        log.debug("🧪 Teste do serviço de import");
        return ResponseEntity.ok(
            new Object() {
                public String status = "OK";
                public String service = "Excel Import Service";
                public String version = "1.0.0";
                public long timestamp = System.currentTimeMillis();
            }
        );
    }
    
    /**
     * Endpoint para obter informações sobre o serviço de import
     */
    @GetMapping("/info")
    public ResponseEntity<Object> getImportInfo() {
        return ResponseEntity.ok(
            new Object() {
                public String service = "Excel Import Service";
                public String version = "1.0.0";
                public String[] supportedFormats = {".xlsx", ".xls"};
                public String requiredSheet = "Palpite";
                public String description = "Importa palpites históricos de arquivos Excel";
                public int maxFileSizeMB = 10;
                public String status = "ACTIVE";
                public long timestamp = System.currentTimeMillis();
            }
        );
    }
    
    /**
     * Endpoint de status/health para verificar se o serviço está funcionando
     */
    @GetMapping("/status")
    public ResponseEntity<Object> getStatus() {
        return ResponseEntity.ok(
            new Object() {
                public String status = "UP";
                public String service = "import-service";
                public long timestamp = System.currentTimeMillis();
                public boolean ready = true;
            }
        );
    }
    
    // ========== MÉTODOS UTILITÁRIOS ==========
    
    private boolean isExcelFile(String filename) {
        return filename.toLowerCase().endsWith(".xlsx") || filename.toLowerCase().endsWith(".xls");
    }
    
    private String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }
} 