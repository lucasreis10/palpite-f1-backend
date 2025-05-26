# syntax=docker/dockerfile:1

########################
# Etapa de build
########################
FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

# Copia o projeto
COPY . .

# Garante que o gradlew é executável (caso o bit não venha no git)
RUN chmod +x gradlew

# Compila sem rodar testes
RUN ./gradlew clean build -x test --no-daemon

########################
# Imagem final, só com o JRE
########################
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copia o artefato pronto da etapa anterior
COPY --from=build /app/build/libs/*.jar app.jar

# Sobe a aplicação
CMD ["java", "-jar", "app.jar"]
