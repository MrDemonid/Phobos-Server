# Dockerfile
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
VOLUME /tmp
COPY target/web-client-*.jar web-client.jar
ENTRYPOINT ["java", "-jar", "web-client.jar"]
EXPOSE 9010
