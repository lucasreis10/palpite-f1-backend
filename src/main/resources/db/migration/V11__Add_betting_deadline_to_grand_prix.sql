-- Adicionar coluna betting_deadline na tabela grand_prix
ALTER TABLE grand_prix
ADD COLUMN betting_deadline TIMESTAMP NULL AFTER race_datetime;

-- Atualizar os registros existentes com o prazo padrão (sexta-feira 22h antes do GP)
UPDATE grand_prix
SET betting_deadline = DATE_SUB(
    DATE_SUB(race_datetime, 
        INTERVAL (DAYOFWEEK(race_datetime) + 5) % 7 DAY
    ),
    INTERVAL 2 HOUR -- Ajustar para 22h
)
WHERE betting_deadline IS NULL; 