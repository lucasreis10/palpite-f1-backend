# Endpoints do Sistema de Calendário - Grandes Prêmios

## Visão Geral
Sistema completo para gerenciamento do calendário de Grandes Prêmios da Fórmula 1, incluindo informações detalhadas sobre datas, horários, circuitos e status dos eventos.

## Endpoints Disponíveis

### 📅 **Consultas Gerais**

#### `GET /api/grand-prix`
Lista todos os Grandes Prêmios cadastrados no sistema.

#### `GET /api/grand-prix/seasons`
Retorna lista das temporadas disponíveis no sistema.

#### `GET /api/grand-prix/{id}`
Busca um Grande Prêmio específico por ID.

---

### 🏁 **Consultas por Temporada**

#### `GET /api/grand-prix/season/{season}`
Lista todos os GPs de uma temporada específica, ordenados por rodada.

#### `GET /api/grand-prix/season/{season}/active`
Lista apenas os GPs ativos de uma temporada.

#### `GET /api/grand-prix/season/{season}/completed`
Lista apenas os GPs já realizados de uma temporada.

#### `GET /api/grand-prix/season/{season}/pending`
Lista apenas os GPs pendentes (não realizados) de uma temporada.

#### `GET /api/grand-prix/season/{season}/sprint`
Lista apenas os fins de semana de sprint de uma temporada.

#### `GET /api/grand-prix/season/{season}/round/{round}`
Busca um GP específico por temporada e rodada.

---

### 🌍 **Consultas por Localização**

#### `GET /api/grand-prix/country/{country}`
Lista todos os GPs de um país específico (histórico completo).

#### `GET /api/grand-prix/search/circuit?name={circuitName}`
Busca GPs por nome do circuito (busca parcial).

---

### ⏰ **Consultas por Tempo**

#### `GET /api/grand-prix/upcoming`
Lista próximos GPs (não concluídos com data futura).

#### `GET /api/grand-prix/next`
Retorna o próximo GP a ser realizado.

#### `GET /api/grand-prix/recent`
Lista os últimos 10 GPs realizados.

---

### ✏️ **Operações de Criação e Edição**

#### `POST /api/grand-prix`
Cria um novo Grande Prêmio.

**Exemplo de payload:**
```json
{
  "season": 2025,
  "round": 1,
  "name": "Bahrain Grand Prix",
  "country": "Bahrain",
  "city": "Sakhir",
  "circuitName": "Bahrain International Circuit",
  "circuitUrl": "https://en.wikipedia.org/wiki/Bahrain_International_Circuit",
  "practice1DateTime": "2025-03-14T11:30:00",
  "practice2DateTime": "2025-03-14T15:00:00",
  "practice3DateTime": "2025-03-15T12:30:00",
  "qualifyingDateTime": "2025-03-15T16:00:00",
  "sprintDateTime": null,
  "raceDateTime": "2025-03-16T15:00:00",
  "timezone": "UTC+3",
  "laps": 57,
  "circuitLength": 5.41,
  "description": "Abertura da temporada 2025 de Fórmula 1."
}
```

#### `POST /api/grand-prix/batch`
Cria múltiplos Grandes Prêmios em uma única operação.

**Exemplo de payload:**
```json
{
  "grandPrix": [
    {
      "season": 2025,
      "round": 1,
      "name": "Bahrain Grand Prix",
      // ... outros campos
    },
    {
      "season": 2025,
      "round": 2,
      "name": "Saudi Arabian Grand Prix",
      // ... outros campos
    }
  ]
}
```

#### `PUT /api/grand-prix/{id}`
Atualiza um Grande Prêmio existente (campos opcionais).

#### `DELETE /api/grand-prix/{id}`
Remove um Grande Prêmio do sistema.

---

### 🏆 **Operações de Status**

#### `PATCH /api/grand-prix/{id}/complete`
Marca um GP como concluído.

