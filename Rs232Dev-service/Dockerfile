# Dockerfile
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
VOLUME /tmp
COPY target/rs232dev-service-*.jar rs232dev-service.jar
ENTRYPOINT ["java", "-jar", "rs232dev-service.jar"]
EXPOSE 9060
