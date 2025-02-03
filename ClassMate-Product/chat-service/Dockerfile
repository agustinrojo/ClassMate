# Etapa 1: Construcción del JAR
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: Ejecutar la aplicación
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/chat-service-0.0.1-SNAPSHOT.jar chat_service.jar
EXPOSE 8088
ENTRYPOINT ["java", "-jar", "chat_service.jar"]