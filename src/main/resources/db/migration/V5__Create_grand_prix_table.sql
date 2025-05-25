CREATE TABLE grand_prix (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    season INT NOT NULL,
    round INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL,
    circuit_name VARCHAR(100) NOT NULL,
    circuit_url VARCHAR(500),
    
    -- Horários dos eventos (em UTC)
    practice1_datetime TIMESTAMP NULL,
    practice2_datetime TIMESTAMP NULL,
    practice3_datetime TIMESTAMP NULL,
    qualifying_datetime TIMESTAMP NULL,
    sprint_datetime TIMESTAMP NULL,
    race_datetime TIMESTAMP NOT NULL,
    
    -- Informações adicionais
    timezone VARCHAR(10),
    laps INT,
    circuit_length DECIMAL(4,2), -- Ex: 5.89 km
    description TEXT,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Unique constraints
    CONSTRAINT uk_grand_prix_season_round UNIQUE (season, round),
    CONSTRAINT uk_grand_prix_season_name UNIQUE (season, name),
    
    -- Check constraints
    CONSTRAINT chk_grand_prix_season_range CHECK (season >= 2020 AND season <= 2030),
    CONSTRAINT chk_grand_prix_round_range CHECK (round >= 1 AND round <= 30),
    CONSTRAINT chk_grand_prix_laps_range CHECK (laps IS NULL OR (laps >= 1 AND laps <= 100)),
    CONSTRAINT chk_grand_prix_circuit_length CHECK (circuit_length IS NULL OR (circuit_length >= 0.1 AND circuit_length <= 10.0))
);

-- Indexes for better performance
CREATE INDEX idx_grand_prix_season ON grand_prix(season);
CREATE INDEX idx_grand_prix_season_round ON grand_prix(season, round);
CREATE INDEX idx_grand_prix_country ON grand_prix(country);
CREATE INDEX idx_grand_prix_race_datetime ON grand_prix(race_datetime);
CREATE INDEX idx_grand_prix_completed ON grand_prix(completed);
CREATE INDEX idx_grand_prix_active ON grand_prix(active);
CREATE INDEX idx_grand_prix_season_active ON grand_prix(season, active);
CREATE INDEX idx_grand_prix_upcoming ON grand_prix(completed, race_datetime); 