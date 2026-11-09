# Maven et Corretto 17 compilent et testent le projet.
FROM maven:3-amazoncorretto-17-alpine AS build
WORKDIR /app
COPY pom.xml ./
COPY src ./src
# Profil de test, sans dépendre de Consul.
RUN mvn -B -Dspring.profiles.active=test \
    -Dspring.cloud.consul.enabled=false \
    -Dspring.cloud.consul.discovery.enabled=false \
    clean verify

# Image finale : Corretto 17 et le JAR, sans Maven.
FROM amazoncorretto:17-alpine-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
# Port interne, non publié par cette instruction.
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
