# -----------------------------
# 🏗️ Etapa 1 — Build da aplicação
# -----------------------------
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app
COPY app/ .
RUN apt-get update && apt-get install -y maven
RUN mvn clean package -DskipTests

# -----------------------------
# 🚀 Etapa 2 — Runtime leve e seguro
# -----------------------------
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Cria usuário não-root (requisito da Sprint)
RUN useradd -ms /bin/bash appuser
USER appuser

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

