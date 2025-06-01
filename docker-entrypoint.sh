#!/bin/sh
set -e

echo "==================================="
echo "Starting Palpite F1 Backend"
echo "==================================="
echo "PORT: ${PORT:-8081}"
echo "SPRING_PROFILES_ACTIVE: ${SPRING_PROFILES_ACTIVE}"
echo "DATABASE_URL: ${DATABASE_URL}"
echo "DATABASE_USERNAME: ${DATABASE_USERNAME}"
echo "==================================="

# Railway fornece a variável PORT
export SERVER_PORT=${PORT:-8081}

echo "Starting application on port $SERVER_PORT..."

# Executar a aplicação com logs detalhados
exec java $JAVA_OPTS \
    -Dserver.port=$SERVER_PORT \
    -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:-prod} \
    -jar app.jar 