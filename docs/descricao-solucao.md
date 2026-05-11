# Descrição da Solução — Duckbill

## O que é o Duckbill

Duckbill é uma aplicação web de gestão financeira pessoal desenvolvida com Spring Boot (Java 17). Permite que usuários acompanhem suas despesas, definam metas de poupança, registrem transações de ativos de investimento, criem tarefas financeiras com alertas temporais e consultem cotações de moedas em tempo real.

O problema central que o Duckbill resolve é a dispersão do controle financeiro: hoje as pessoas usam planilhas, apps separados ou não controlam nada. O Duckbill centraliza despesas, investimentos e metas em uma interface única, com autenticação segura e API REST para integração com app mobile.

---

## Escolha do Azure DevOps e Azure App Service

### Azure DevOps
Escolhido por ser a plataforma de CI/CD integrada ao ecossistema Microsoft Azure, o que elimina fricção entre o pipeline e o ambiente de deploy. Permite:
- Definir pipelines como código (YAML) versionados junto ao repositório
- Organizar variáveis sensíveis em Library com criptografia gerenciada
- Criar environments com aprovações e gates antes do deploy
- Integrar nativamente com Azure App Service via task `AzureWebApp@1`

### Azure App Service
Escolhido por suportar deploy direto de `.jar` Spring Boot com runtime Java 17 em Linux, sem necessidade de gerenciar infraestrutura de VM ou container registry. Benefícios:
- Deploy simplificado: o pipeline envia o `.jar` e o App Service gerencia o processo Java
- Escalonamento automático configurável
- HTTPS nativo com certificado gerenciado
- Integração com Azure Monitor para logs e métricas

---

## Fluxo CI/CD completo

### Pipeline CI (`azure-pipelines-ci.yml`)

**Trigger:** push na branch `main`

| Etapa | Ferramenta | Finalidade |
|---|---|---|
| Cache Maven | task `Cache@2` | Evita baixar ~200 dependências a cada build. Chave: hash do `pom.xml`. |
| Configurar Java 17 | task `JavaToolInstaller@0` | Garante que o agente Ubuntu use JDK 17 em todos os steps Maven. |
| Build | `mvn package -DskipTests` | Compila o código e gera `target/duckbill-0.0.1-SNAPSHOT.jar`. Testes pulados aqui para separar responsabilidades. |
| Testes | `mvn test` | Executa DespesaServiceTest, MetaServiceTest, TarefaFinanceiraServiceTest e DuckbillApplicationTests. Resultados publicados no Azure DevOps (JUnit XML). |
| Copy JAR | task `CopyFiles@2` | Move o `.jar` para o diretório de staging do agente. |
| Publicar artefato | task `PublishBuildArtifacts@1` | Persiste o `.jar` como artefato `duckbill-app` disponível para o CD. |

### Pipeline CD (`azure-pipelines-cd.yml`)

**Trigger:** conclusão bem-sucedida do CI na branch `main` (resource pipeline)

| Etapa | Ferramenta | Finalidade |
|---|---|---|
| Download artefato | task `download` | Recupera o `.jar` publicado pelo CI. |
| Deploy | task `AzureWebApp@1` | Envia o `.jar` para o Azure App Service com runtime `JAVA|17-java17` (Linux). |
| Injeção de variáveis | `appSettings` na task | Injeta as variáveis de ambiente no App Service: `DATASOURCE_URL`, `DATASOURCE_USERNAME`, `DATASOURCE_PASSWORD`, `JWT_SECRET`, `JWT_EXPIRATION_MS`, `APP_CORS_ALLOWED_ORIGINS`, `SPRING_PROFILES_ACTIVE`. Todas vindas do variable group `duckbill-secrets`. |

---

## Tabelas do banco e relacionamentos

O banco Oracle é gerenciado por migrações Flyway (V1 a V5). As entidades JPA mapeiam as seguintes tabelas:

