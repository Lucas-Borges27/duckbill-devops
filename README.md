# DuckBill – Automação de Deploy com Docker e Cloud

## Integrantes 
- Bruno Carlos Soares RM 559250 
- Lucas Borges de Souza RM 560027 
- Pedro Henrique Rodrigues RM 560393 

##  Sobre o Projeto
Este repositório contém a **infraestrutura DevOps** da aplicação DuckBill, desenvolvida para a **Sprint 2 do curso de DevOps & Cloud Computing (FIAP)**.

O objetivo é demonstrar a criação de uma **pipeline automatizada em ambiente cloud**, utilizando **Docker**, **Docker Compose** e **build multi-stage**, capaz de:

- Clonar automaticamente o código-fonte do repositório Java;  
- Compilar a aplicação com Maven;  
- Empacotar o artefato `.jar`;  
- Executar a aplicação em um container leve;  
- Expor o serviço via porta 8080;  
- Demonstrar automação, isolamento e deploy em nuvem.

---

##  Estrutura do Repositório

```
duckbill-devops/
├── Dockerfile           # Build automatizado (git clone Maven + multi-stage)
├── docker-compose.yml   # Orquestração da aplicação
└── README.md            # Documentação do projeto
```

---

## Tecnologias Utilizadas

| Tecnologia | Finalidade |
|-------------|-------------|
| **Ubuntu Server 22.04 LTS (Oracle Cloud)** | Ambiente de execução |
| **Docker CE 28.x** | Engine de containers |
| **Docker Compose Plugin** | Orquestração multi-container |
| **Eclipse Temurin JDK 17 / JRE 17** | Base para build e execução |
| **Apache Maven** | Compilação e empacotamento do projeto Java |
| **Git** | Versionamento e clonagem automatizada do app |

---

## Pipeline Automatizada (Dockerfile)

O **Dockerfile** faz automaticamente:

1. Baixa a imagem base com JDK 17;  
2. Instala **Git** e **Maven**;  
3. Clona o repositório do backend Java;  
4. Executa o build com `mvn clean package -DskipTests`;  
5. Copia apenas o `.jar` gerado para uma nova imagem JRE 17;  
6. Executa o app com `java -jar app.jar`.

---

##  Passo a Passo Completo

### 1 - Clonar o Repositório

```bash
git clone https://github.com/Lucas-Borges27/duckbill-devops.git
cd duckbill-devops
```

---

### 2 -  Build da Imagem Docker

```bash
docker compose build --no-cache
```

---

### 3 -  Subir o Container

```bash
docker compose up -d
docker ps
```

---

### 4 -  Testar a Aplicação

**Endpoint padrão da API:**
```bash
curl http://localhost:8080/api/v1/usuarios
```

Se quiser testar diretamente pelo navegador:
```
http://<seu-ip-da-vm>:8080/api/v1/usuarios
```

---


### 5 -  Encerrar Containers

```bash
docker compose down
```

---

## Conceitos Aplicados

| Conceito | Descrição |
|-----------|------------|
| **Containerização** | Empacotamento completo da aplicação e dependências |
| **Multi-stage Build** | Redução de tamanho da imagem e separação entre build e runtime |
| **Automação DevOps** | Build automático via Dockerfile (Git + Maven) |
| **Infra as Code (IaC)** | Repositório versionado com infraestrutura |
| **Orquestração** | Docker Compose simplificando execução e rede |
| **Cloud Deployment** | Execução do container em uma VM Oracle Cloud pública |

---

## 🧾 Comandos Executados na Sprint

### 🔧 Instalação do Docker
```bash
sudo apt update -y
sudo apt install -y ca-certificates curl gnupg lsb-release
sudo install -m 0755 -d /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
echo   "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg]   https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
sudo apt update -y
sudo apt install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin
```

Verificar:
```bash
docker version
docker compose version
```

---

###  Build e Deploy

```bash
git clone https://github.com/Lucas-Borges27/duckbill-devops.git
cd duckbill-devops
docker compose build --no-cache
docker compose up -d
docker ps
```

---

###  Testes de API
```bash
curl http://localhost:8080/api/v1/usuarios
```

Se a aplicação estiver rodando na nuvem:
```bash
curl http://<ip-publico-da-vm>:8080/api/v1/usuarios
```

---

### Limpeza Final (opcional)
```bash
docker compose down -v
docker rmi -f $(docker images -q)
docker volume prune -f
```

---


## 💬 Resultados da Sprint

✅ Dockerfile automatizado com build e execução  
✅ Aplicação rodando em container na nuvem  
✅ Endpoint `/api/v1/usuarios` acessível via `curl`  
✅ Pipeline completa: **clone → build → package → run**  
✅ Deploy reproduzível e automatizado com Docker Compose  

