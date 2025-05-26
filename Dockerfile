# syntax=docker/dockerfile:1

########################
# Etapa de build
########################
FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

# Copia apenas os arquivos de configuração primeiro (para cache)
COPY build.gradle.kts settings.gradle.kts gradlew ./
COPY gradle gradle

# Garante que o gradlew é executável
RUN chmod +x gradlew

# Download das dependências (será cacheado se não mudar)
RUN ./gradlew dependencies --no-daemon

# Copia o código fonte
COPY src src

# Compila a aplicação
RUN ./gradlew clean build -x test --no-daemon

########################
# Imagem final, só com o JRE
########################
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Instala curl para healthcheck
RUN apk add --no-cache curl

# Cria usuário não-root
RUN addgroup -g 1001 -S spring && \
    adduser -S spring -u 1001

# Copia o artefato pronto da etapa anterior
COPY --from=build /app/build/libs/*.jar app.jar

# Muda para usuário não-root
USER spring:spring

# Expõe a porta
EXPOSE 8081

# Healthcheck
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/api/health || exit 1

# Sobe a aplicação
CMD ["java", "-jar", "app.jar"]
