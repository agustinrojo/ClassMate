FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/authentication-service-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} auth_service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar -Dspring.profiles.active=dockerembbed", "auth_service.jar"]