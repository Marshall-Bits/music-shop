# Stage 1: Build
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre
WORKDIR /app

# Run as non-root for security
RUN addgroup --system appgroup && adduser --system --ingroup appgroup appuser

# Copy the JAR from the build stage
COPY --from=build /app/target/render-example-*.jar app.jar

# Document the port Spring Boot listens on
EXPOSE 8080

# Health check using Spring Boot Actuator
HEALTHCHECK --interval=30s --timeout=5s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# JVM tuning for containers
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

USER appuser

# Run the application when the container starts
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]