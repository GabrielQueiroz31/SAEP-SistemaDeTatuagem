# Casos de teste — Tema 13

Pré-condições: PostgreSQL populado pelos scripts, API e Angular iniciados. Acesso administrativo: admin / admin123. Para conflitos, cadastrar dois clientes com documentos diferentes e usar uma data de teste sem reservas.

| ID | Cenário e passos | Resultado esperado |
|---|---|---|
| CT-01 | Entrar com admin / admin123. | Abrir Agendamentos e mostrar Administrador. |
| CT-02 | Entrar com senha incorreta. | Mostrar motivo da falha e permanecer no login. |
| CT-03 | Sem login, abrir /home/clientes e chamar GET /api/clientes. | Tela redireciona ao login; API responde 401. |
| CT-04 | Fazer login, sair e repetir chamada com o cookie antigo. | Retornar ao login e API responder 401. |
| CT-05 | Cadastrar cliente com nome, documento, telefone e e-mail válido. | Cliente aparece na tabela e persiste após recarregar. |
| CT-06 | Tocar e sair dos campos obrigatórios vazios. | Exibir mensagens de validação e impedir salvar. |
| CT-07 | Buscar parte do nome e depois parte do documento. | Listar somente clientes correspondentes. |
| CT-08 | Editar telefone de um cliente. | Persistir o novo valor. |
| CT-09 | Excluir cliente sem agendamentos, confirmando. | Remover da listagem. |
| CT-10 | Cadastrar outro cliente com documento já utilizado. | Bloquear operação e mostrar mensagem de conflito. |
| CT-11 | Abrir formulário de agendamento. | Selects apresentam clientes, cinco tatuadores e cinco macas do banco. |
| CT-12 | Salvar reserva com todos os obrigatórios preenchidos. | Histórico exibe data, hora, cliente, tatuador, maca e serviço. |
| CT-13 | Reservar mesmo tatuador/data/hora para outro cliente e outra maca. | API responde 409 e Angular mostra alerta; não cria reserva. |
| CT-14 | Reservar mesma maca/data/hora para outro cliente e outro tatuador. | API responde 409 e Angular mostra alerta; não cria reserva. |
| CT-15 | Reservar mesma data/hora com tatuador e maca diferentes. | Permitir agendamento. |
| CT-16 | Reservar mesmo tatuador e maca em outro horário ou outra data. | Permitir agendamento. |
| CT-17 | Editar somente a observação de uma reserva. | Salvar sem acusar conflito com o próprio registro. |
| CT-18 | Editar uma reserva para ocupar tatuador ou maca já reservados. | Bloquear edição e preservar os dados anteriores. |
| CT-19 | Excluir agendamento e reservar novamente os recursos no mesmo horário. | Permitir nova reserva. |
| CT-20 | Tentar excluir cliente com agendamento. | Bloquear com mensagem; preservar cliente e histórico. |
| CT-21 | Enviar agendamento sem maca diretamente pela API autenticada. | Responder 400 e não salvar. |
| CT-22 | Recarregar página após cadastro e agendamento. | Dados persistidos reaparecem. |

## Execução

Os casos acima são um roteiro manual e não devem ser considerados executados apenas por estarem documentados. Os testes automatizados do backend cobrem regras de conflito e sessão HTTP; o build Angular verifica compilação. Resultados efetivos são registrados após a execução nesta revisão.

## Resultados desta revisão — 10/09/2026

| Verificação | Resultado |
|---|---|
| `mvnw.cmd -o test` | 9 testes passaram, sem falhas ou erros. |
| `npm run build` | Compilação de produção do Angular concluída. |
| SQL no PostgreSQL 18.4 | Tabelas e cinco registros de cada recurso criados em schema isolado; conflitos de tatuador e maca rejeitados; recursos distintos aceitos. Transação revertida ao final, sem alterar dados do sistema. |
| DER em PNG | Arquivo gerado e inspecionado visualmente. |

Cobertura automatizada: inicialização da aplicação; conflito de tatuador; conflito de maca; edição sem conflito com o próprio registro; recursos diferentes no mesmo horário e conflitos na edição; API sem sessão; senha incorreta com mensagem; login e logout com invalidação da sessão; CORS com credenciais para o Angular.

O roteiro completo de interação no navegador permanece como teste manual de aceitação. O build não substitui essa execução. O script `CREATE DATABASE` foi conferido, mas não foi executado contra o banco existente; o teste SQL executou a estrutura e a população em schema isolado dentro do PostgreSQL.

## Correção da base local e validação de ponta a ponta

A base existente usava `clientes.cpf` e exigia `sala_id`/`procedimento_id` nos agendamentos. A migração `banco/02_atualizar_base_antiga.sql` foi aplicada, preservando os dois clientes anteriores. A API agora valida o esquema na inicialização. Falhas de infraestrutura retornam mensagem amigável; detalhes técnicos ficam no log do servidor.

- **26 verificações HTTP passaram contra a API e o PostgreSQL reais**, cobrindo autenticação, CRUD de clientes, busca, validação, duplicidade de documento, recursos, histórico, criação/edição/exclusão de agendamentos, conflitos e exclusão de cliente vinculado. Execute `powershell -ExecutionPolicy Bypass -File docs/testar-api.ps1` com a aplicação iniciada. O teste cria registros identificados e os remove ao final.
- **7 verificações de interface passaram no Chrome isolado:** proteção ao abrir rota interna, erro de login visível, validação visual de e-mail, cadastro aparecendo na tabela, agendamento no histórico, alerta visível de conflito e logout. A falta de `<base href="/">`, que impedia carregar diretamente páginas internas, foi corrigida.
- Os **9 testes automatizados Java** passaram novamente após as correções.

O teste de interface está em `docs/testar-interface.mjs` e usa apenas Node.js e o protocolo de depuração do Chrome. Para reproduzir no Windows, inicie uma instância isolada do Chrome com `--headless=new --remote-debugging-port=9223 --user-data-dir=C:\caminho\temporario\perfil-saep --no-first-run`, sem usar seu perfil pessoal, e execute `node docs/testar-interface.mjs` na raiz. O script fecha essa instância e remove os registros que criou. A evidência visual fica em `backend/target/validacao-interface.png`.

Essas verificações complementam a primeira revisão; o roteiro manual acima continua disponível para a apresentação ao avaliador.
