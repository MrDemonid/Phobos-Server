# Dockerfile
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
VOLUME /tmp
COPY target/storage-service-*.jar storage-service.jar
ENTRYPOINT ["java", "-jar", "storage-service.jar"]
EXPOSE 9080
