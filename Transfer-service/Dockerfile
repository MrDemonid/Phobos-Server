# Dockerfile
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
VOLUME /tmp
COPY target/transfer-service-*.jar transfer-service.jar
ENTRYPOINT ["java", "-jar", "transfer-service.jar"]
EXPOSE 9050
