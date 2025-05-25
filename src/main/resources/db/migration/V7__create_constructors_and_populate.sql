-- Criar tabela de escuderias
CREATE TABLE constructors (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    constructor_id VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    nationality VARCHAR(255),
    url VARCHAR(500),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Adicionar coluna constructor_id na tabela pilots
ALTER TABLE pilots ADD COLUMN constructor_id BIGINT;
ALTER TABLE pilots ADD CONSTRAINT fk_pilots_constructor FOREIGN KEY (constructor_id) REFERENCES constructors(id);

-- Popular escuderias de F1 2024
INSERT INTO constructors (constructor_id, name, nationality, url, active) VALUES
('red_bull', 'Red Bull Racing', 'Austrian', 'http://en.wikipedia.org/wiki/Red_Bull_Racing', true),
('ferrari', 'Ferrari', 'Italian', 'http://en.wikipedia.org/wiki/Scuderia_Ferrari', true),
('mercedes', 'Mercedes', 'German', 'http://en.wikipedia.org/wiki/Mercedes-Benz_in_Formula_One', true),
('mclaren', 'McLaren', 'British', 'http://en.wikipedia.org/wiki/McLaren', true),
('aston_martin', 'Aston Martin', 'British', 'http://en.wikipedia.org/wiki/Aston_Martin_in_Formula_One', true),
('alpine', 'Alpine F1 Team', 'French', 'http://en.wikipedia.org/wiki/Alpine_F1_Team', true),
('williams', 'Williams', 'British', 'http://en.wikipedia.org/wiki/Williams_Grand_Prix_Engineering', true),
('rb', 'RB F1 Team', 'Italian', 'http://en.wikipedia.org/wiki/RB_F1_Team', true),
('kick_sauber', 'Kick Sauber', 'Swiss', 'http://en.wikipedia.org/wiki/Sauber_Motorsport', true),
('haas', 'Haas F1 Team', 'American', 'http://en.wikipedia.org/wiki/Haas_F1_Team', true); 