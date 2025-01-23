# Dockerfile
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
VOLUME /tmp
COPY target/auth-server-*.jar auth-server.jar
ENTRYPOINT ["java", "-jar", "auth-server.jar"]
EXPOSE 8090
