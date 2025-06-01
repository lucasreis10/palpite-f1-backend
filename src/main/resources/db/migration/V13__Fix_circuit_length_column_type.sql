-- Corrigir tipo da coluna circuit_length de DECIMAL para FLOAT
-- O Hibernate espera FLOAT quando usamos Double no Java

ALTER TABLE grand_prix
MODIFY COLUMN circuit_length FLOAT; 