#### `PATCH /api/grand-prix/{id}/pending`
Marca um GP como pendente (não concluído).

---

## 📊 **Estrutura de Resposta**

### GrandPrixResponse
```json
{
  "id": 1,
  "season": 2025,
  "round": 1,
  "name": "Bahrain Grand Prix",
  "country": "Bahrain",
  "city": "Sakhir",
  "circuitName": "Bahrain International Circuit",
  "circuitUrl": "https://en.wikipedia.org/wiki/Bahrain_International_Circuit",
  "fullName": "Bahrain Grand Prix - Bahrain",
  "practice1DateTime": "2025-03-14T11:30:00",
  "practice2DateTime": "2025-03-14T15:00:00",
  "practice3DateTime": "2025-03-15T12:30:00",
  "qualifyingDateTime": "2025-03-15T16:00:00",
  "sprintDateTime": null,
  "raceDateTime": "2025-03-16T15:00:00",
  "timezone": "UTC+3",
  "laps": 57,
  "circuitLength": 5.41,
  "description": "Abertura da temporada 2025 de Fórmula 1.",
  "active": true,
  "completed": false,
  "isSprintWeekend": false,
  "createdAt": "2025-05-25T15:19:47",
  "updatedAt": "2025-05-25T15:19:47"
}
```

### CreateGrandPrixBatchResponse
```json
{
  "totalRequested": 3,
  "totalCreated": 3,
  "totalErrors": 0,
  "created": [/* array de GrandPrixResponse */],
  "errors": [/* array de strings com erros */]
}
```

---

## 🔒 **Validações e Regras de Negócio**

### Validações de Criação:
- **Temporada**: Entre 2020 e 2030
- **Rodada**: Entre 1 e 30, única por temporada
- **Nome**: Único por temporada, 3-100 caracteres
- **País/Cidade**: 2-100 caracteres
- **Circuito**: 3-100 caracteres
- **Data da corrida**: Obrigatória
- **Voltas**: 1-100 (opcional)
- **Comprimento**: 0.1-10.0 km (opcional)

### Constraints Únicos:
- Combinação temporada + rodada
- Combinação temporada + nome

### Funcionalidades Especiais:
- **Sprint Weekend**: Detectado automaticamente quando `sprintDateTime` está preenchido
- **Fuso Horário**: Suporte a diferentes fusos horários
- **Status**: Controle de GPs ativos/inativos e concluídos/pendentes
- **Busca Inteligente**: Por nome do circuito, país, etc.

---

## 🚀 **Exemplos de Uso**

### Calendário Completo de uma Temporada:
```bash
curl -X GET https://javaspringboot-production-a2d3.up.railway.app/api/grand-prix/season/2025
```

### Próximos Fins de Semana de Sprint:
```bash
curl -X GET https://javaspringboot-production-a2d3.up.railway.app/api/grand-prix/season/2025/sprint
```

### Criar Calendário Completo:
```bash
curl -X POST https://javaspringboot-production-a2d3.up.railway.app/api/grand-prix/batch \
  -H "Content-Type: application/json" \
  -d @calendario_2025.json
```

### Marcar GP como Realizado:
```bash
curl -X PATCH https://javaspringboot-production-a2d3.up.railway.app/api/grand-prix/1/complete
```

---

## 📈 **Códigos de Status HTTP**

- **200 OK**: Consulta bem-sucedida
- **201 Created**: GP criado com sucesso
- **204 No Content**: Deleção bem-sucedida
- **207 Multi-Status**: Criação em lote com erros parciais
- **400 Bad Request**: Dados inválidos
- **404 Not Found**: GP não encontrado
- **500 Internal Server Error**: Erro de validação de negócio

---

## 🗄️ **Banco de Dados**

### Tabela: `grand_prix`
- Índices otimizados para consultas por temporada, país, data
- Constraints de integridade para evitar duplicatas
- Suporte a timestamps automáticos
- Campos opcionais para flexibilidade 