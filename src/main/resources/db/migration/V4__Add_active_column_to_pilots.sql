-- Adicionar coluna active na tabela pilots
ALTER TABLE pilots ADD COLUMN active BOOLEAN NOT NULL DEFAULT TRUE;

-- Criar índice para melhor performance nas consultas por status ativo
CREATE INDEX idx_pilots_active ON pilots(active); 