FROM openjdk:17-jdk-slim

LABEL maintainer="LLM API Team"
LABEL description="Large Language Model API using Spring Boot"

# Set working directory
WORKDIR /app

# Copy Maven files for dependency caching
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Download dependencies (this layer will be cached)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Expose port
EXPOSE 8080

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=docker
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Create non-root user for security
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar target/llm-api-1.0.0.jar"]

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8080/api/v1/llm/health || exit 1