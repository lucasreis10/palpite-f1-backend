
FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts gradlew ./
COPY gradle gradle

RUN chmod +x gradlew

RUN ./gradlew dependencies --no-daemon

COPY src src

RUN ./gradlew clean build -x test --no-daemon


FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

RUN apk add --no-cache curl

RUN addgroup -g 1001 -S spring && \
    adduser -S spring -u 1001

COPY --from=build /app/build/libs/*.jar app.jar

USER spring:spring

EXPOSE 8081

HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/api/health || exit 1

CMD ["java", "-jar", "app.jar"]
