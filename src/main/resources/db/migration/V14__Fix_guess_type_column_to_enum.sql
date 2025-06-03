-- Alterar o tipo da coluna guess_type de VARCHAR para ENUM
-- Primeiro, remover o constraint CHECK existente
ALTER TABLE guesses DROP CONSTRAINT chk_guesses_type;

-- Alterar o tipo da coluna para ENUM MySQL
ALTER TABLE guesses MODIFY COLUMN guess_type ENUM('QUALIFYING', 'RACE') NOT NULL; 