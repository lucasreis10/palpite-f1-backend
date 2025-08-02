package com.lucasreis.palpitef1backend.controllers;

import com.lucasreis.palpitef1backend.domain.imports.ExcelImportService;
import com.lucasreis.palpitef1backend.domain.imports.ImportResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/import")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ImportController {
    
    @Autowired
    private ExcelImportService excelImportService;
    
    /**
     * Endpoint para fazer upload e processar arquivo Excel com palpites históricos
     * 
     * @param file Arquivo Excel com aba "Palpite"
     * @return Resultado do processamento com estatísticas
     */
    @PostMapping("/excel")
    public ResponseEntity<ImportResponse> importExcel(@RequestParam("excel") MultipartFile file) {
        System.out.println("📁 Recebido arquivo Excel para import: " + file.getOriginalFilename() + " (" + formatFileSize(file.getSize()) + ")");
        
        try {
            // Validações básicas
            if (file.isEmpty()) {
                System.out.println("❌ Arquivo vazio recebido");
                return ResponseEntity.badRequest().body(
                    ImportResponse.error("Arquivo não pode estar vazio")
                );
            }
            
            String filename = file.getOriginalFilename();
            if (filename == null || !isExcelFile(filename)) {
                System.out.println("❌ Formato de arquivo inválido: " + filename);
                return ResponseEntity.badRequest().body(
                    ImportResponse.error("Arquivo deve ser Excel (.xlsx ou .xls)")
                );
            }
            
            // Processar arquivo Excel
            ImportResponse result = excelImportService.processExcelFile(file);
            
            if (result.isSuccess()) {
                System.out.println("✅ Import concluído com sucesso: " + 
                    result.getStats().getTotalUsers() + " usuários, " +
                    result.getStats().getTotalGuesses() + " palpites");
                return ResponseEntity.ok(result);
            } else {
                System.out.println("⚠️ Import falhou: " + result.getMessage());
                return ResponseEntity.badRequest().body(result);
            }
            
        } catch (Exception e) {
            System.err.println("❌ Erro interno durante import do Excel: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ImportResponse.error("Erro interno do servidor: " + e.getMessage())
            );
        }
    }
    
    /**
     * Endpoint para testar se o serviço de import está funcionando
     */
    @GetMapping("/test")
    public ResponseEntity<String> testImport() {
        System.out.println("🧪 Teste do serviço de import");
        return ResponseEntity.ok("Serviço de import Excel funcionando - versão 1.0");
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