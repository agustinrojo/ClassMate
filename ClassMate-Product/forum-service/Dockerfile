# Etapa 1: Construcción del JAR
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: Ejecutar la aplicación
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/forum-service-0.0.1-SNAPSHOT.jar forum_service.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "forum_service.jar"]