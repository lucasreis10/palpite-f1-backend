package com.lucasreis.palpitef1backend.domain.grandprix;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class JolpicaImportService {
    
    private final RestTemplate restTemplate;
    private final GrandPrixRepository grandPrixRepository;
    
    private static final String JOLPICA_API_BASE = "https://api.jolpi.ca/ergast/f1";
    
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class JolpicaResponse {
        @JsonProperty("MRData")
        private MRData mrData;
    }
    
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MRData {
        @JsonProperty("RaceTable")
        private RaceTable raceTable;
    }
    
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RaceTable {
        private String season;
        @JsonProperty("Races")
        private List<Race> races;
    }
    
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Race {
        private String season;
        private String round;
        private String url;
        private String raceName;
        @JsonProperty("Circuit")
        private Circuit circuit;
        private String date;
        private String time;
        @JsonProperty("FirstPractice")
        private Session firstPractice;
        @JsonProperty("SecondPractice")
        private Session secondPractice;
        @JsonProperty("ThirdPractice")
        private Session thirdPractice;
        @JsonProperty("Qualifying")
        private Session qualifying;
        @JsonProperty("Sprint")
        private Session sprint;
        @JsonProperty("SprintQualifying")
        private Session sprintQualifying;
    }
    
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Circuit {
        private String circuitId;
        private String url;
        private String circuitName;
        @JsonProperty("Location")
        private Location location;
    }
    
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Location {
        private String lat;
        @JsonProperty("long")
        private String longitude;
        private String locality;
        private String country;
    }
    
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Session {
        private String date;
        private String time;
    }
    
    public ImportEventsResponse importEventsFromJolpica(Integer season) {
        log.info("Iniciando importação de eventos da temporada {} da API jolpica-f1", season);
        
        try {
            String url = JOLPICA_API_BASE + "/" + season + ".json";
            log.debug("Fazendo requisição para: {}", url);
            
            JolpicaResponse response = restTemplate.getForObject(url, JolpicaResponse.class);
            
            if (response == null || response.getMrData() == null || 
                response.getMrData().getRaceTable() == null || 
                response.getMrData().getRaceTable().getRaces() == null) {
                throw new RuntimeException("Resposta inválida da API jolpica-f1");
            }
            
            List<Race> races = response.getMrData().getRaceTable().getRaces();
            log.info("Encontrados {} eventos na API jolpica-f1 para a temporada {}", races.size(), season);
            
            List<GrandPrix> importedEvents = new ArrayList<>();
            List<String> errors = new ArrayList<>();
            int skipped = 0;
            
            for (Race race : races) {
                try {
                    // Verificar se já existe
                    Integer roundNumber = Integer.parseInt(race.getRound());
                    if (grandPrixRepository.existsBySeasonAndRound(season, roundNumber)) {
                        log.debug("Evento já existe: temporada {} rodada {} - {}", season, roundNumber, race.getRaceName());
                        skipped++;
                        continue;
                    }
                    
                    GrandPrix grandPrix = convertRaceToGrandPrix(race, season);
                    GrandPrix saved = grandPrixRepository.save(grandPrix);
                    importedEvents.add(saved);
                    log.debug("Evento importado: {}", saved.getName());
                    
                } catch (Exception e) {
                    String error = "Erro ao importar evento " + race.getRaceName() + ": " + e.getMessage();
                    log.error(error, e);
                    errors.add(error);
                }
            }
            
            log.info("Importação concluída: {} eventos importados, {} ignorados, {} erros", 
                    importedEvents.size(), skipped, errors.size());
            
            return ImportEventsResponse.builder()
                    .season(season)
                    .totalFound(races.size())
                    .imported(importedEvents.size())
                    .skipped(skipped)
                    .errors(errors.size())
                    .errorMessages(errors)
                    .importedEvents(importedEvents.stream()
                            .map(GrandPrixResponse::fromGrandPrix)
                            .toList())
                    .build();
                    
        } catch (Exception e) {
            log.error("Erro ao importar eventos da API jolpica-f1", e);
            throw new RuntimeException("Erro ao importar eventos: " + e.getMessage(), e);
        }
    }
    
    private GrandPrix convertRaceToGrandPrix(Race race, Integer season) {
        try {
            return GrandPrix.builder()
                    .season(season)
                    .round(Integer.parseInt(race.getRound()))
                    .name(race.getRaceName())
                    .country(race.getCircuit().getLocation().getCountry())
                    .city(race.getCircuit().getLocation().getLocality())
                    .circuitName(race.getCircuit().getCircuitName())
                    .circuitUrl(race.getCircuit().getUrl())
                    .practice1DateTime(parseDateTime(race.getFirstPractice()))
                    .practice2DateTime(parseDateTime(race.getSecondPractice()))
                    .practice3DateTime(parseDateTime(race.getThirdPractice()))
                    .qualifyingDateTime(parseDateTime(race.getQualifying()))
                    .sprintDateTime(parseDateTime(race.getSprint()))
                    .raceDateTime(parseDateTime(race.getDate(), race.getTime()))
                    .active(true)
                    .completed(false)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter dados do evento " + race.getRaceName(), e);
        }
    }
    
    private LocalDateTime parseDateTime(Session session) {
        if (session == null || session.getDate() == null || session.getTime() == null) {
            return null;
        }
        return parseDateTime(session.getDate(), session.getTime());
    }
    
    private LocalDateTime parseDateTime(String date, String time) {
        if (date == null || time == null) {
            return null;
        }
        
        try {
            // Formato esperado: "2025-03-16" e "04:00:00Z"
            String dateTimeStr = date + "T" + time.replace("Z", "");
            return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (Exception e) {
            log.warn("Erro ao fazer parse da data/hora: {} {}", date, time, e);
            return null;
        }
    }
} 