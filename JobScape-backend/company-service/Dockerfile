# Build avec Maven et Corretto 17
FROM maven:3-amazoncorretto-17-alpine AS build
WORKDIR /app

COPY pom.xml ./
COPY src ./src

# Compilation sans exécution des tests
RUN mvn -B -DskipTests \
    -Dspring.profiles.active=test \
    -Dspring.cloud.consul.enabled=false \
    -Dspring.cloud.consul.discovery.enabled=false \
    clean verify

# Image finale : Corretto 17 sans Maven
FROM amazoncorretto:17-alpine-jdk
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]