FROM ubuntu:latest
LABEL authors="Santiago Ortiz Reverte"

FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/calendar-service-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} calendar_service.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "calendar_service.jar"]