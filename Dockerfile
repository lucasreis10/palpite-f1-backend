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
ENV JAVA_TOOL_OPTIONS="-XX:+UseParallelGC -XX:MaxRAMPercentage=75 -Xss512k -XX:+UseStringDeduplication"

HEALTHCHECK --interval=10s --timeout=5s --start-period=60s --retries=5 \
  CMD curl -v http://localhost:8081/api/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar", "--debug"]
