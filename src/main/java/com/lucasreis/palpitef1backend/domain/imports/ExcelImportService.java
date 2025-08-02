package com.lucasreis.palpitef1backend.domain.imports;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class ExcelImportService {
    
    /**
     * Processa arquivo Excel e retorna resultado do import
     */
    public ImportResponse processExcelFile(MultipartFile file) {
        long startTime = System.currentTimeMillis();
        
        try {
            log.info("🔄 Iniciando processamento do arquivo Excel: {}", file.getOriginalFilename());
            
            // Abrir workbook
            Workbook workbook = createWorkbook(file);
            
            // Encontrar e processar aba "Palpite"
            Sheet sheet = workbook.getSheet("Palpite");
            if (sheet == null) {
                log.error("❌ Aba 'Palpite' não encontrada no arquivo Excel");
                return ImportResponse.error("Aba 'Palpite' não encontrada no arquivo Excel");
            }
            
            // Processar dados da planilha
            ImportResponse.ImportStats stats = processSheet(sheet, file);
            ImportResponse.ImportStats finalStats = ImportResponse.ImportStats.builder()
                .totalUsers(stats.getTotalUsers())
                .totalGuesses(stats.getTotalGuesses())
                .qualifyingGuesses(stats.getQualifyingGuesses())
                .raceGuesses(stats.getRaceGuesses())
                .processedRaces(stats.getProcessedRaces())
                .processingTimeMs(System.currentTimeMillis() - startTime)
                .fileName(file.getOriginalFilename())
                .fileSizeBytes(file.getSize())
                .build();
            
            workbook.close();
            
            log.info("✅ Processamento concluído com sucesso!");
            log.info("📊 Estatísticas: {} usuários, {} palpites processados", 
                finalStats.getTotalUsers(), finalStats.getTotalGuesses());
            
            return ImportResponse.success(
                String.format("Import concluído! Processados %d usuários com %d palpites.", 
                    finalStats.getTotalUsers(), finalStats.getTotalGuesses()),
                finalStats
            );
            
        } catch (Exception e) {
            log.error("❌ Erro durante processamento do Excel: {}", e.getMessage(), e);
            return ImportResponse.error("Erro interno: " + e.getMessage());
        }
    }
    
    private Workbook createWorkbook(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        if (filename != null && filename.toLowerCase().endsWith(".xlsx")) {
            return new XSSFWorkbook(file.getInputStream());
        } else {
            return new HSSFWorkbook(file.getInputStream());
        }
    }
    
    private ImportResponse.ImportStats processSheet(Sheet sheet, MultipartFile file) {
        // Mock de processamento por enquanto
        log.info("🔄 Processando planilha com {} linhas", sheet.getLastRowNum() + 1);
        
        return ImportResponse.ImportStats.builder()
                .totalUsers(10) // Mock
                .totalGuesses(230) // Mock
                .qualifyingGuesses(115) // Mock
                .raceGuesses(115) // Mock
                .processedRaces(23) // Mock
                .build();
    }
}
