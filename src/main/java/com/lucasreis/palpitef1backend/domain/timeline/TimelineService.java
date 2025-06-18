package com.lucasreis.palpitef1backend.domain.timeline;

import com.lucasreis.palpitef1backend.domain.user.User;
import com.lucasreis.palpitef1backend.domain.grandprix.GrandPrix;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TimelineService {
    
    private final TimelineRepository timelineRepository;
    
    // Buscar timeline completa do usuário
    public List<TimelineEvent> getUserTimeline(Long userId, Integer season) {
        log.debug("Buscando timeline do usuário {} para temporada {}", userId, season);
        
        if (season != null) {
            return timelineRepository.findByUserIdAndSeasonOrderByCreatedAtDesc(userId, season);
        } else {
            return timelineRepository.findByUserIdOrderByCreatedAtDesc(userId);
        }
    }
    
    // Buscar marcos importantes da temporada
    public List<TimelineEvent> getSeasonMilestones(Long userId, Integer season) {
        log.debug("Buscando marcos da temporada {} para usuário {}", season, userId);
        
        List<TimelineEventType> milestoneTypes = Arrays.asList(
            TimelineEventType.FIRST_POINTS,
            TimelineEventType.PERSONAL_BEST,
            TimelineEventType.PERFECT_WEEKEND,
            TimelineEventType.TOP_10,
            TimelineEventType.TOP_5,
            TimelineEventType.PODIUM,
            TimelineEventType.FIRST_PLACE,
            TimelineEventType.STREAK_MILESTONE
        );
        
        return timelineRepository.findMilestonesByUserAndSeason(userId, season, milestoneTypes);
    }
    
    // Criar evento na timeline
    @Transactional
    public TimelineEvent createTimelineEvent(User user, TimelineEventType eventType, 
                                           String title, String description, 
                                           GrandPrix grandPrix, Integer pointsGained, 
                                           Integer season) {
        
        // Verificar se evento já existe para evitar duplicatas
        if (grandPrix != null && timelineRepository.existsByUserIdAndEventTypeAndGrandPrixId(
                user.getId(), eventType, grandPrix.getId())) {
            log.debug("Evento {} já existe para usuário {} no GP {}", eventType, user.getId(), grandPrix.getId());
            return null;
        }
        
        TimelineEvent event = TimelineEvent.builder()
                .user(user)
                .eventType(eventType)
                .title(title)
                .description(description)
                .grandPrix(grandPrix)
                .pointsGained(pointsGained)
                .season(season)
                .icon(eventType.getDefaultIcon())
                .color(eventType.getDefaultColor())
                .build();
        
        TimelineEvent saved = timelineRepository.save(event);
        log.info("Evento de timeline criado: {} para usuário {}", eventType, user.getId());
        
        return saved;
    }
    
    // Criar evento de primeira pontuação
    @Transactional
    public void createFirstPointsEvent(User user, GrandPrix grandPrix, Integer points, Integer season) {
        Long existingPointsEvents = timelineRepository.countByUserIdAndEventType(
            user.getId(), TimelineEventType.FIRST_POINTS);
        
        if (existingPointsEvents == 0) {
            createTimelineEvent(
                user,
                TimelineEventType.FIRST_POINTS,
                "Primeiros Pontos! 🎯",
                String.format("Conseguiu seus primeiros %d pontos no GP %s!", points, grandPrix.getName()),
                grandPrix,
                points,
                season
            );
        }
    }
    
    // Criar evento de melhor pontuação
    @Transactional
    public void createPersonalBestEvent(User user, GrandPrix grandPrix, Integer points, Integer season) {
        createTimelineEvent(
            user,
            TimelineEventType.PERSONAL_BEST,
            "Nova Melhor Pontuação! ⭐",
            String.format("Alcançou %d pontos no GP %s - seu novo recorde pessoal!", points, grandPrix.getName()),
            grandPrix,
            points,
            season
        );
    }
    
    // Criar evento de final de semana perfeito
    @Transactional
    public void createPerfectWeekendEvent(User user, GrandPrix grandPrix, Integer points, Integer season) {
        createTimelineEvent(
            user,
            TimelineEventType.PERFECT_WEEKEND,
            "Final de Semana Perfeito! 🏆",
            String.format("Acertou tudo no GP %s! Pontuação total: %d pontos", grandPrix.getName(), points),
            grandPrix,
            points,
            season
        );
    }
    
    // Criar evento de posição no ranking
    @Transactional
    public void createRankingEvent(User user, Integer position, Integer season) {
        TimelineEventType eventType;
        String title;
        String description;
        
        if (position == 1) {
            eventType = TimelineEventType.FIRST_PLACE;
            title = "1º Lugar! 👑";
            description = "Conquistou a liderança do ranking!";
        } else if (position <= 3) {
            eventType = TimelineEventType.PODIUM;
            title = "Pódio! 🥇";
            description = String.format("Alcançou a %dª posição no ranking!", position);
        } else if (position <= 5) {
            eventType = TimelineEventType.TOP_5;
            title = "Top 5! 🥈";
            description = String.format("Entrou no top 5 - %dª posição!", position);
        } else if (position <= 10) {
            eventType = TimelineEventType.TOP_10;
            title = "Top 10! 🥉";
            description = String.format("Alcançou o top 10 - %dª posição!", position);
        } else {
            return; // Não criar evento para posições abaixo do top 10
        }
        
        createTimelineEvent(
            user,
            eventType,
            title,
            description,
            null,
            null,
            season
        );
    }
    
    // Criar evento de sequência
    @Transactional
    public void createStreakEvent(User user, Integer streakCount, Integer season) {
        TimelineEventType eventType;
        String title;
        String description;
        
        if (streakCount == 2) {
            eventType = TimelineEventType.STREAK_START;
            title = "Sequência Iniciada! 🔥";
            description = "Começou uma sequência de palpites corretos!";
        } else if (streakCount % 5 == 0) {
            eventType = TimelineEventType.STREAK_MILESTONE;
            title = String.format("Sequência de %d! 📈", streakCount);
            description = String.format("Mantém %d palpites corretos consecutivos!", streakCount);
        } else {
            return; // Só criar eventos em marcos específicos
        }
        
        createTimelineEvent(
            user,
            eventType,
            title,
            description,
            null,
            null,
            season
        );
    }
    
    // Buscar eventos recentes (últimos 30 dias)
    public List<TimelineEvent> getRecentEvents(Long userId) {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        LocalDateTime now = LocalDateTime.now();
        
        return timelineRepository.findByUserIdAndDateRange(userId, thirtyDaysAgo, now);
    }
}
 