FROM ubuntu:latest
LABEL authors="Santiago Ortiz Reverte"

FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/authentication-service-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} auth_service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "auth_service.jar"]