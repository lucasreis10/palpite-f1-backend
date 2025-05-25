-- Tabela principal de palpites
CREATE TABLE guesses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    grand_prix_id BIGINT NOT NULL,
    guess_type VARCHAR(20) NOT NULL,
    score DECIMAL(10,3),
    calculated BOOLEAN NOT NULL DEFAULT FALSE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Foreign keys
    CONSTRAINT fk_guesses_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_guesses_grand_prix FOREIGN KEY (grand_prix_id) REFERENCES grand_prix(id) ON DELETE CASCADE,
    
    -- Unique constraint: um usuário só pode ter um palpite por GP e tipo
    CONSTRAINT uk_guesses_user_gp_type UNIQUE (user_id, grand_prix_id, guess_type),
    
    -- Check constraints
    CONSTRAINT chk_guesses_type CHECK (guess_type IN ('QUALIFYING', 'RACE')),
    CONSTRAINT chk_guesses_score CHECK (score IS NULL OR score >= 0)
);

-- Tabela para armazenar os pilotos do palpite do usuário (em ordem)
CREATE TABLE guess_pilots (
    guess_id BIGINT NOT NULL,
    pilot_id BIGINT NOT NULL,
    position INT NOT NULL,
    
    -- Foreign keys
    CONSTRAINT fk_guess_pilots_guess FOREIGN KEY (guess_id) REFERENCES guesses(id) ON DELETE CASCADE,
    CONSTRAINT fk_guess_pilots_pilot FOREIGN KEY (pilot_id) REFERENCES pilots(id) ON DELETE CASCADE,
    
    -- Primary key composta
    PRIMARY KEY (guess_id, position),
    
    -- Unique constraint: cada piloto só pode aparecer uma vez por palpite
    CONSTRAINT uk_guess_pilots_guess_pilot UNIQUE (guess_id, pilot_id)
);

-- Tabela para armazenar o resultado real (em ordem) - preenchido pelo admin
CREATE TABLE guess_real_results (
    guess_id BIGINT NOT NULL,
    pilot_id BIGINT NOT NULL,
    position INT NOT NULL,
    
    -- Foreign keys
    CONSTRAINT fk_guess_real_results_guess FOREIGN KEY (guess_id) REFERENCES guesses(id) ON DELETE CASCADE,
    CONSTRAINT fk_guess_real_results_pilot FOREIGN KEY (pilot_id) REFERENCES pilots(id) ON DELETE CASCADE,
    
    -- Primary key composta
    PRIMARY KEY (guess_id, position),
    
    -- Unique constraint: cada piloto só pode aparecer uma vez por resultado
    CONSTRAINT uk_guess_real_results_guess_pilot UNIQUE (guess_id, pilot_id)
);

-- Índices para performance
CREATE INDEX idx_guesses_user_id ON guesses(user_id);
CREATE INDEX idx_guesses_grand_prix_id ON guesses(grand_prix_id);
CREATE INDEX idx_guesses_type ON guesses(guess_type);
CREATE INDEX idx_guesses_calculated ON guesses(calculated);
CREATE INDEX idx_guesses_score ON guesses(score);
CREATE INDEX idx_guesses_user_season ON guesses(user_id, grand_prix_id);
CREATE INDEX idx_guesses_gp_type ON guesses(grand_prix_id, guess_type);
CREATE INDEX idx_guesses_active ON guesses(active);

-- Índices para as tabelas de pilotos
CREATE INDEX idx_guess_pilots_pilot_id ON guess_pilots(pilot_id);
CREATE INDEX idx_guess_real_results_pilot_id ON guess_real_results(pilot_id); 