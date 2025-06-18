package com.lucasreis.palpitef1backend.domain.timeline;

public enum TimelineEventType {
    // Marcos de pontuação
    FIRST_POINTS("Primeiros Pontos", "🎯", "#4CAF50"),
    PERSONAL_BEST("Melhor Pontuação", "⭐", "#FFD700"),
    PERFECT_WEEKEND("Final de Semana Perfeito", "🏆", "#FF6B35"),
    
    // Sequências (Streaks)
    STREAK_START("Início de Sequência", "🔥", "#FF5722"),
    STREAK_MILESTONE("Marco de Sequência", "📈", "#2196F3"),
    STREAK_BROKEN("Sequência Quebrada", "💔", "#9E9E9E"),
    
    // Posições no ranking
    TOP_10("Top 10", "🥉", "#CD7F32"),
    TOP_5("Top 5", "🥈", "#C0C0C0"),
    PODIUM("Pódio", "🥇", "#FFD700"),
    FIRST_PLACE("1º Lugar", "👑", "#4CAF50"),
    
    // Eventos especiais
    MILESTONE_RACE("Corrida Marco", "🏁", "#673AB7"),
    UPSET_PREDICTION("Palpite Improvável", "🎲", "#E91E63"),
    COMEBACK("Recuperação", "💪", "#FF9800"),
    
    // Análises técnicas
    RAIN_MASTER("Mestre da Chuva", "🌧️", "#03A9F4"),
    QUALIFYING_EXPERT("Expert em Qualifying", "⏱️", "#8BC34A"),
    STREET_CIRCUIT_KING("Rei dos Circuitos Urbanos", "🏙️", "#795548"),
    
    // Eventos sociais
    FRIEND_INVITED("Amigo Convidado", "👥", "#9C27B0"),
    LEAGUE_CREATED("Liga Criada", "🏟️", "#FF5722"),
    HEAD_TO_HEAD_WIN("Vitória Head-to-Head", "⚔️", "#F44336");
    
    private final String displayName;
    private final String defaultIcon;
    private final String defaultColor;
    
    TimelineEventType(String displayName, String defaultIcon, String defaultColor) {
        this.displayName = displayName;
        this.defaultIcon = defaultIcon;
        this.defaultColor = defaultColor;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDefaultIcon() {
        return defaultIcon;
    }
    
    public String getDefaultColor() {
        return defaultColor;
    }
} 