# -----------------------------
# 1 — Build da aplicação (DevOps)
# -----------------------------
FROM eclipse-temurin:17-jdk-jammy AS build

WORKDIR /app

# Instala Git e Maven
RUN apt-get update && apt-get install -y git maven

# Clona o repositório do código-fonte (Java)
RUN git clone https://github.com/Lucas-Borges27/duckBill-Java.git .

# Compila o projeto com Maven
RUN mvn clean package -DskipTests

# -----------------------------
# 2 — Runtime leve e seguro
# -----------------------------
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copia o jar gerado do estágio anterior
COPY --from=build /app/target/*.jar app.jar

# Cria usuário não-root (boa prática)
RUN useradd -ms /bin/bash appuser
USER appuser

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
