FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/service-registry-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} service_registry.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "service_registry.jar"]