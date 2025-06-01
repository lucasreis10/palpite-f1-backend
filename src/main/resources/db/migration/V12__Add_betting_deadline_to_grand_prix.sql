-- Adicionar coluna betting_deadline na tabela grand_prix
ALTER TABLE grand_prix
ADD COLUMN betting_deadline TIMESTAMP NULL AFTER race_datetime;

-- Por enquanto, vamos deixar os valores nulos
-- A aplicação irá calcular automaticamente quando necessário 