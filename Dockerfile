FROM gradle:8.5-jdk17 AS build
WORKDIR /app
COPY . .
RUN ./gradlew clean build -x test --no-daemon

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN apk add --no-cache curl
RUN addgroup -g 1001 -S spring && \
    adduser -S spring -u 1001

COPY --from=build /app/build/libs/*.jar app.jar
USER spring:spring

EXPOSE 8081
ENV SPRING_PROFILES_ACTIVE=prod

HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
  CMD curl -f http://localhost:8081/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
