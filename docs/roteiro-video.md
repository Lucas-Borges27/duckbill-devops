# Roteiro curto do vídeo (até 10 min)

1. Abertura (20s)
   - Apresentar o projeto DuckBill e o objetivo: controle financeiro + despesas por categoria + investimentos.

2. Infra e seed (40s)
   - Mostrar que o Flyway cria tabelas e insere dados.
   - Informar credenciais seed: admin e user.

3. Login e segurança (1 min)
   - Acessar `/login`, entrar como `user@duckbill.com`.
   - Mostrar que `/admin/categorias` não é acessível para USER (acesso negado).

4. Fluxo A (USER) - Dashboard mensal (2 min)
   - Acessar `/app/dashboard`.
   - Mostrar total do mês, top 3 categorias e insights.
   - Trocar o mês e observar atualização.

5. Criar despesa (1 min)
   - Acessar `/app/despesas/nova`.
   - Criar uma despesa e voltar ao dashboard para ver impacto.

6. Fluxo B (USER) - Investimentos (2 min)
   - Acessar `/app/transacoes/nova`.
   - Criar uma transação.
   - Mostrar histórico das transações.
   - Mostrar resumo consolidado da carteira.

7. Fluxo C (ADMIN) - Exclusão de categoria (2 min)
   - Entrar com `admin@duckbill.com`.
   - Acessar `/admin/categorias`.
   - Tentar excluir categoria usada em despesas e mostrar mensagem de bloqueio.
   - Excluir categoria sem vínculo para mostrar sucesso.

8. Encerramento (20s)
   - Reforçar boas práticas (controller/service/repository, validações, erros).
