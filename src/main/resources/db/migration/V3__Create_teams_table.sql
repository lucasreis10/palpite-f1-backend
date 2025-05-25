CREATE TABLE teams (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    year INT NOT NULL,
    user1_id BIGINT NOT NULL,
    user2_id BIGINT NOT NULL,
    total_score INT NOT NULL DEFAULT 0,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Foreign keys
    CONSTRAINT fk_teams_user1 FOREIGN KEY (user1_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_teams_user2 FOREIGN KEY (user2_id) REFERENCES users(id) ON DELETE CASCADE,
    
    -- Unique constraints
    CONSTRAINT uk_teams_name_year UNIQUE (name, year),
    CONSTRAINT uk_teams_user1_year UNIQUE (user1_id, year),
    CONSTRAINT uk_teams_user2_year UNIQUE (user2_id, year),
    
    -- Check constraints
    CONSTRAINT chk_teams_different_users CHECK (user1_id != user2_id),
    CONSTRAINT chk_teams_year_range CHECK (year >= 2020 AND year <= 2030),
    CONSTRAINT chk_teams_total_score CHECK (total_score >= 0)
);

-- Indexes for better performance
CREATE INDEX idx_teams_year ON teams(year);
CREATE INDEX idx_teams_year_active ON teams(year, active);
CREATE INDEX idx_teams_total_score ON teams(total_score DESC);
CREATE INDEX idx_teams_user1_id ON teams(user1_id);
CREATE INDEX idx_teams_user2_id ON teams(user2_id); 