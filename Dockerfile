# Etapa 1: Build da aplicação
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app
COPY app/ .
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# Etapa 2: Runtime leve e seguro
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

RUN useradd -ms /bin/bash appuser
USER appuser

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
