# Sistema de Palpites - Endpoints

## Visão Geral
Sistema completo para gerenciamento de palpites de qualifying e corrida da Fórmula 1, incluindo cálculo automático de pontuações baseado nas regras específicas fornecidas.

## Fluxo Principal
1. **Usuário cria palpite** antes do evento (qualifying/corrida)
2. **Administrador define resultado real** após o evento
3. **Sistema calcula automaticamente** as pontuações de todos os palpites
4. **Rankings são gerados** automaticamente

---

## 🎯 **Endpoints para Usuários**

### `POST /api/guesses/user/{userId}`
Cria um novo palpite para qualifying ou corrida.

**Payload:**
```json
{
  "grandPrixId": 1,
  "guessType": "QUALIFYING",
  "pilotIds": [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]
}
```

**Validações:**
- Usuário só pode ter 1 palpite por GP e tipo
- Lista deve ter entre 10-20 pilotos
- Todos os pilotos devem existir e estar ativos
- Não pode haver pilotos duplicados

### `PUT /api/guesses/user/{userId}/{guessId}`
Atualiza um palpite existente (apenas se não foi calculado).

**Payload:**
```json
{
  "pilotIds": [2, 1, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]
}
```

### `GET /api/guesses/user/{userId}`
Lista todos os palpites de um usuário.

### `GET /api/guesses/user/{userId}/season/{season}`
Lista palpites de um usuário em uma temporada específica.

### `GET /api/guesses/user/{userId}/grand-prix/{grandPrixId}?guessType=QUALIFYING`
Busca palpite específico do usuário para um GP e tipo.

### `DELETE /api/guesses/user/{userId}/{guessId}`
Remove um palpite (apenas se não foi calculado).

---

## 📊 **Endpoints de Consulta**

### `GET /api/guesses/{guessId}`
Busca palpite específico por ID.

### `GET /api/guesses/grand-prix/{grandPrixId}?guessType=QUALIFYING`
Lista todos os palpites de um GP e tipo.

### `GET /api/guesses/grand-prix/{grandPrixId}/ranking?guessType=QUALIFYING`
Ranking de um GP específico (apenas palpites calculados, ordenados por pontuação).

---

## 🔧 **Endpoints Administrativos**

### `POST /api/guesses/admin/calculate-scores`
**ENDPOINT MAIS IMPORTANTE** - Define resultado real e calcula todas as pontuações.

**Payload:**
```json
{
  "grandPrixId": 1,
  "guessType": "QUALIFYING",
  "realResultPilotIds": [3, 1, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12]
}
```

**Resposta:**
```json
{
  "grandPrixId": 1,
  "grandPrixName": "Bahrain Grand Prix",
  "guessType": "QUALIFYING",
  "totalGuesses": 5,
  "calculatedGuesses": 5,
  "results": [
    {
      "id": 1,
      "user": {
        "id": 1,
        "name": "João Silva",
        "email": "joao@email.com"
      },
      "grandPrixId": 1,
      "grandPrixName": "Bahrain Grand Prix",
      "season": 2025,
      "round": 1,
      "guessType": "QUALIFYING",
      "pilots": [/* lista ordenada dos pilotos do palpite */],
      "realResultPilots": [/* resultado real */],
      "score": 15.234,
      "calculated": true,
      "active": true,
      "createdAt": "2025-03-14T10:00:00",
      "updatedAt": "2025-03-16T16:30:00"
    }
  ],
  "message": "Pontuações calculadas com sucesso"
}
```

---

## 🏆 **Sistema de Pontuação**

### **Qualifying (12 posições)**
Baseado no código TypeScript fornecido:
- **1º lugar**: 5.0, 4.25, 3.612 pontos (se palpitou 1º, 2º, 3º)
- **2º lugar**: 4.25, 5.0, 4.25, 2.89 pontos
- **3º lugar**: 3.612, 4.25, 5.0, 3.4, 2.89 pontos
- E assim por diante...

### **Corrida (14 posições)**
Pontuação mais alta:
- **1º lugar**: 25, 21.25, 18.062, 12.282, 10.44 pontos
- **2º lugar**: 21.25, 25, 21.25, 14.45, 12.282, 10.44 pontos
- **3º lugar**: 18.062, 21.25, 25, 17, 14.45, 12.282, 7.83 pontos
- E assim por diante...

---

## 📋 **Estrutura de Dados**

### **GuessType Enum**
```
QUALIFYING - Palpite do qualifying
RACE - Palpite da corrida
```

### **Validações de Negócio**
- ✅ Um usuário só pode ter 1 palpite por GP e tipo
- ✅ Palpites calculados não podem ser alterados/deletados
- ✅ Lista de pilotos deve ter 10-20 elementos
- ✅ Todos os pilotos devem existir e estar ativos
- ✅ Não pode haver pilotos duplicados na lista

### **Estados do Palpite**
- `calculated: false` - Palpite criado, aguardando resultado
- `calculated: true` - Pontuação calculada, resultado final
- `active: true/false` - Controle de ativação

---

## 🚀 **Exemplos de Uso**

### 1. Usuário cria palpite para qualifying
```bash
curl -X POST http://localhost:8081/api/guesses/user/1 \
  -H "Content-Type: application/json" \
  -d '{
    "grandPrixId": 1,
    "guessType": "QUALIFYING",
    "pilotIds": [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]
  }'
```

### 2. Admin define resultado e calcula pontuações
```bash
curl -X POST http://localhost:8081/api/guesses/admin/calculate-scores \
  -H "Content-Type: application/json" \
  -d '{
    "grandPrixId": 1,
    "guessType": "QUALIFYING",
    "realResultPilotIds": [3, 1, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12]
  }'
```

### 3. Ver ranking do qualifying
```bash
curl -X GET "http://localhost:8081/api/guesses/grand-prix/1/ranking?guessType=QUALIFYING"
```

---

## 🗄️ **Estrutura do Banco**

### Tabela `guesses`
- Palpite principal com referências para usuário e GP
- Constraint único: (user_id, grand_prix_id, guess_type)

### Tabela `guess_pilots`
- Lista ordenada dos pilotos do palpite do usuário
- Mantém a ordem através da coluna `position`

### Tabela `guess_real_results`
- Lista ordenada do resultado real (preenchido pelo admin)
- Usado para calcular as pontuações

---

## ⚡ **Performance e Índices**
- Índices otimizados para consultas por usuário, GP, tipo e pontuação
- Queries eficientes para rankings e estatísticas
- Transações para garantir consistência nos cálculos

---

## 🔒 **Códigos de Status HTTP**
- **201 Created**: Palpite criado com sucesso
- **200 OK**: Consulta/atualização bem-sucedida
- **204 No Content**: Deleção bem-sucedida
- **400 Bad Request**: Dados inválidos ou regra de negócio violada
- **404 Not Found**: Palpite/GP/Usuário não encontrado
- **500 Internal Server Error**: Erro interno do servidor 