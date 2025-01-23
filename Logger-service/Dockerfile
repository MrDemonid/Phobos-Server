# Dockerfile
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
VOLUME /tmp
COPY target/logger-service-*.jar logger-service.jar
ENTRYPOINT ["java", "-jar", "logger-service.jar"]
EXPOSE 9070
