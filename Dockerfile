# Multi-stage build with outdated base image
FROM openjdk:11-jdk-slim as builder

WORKDIR /app

# Copy gradle files
COPY build.gradle settings.gradle gradle.properties ./
COPY gradle/ ./gradle/
COPY gradlew ./

# Copy source code
COPY common/ ./common/
COPY weather-api/ ./weather-api/
COPY data-collector/ ./data-collector/

# Build the application
RUN ./gradlew clean build -x test

# Runtime stage with outdated JRE
FROM openjdk:11-jre-slim

WORKDIR /app

# Create non-root user
RUN groupadd -r weather && useradd -r -g weather weather

# Copy the built JAR
COPY --from=builder /app/weather-api/build/libs/weather-api-*.jar app.jar

# Create logs directory
RUN mkdir -p logs && chown weather:weather logs

# Expose port
EXPOSE 8080 8081

# Switch to non-root user
USER weather

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8081/healthcheck || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar", "server", "application.yml"]