| Tabela | Campos principais | Relacionamentos |
|---|---|---|
| `USUARIO` | id, nome, email, senha (BCrypt), role, saldo | Entidade raiz — referenciada por todas as entidades do usuário |
| `CATEGORIA` | id, nome (único) | Referenciada por DESPESA |
| `DESPESA` | id, valor, moeda, data_compra, descricao | ManyToOne → USUARIO (USUARIO_ID), ManyToOne → CATEGORIA (CATEGORIA_ID) |
| `META` | id, titulo, descricao, valor_objetivo, valor_guardado, icone, cor_destaque, prazo | ManyToOne → USUARIO (USUARIO_ID) |
| `TAREFA_FINANCEIRA` | id, titulo, descricao, valor_estimado, data_limite, notificar_em, status | ManyToOne → USUARIO (USUARIO_ID) |
| `ATIVO` | id, ticker (único), tipo (STOCK/BOND/FUND), moeda_base | Referenciado por TRANSACAO_ATIVO e COTACAO_ATIVO |
| `TRANSACAO_ATIVO` | id, tipo (BUY/SELL), qtd, preco, data_negocio | ManyToOne → USUARIO (USUARIO_ID), ManyToOne → ATIVO (ATIVO_ID) |
| `COTACAO_ATIVO` | (ativo_id + data_ref) chave composta, preco_fech | EmbeddedId referenciando ATIVO |
| `COTACAO_MOEDA` | (moeda + data_ref) chave composta, valor | Chave composta sem FK externa |

---

## Endpoints principais da API

Base: `/api/v1`

| Grupo | Método | Rota | Descrição |
|---|---|---|---|
| Auth | POST | `/auth/login` | Autenticar e obter JWT |
| Auth | POST | `/auth/register` | Registrar novo usuário |
| Me | GET | `/me` | Dados do usuário autenticado |
| Usuários | GET/POST | `/usuarios` | Listar / criar usuários (admin) |
| Usuários | GET | `/usuarios/{id}` | Buscar usuário por ID |
| Categorias | GET/POST | `/categorias` | Listar / criar categorias |
| Despesas | GET/POST | `/despesas` | Listar / criar despesas |
| Despesas | GET/PUT/DELETE | `/despesas/{id}` | Detalhar / editar / excluir despesa |
| Despesas | GET | `/despesas/top3` | Top 3 categorias por gasto no mês |
| Despesas | GET | `/despesas/insights` | Insights financeiros mensais |
| Metas | GET/POST | `/metas` | Listar / criar metas |
| Metas | GET/PUT/DELETE | `/metas/{id}` | Detalhar / editar / excluir meta |
| Metas | POST | `/metas/{id}/aportes` | Registrar aporte em meta |
| Tarefas | GET/POST | `/tarefas` | Listar / criar tarefas financeiras |
| Tarefas | GET/PUT/DELETE | `/tarefas/{id}` | Detalhar / editar / excluir tarefa |
| Tarefas | GET | `/tarefas/notificacoes` | Tarefas na janela de notificação |
| Tarefas | POST | `/tarefas/{id}/concluir` | Marcar tarefa como concluída |
| Ativos | GET/POST | `/ativos` | Listar / criar ativos |
| Ativos | GET/PUT | `/ativos/{id}` | Detalhar / editar ativo |
| Transações | GET/POST | `/transacoes-ativo` | Listar / criar transações |
| Transações | GET/PUT/DELETE | `/transacoes-ativo/{id}` | Detalhar / editar / excluir transação |
| Transações | GET | `/transacoes-ativo/resumo` | Resumo consolidado da carteira |
| Cotações Ativo | GET/POST | `/cotacoes-ativo` | Listar / registrar cotações de ativo |
| Cotações Ativo | GET | `/cotacoes-ativo/{ativoId}/{dataRef}` | Cotação de ativo por data |
| Cotações Moeda | GET | `/cotacoes-moeda` | Listar cotações de moeda |
| Cotações Moeda | GET | `/cotacoes-moeda/{moeda}/{dataRef}` | Cotação de moeda por data |
| Câmbio | GET | `/cambio` | Converter valor entre moedas (AwesomeAPI) |
