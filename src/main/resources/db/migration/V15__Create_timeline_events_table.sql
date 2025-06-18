-- Criação da tabela timeline_events para armazenar marcos importantes dos usuários
CREATE TABLE timeline_events (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    grand_prix_id BIGINT,
    event_type VARCHAR(50) NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    points_gained INT,
    season INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    icon VARCHAR(50),
    color VARCHAR(20),
    
    CONSTRAINT FK_timeline_events_user 
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT FK_timeline_events_grand_prix 
        FOREIGN KEY (grand_prix_id) REFERENCES grand_prix(id) ON DELETE SET NULL,
    
    INDEX idx_timeline_user_season (user_id, season),
    INDEX idx_timeline_created_at (created_at),
    INDEX idx_timeline_event_type (event_type)
);

-- Comentários para documentação
ALTER TABLE timeline_events 
    COMMENT = 'Armazena eventos importantes na timeline dos usuários como primeiros pontos, recordes, etc.';

ALTER TABLE timeline_events 
    MODIFY COLUMN event_type VARCHAR(50) NOT NULL 
    COMMENT 'Tipo do evento: FIRST_POINTS, PERSONAL_BEST, PERFECT_WEEKEND, etc.';

ALTER TABLE timeline_events 
    MODIFY COLUMN title VARCHAR(200) NOT NULL 
    COMMENT 'Título do evento exibido na timeline';

ALTER TABLE timeline_events 
    MODIFY COLUMN description TEXT 
    COMMENT 'Descrição detalhada do evento';

ALTER TABLE timeline_events 
    MODIFY COLUMN points_gained INT 
    COMMENT 'Pontos ganhos no evento (se aplicável)';

ALTER TABLE timeline_events 
    MODIFY COLUMN icon VARCHAR(50) 
    COMMENT 'Emoji ou ícone do evento';

ALTER TABLE timeline_events 
    MODIFY COLUMN color VARCHAR(20) 
    COMMENT 'Cor em hexadecimal do evento'; 