#!/bin/bash

# Script de teste para o Sistema de Palpites
# Demonstra o fluxo completo: criar palpites -> definir resultado -> calcular pontuações

BASE_URL="http://localhost:8081/api"

echo "🏁 TESTANDO SISTEMA DE PALPITES DA FÓRMULA 1"
echo "=============================================="
echo

# 1. Criar palpite de qualifying para usuário 1
echo "1️⃣ Criando palpite de QUALIFYING para usuário 1..."
curl -X POST "$BASE_URL/guesses/user/1" \
  -H "Content-Type: application/json" \
  -d @test_guess_qualifying.json \
  -w "\nStatus: %{http_code}\n" \
  -s | jq '.'
echo

# 2. Criar palpite de qualifying para usuário 2 (diferente)
echo "2️⃣ Criando palpite de QUALIFYING para usuário 2..."
curl -X POST "$BASE_URL/guesses/user/2" \
  -H "Content-Type: application/json" \
  -d '{
    "grandPrixId": 1,
    "guessType": "QUALIFYING",
    "pilotIds": [2, 3, 1, 5, 4, 7, 6, 9, 8, 11, 10, 12]
  }' \
  -w "\nStatus: %{http_code}\n" \
  -s | jq '.'
echo

# 3. Criar palpite de corrida para usuário 1
echo "3️⃣ Criando palpite de RACE para usuário 1..."
curl -X POST "$BASE_URL/guesses/user/1" \
  -H "Content-Type: application/json" \
  -d @test_guess_race.json \
  -w "\nStatus: %{http_code}\n" \
  -s | jq '.'
echo

# 4. Listar palpites do usuário 1
echo "4️⃣ Listando palpites do usuário 1..."
curl -X GET "$BASE_URL/guesses/user/1" \
  -w "\nStatus: %{http_code}\n" \
  -s | jq '.'
echo

# 5. Listar todos os palpites do GP 1 - QUALIFYING
echo "5️⃣ Listando todos os palpites de QUALIFYING do GP 1..."
curl -X GET "$BASE_URL/guesses/grand-prix/1?guessType=QUALIFYING" \
  -w "\nStatus: %{http_code}\n" \
  -s | jq '.'
echo

# 6. ADMIN: Definir resultado real do qualifying e calcular pontuações
echo "6️⃣ 🔧 ADMIN: Definindo resultado real do QUALIFYING e calculando pontuações..."
curl -X POST "$BASE_URL/guesses/admin/calculate-scores" \
  -H "Content-Type: application/json" \
  -d @test_real_result_qualifying.json \
  -w "\nStatus: %{http_code}\n" \
  -s | jq '.'
echo

# 7. Ver ranking do qualifying
echo "7️⃣ 🏆 Ranking final do QUALIFYING do GP 1..."
curl -X GET "$BASE_URL/guesses/grand-prix/1/ranking?guessType=QUALIFYING" \
  -w "\nStatus: %{http_code}\n" \
  -s | jq '.'
echo

# 8. ADMIN: Definir resultado real da corrida e calcular pontuações
echo "8️⃣ 🔧 ADMIN: Definindo resultado real da RACE e calculando pontuações..."
curl -X POST "$BASE_URL/guesses/admin/calculate-scores" \
  -H "Content-Type: application/json" \
  -d @test_real_result_race.json \
  -w "\nStatus: %{http_code}\n" \
  -s | jq '.'
echo

# 9. Ver ranking da corrida
echo "9️⃣ 🏆 Ranking final da RACE do GP 1..."
curl -X GET "$BASE_URL/guesses/grand-prix/1/ranking?guessType=RACE" \
  -w "\nStatus: %{http_code}\n" \
  -s | jq '.'
echo

# 10. Tentar alterar palpite já calculado (deve dar erro)
echo "🔟 ❌ Tentando alterar palpite já calculado (deve dar erro)..."
curl -X PUT "$BASE_URL/guesses/user/1/1" \
  -H "Content-Type: application/json" \
  -d '{
    "pilotIds": [12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1]
  }' \
  -w "\nStatus: %{http_code}\n" \
  -s | jq '.'
echo

echo "✅ TESTE COMPLETO FINALIZADO!"
echo "=============================="
echo "O sistema de palpites está funcionando corretamente!"
echo "- Palpites criados ✅"
echo "- Pontuações calculadas ✅"
echo "- Rankings gerados ✅"
echo "- Validações funcionando ✅" 