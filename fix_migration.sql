-- Script para reparar problemas com a migration V11

-- Verificar se a coluna betting_deadline existe
SELECT COUNT(*) AS column_exists
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = 'palpite_f1'
AND TABLE_NAME = 'grand_prix'
AND COLUMN_NAME = 'betting_deadline';

-- Se a coluna não existir, executar:
-- ALTER TABLE grand_prix ADD COLUMN betting_deadline TIMESTAMP NULL AFTER race_datetime;

-- Verificar o status das migrations do Flyway
SELECT * FROM flyway_schema_history ORDER BY installed_rank DESC LIMIT 10;

-- Se a migration V11 estiver com erro, marcar como sucesso:
-- UPDATE flyway_schema_history SET success = 1 WHERE version = '11';

-- Ou deletar a migration problemática para rodar novamente:
-- DELETE FROM flyway_schema_history WHERE version = '11'; 