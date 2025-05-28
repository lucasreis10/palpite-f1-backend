package com.lucasreis.palpitef1backend.domain.grandprix;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportEventsResponse {
    
    private Integer season;
    private Integer totalFound;
    private Integer imported;
    private Integer skipped;
    private Integer errors;
    private List<String> errorMessages;
    private List<GrandPrixResponse> importedEvents;
    private String message;
    
    public String getSummaryMessage() {
        if (message != null) {
            return message;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("Importação da temporada ").append(season).append(" concluída: ");
        sb.append(imported).append(" eventos importados");
        
        if (skipped > 0) {
            sb.append(", ").append(skipped).append(" ignorados (já existiam)");
        }
        
        if (errors > 0) {
            sb.append(", ").append(errors).append(" erros");
        }
        
        sb.append(" de ").append(totalFound).append(" eventos encontrados.");
        
        return sb.toString();
    }
} 