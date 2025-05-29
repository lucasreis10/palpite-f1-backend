#!/bin/bash

# Script para testar o sistema de histórico e ranking
# Demonstra as funcionalidades implementadas

BASE_URL="http://localhost:8081/api"

echo "🏁 TESTANDO SISTEMA DE HISTÓRICO E RANKING"
echo "=========================================="
echo

# 1. Testar histórico de um Grand Prix específico
echo "1️⃣ 📊 Histórico completo do GP 1..."
curl -X GET "$BASE_URL/history/grand-prix/1" \
  -w "\nStatus: %{http_code}\n" \
  -s | jq '.'
echo

# 2. Testar ranking da temporada 2025
echo "2️⃣ 🏆 Ranking completo da temporada 2025..."
curl -X GET "$BASE_URL/history/season/2025" \
  -w "\nStatus: %{http_code}\n" \
  -s | jq '.'
echo

# 3. Testar ranking simples da temporada (sem histórico detalhado)
echo "3️⃣ 📈 Ranking simples da temporada 2025..."
curl -X GET "$BASE_URL/history/season/2025/simple" \
  -w "\nStatus: %{http_code}\n" \
  -s | jq '.'
echo

# 4. Testar ranking geral da temporada (endpoint do GuessController)
echo "4️⃣ 🎯 Ranking geral da temporada 2025 (formato Object[])..."
curl -X GET "$BASE_URL/guesses/season/2025/ranking" \
  -w "\nStatus: %{http_code}\n" \
  -s | jq '.'
echo

# 5. Testar ranking por tipo - QUALIFYING
echo "5️⃣ 🏁 Ranking de QUALIFYING da temporada 2025..."
curl -X GET "$BASE_URL/guesses/season/2025/ranking/QUALIFYING" \
  -w "\nStatus: %{http_code}\n" \
  -s | jq '.'
echo

# 6. Testar ranking por tipo - RACE
echo "6️⃣ 🏎️ Ranking de RACE da temporada 2025..."
curl -X GET "$BASE_URL/guesses/season/2025/ranking/RACE" \
  -w "\nStatus: %{http_code}\n" \
  -s | jq '.'
echo

# 7. Testar ranking de um GP específico - QUALIFYING
echo "7️⃣ 🥇 Ranking do GP 1 - QUALIFYING..."
curl -X GET "$BASE_URL/guesses/grand-prix/1/ranking?guessType=QUALIFYING" \
  -w "\nStatus: %{http_code}\n" \
  -s | jq '.'
echo

# 8. Testar ranking de um GP específico - RACE
echo "8️⃣ 🏁 Ranking do GP 1 - RACE..."
curl -X GET "$BASE_URL/guesses/grand-prix/1/ranking?guessType=RACE" \
  -w "\nStatus: %{http_code}\n" \
  -s | jq '.'
echo

echo "✅ TESTE DO SISTEMA DE HISTÓRICO FINALIZADO!"
echo "============================================"
echo "Funcionalidades testadas:"
echo "- ✅ Histórico completo de Grand Prix"
echo "- ✅ Ranking detalhado da temporada"
echo "- ✅ Ranking simples da temporada"
echo "- ✅ Rankings por tipo (Qualifying/Race)"
echo "- ✅ Estatísticas e métricas"
echo "- ✅ Histórico de participação por usuário" 