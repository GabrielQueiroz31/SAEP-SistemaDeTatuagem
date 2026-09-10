# Frontend — SAEP Estúdio de Tatuagem

Interface Angular integrada com a API em http://localhost:8081/api.

## Executar

Primeiro inicie o backend. Em outro terminal, execute:

    cd frontend
    npm install
    npm start

Abra http://localhost:4200.

## Acesso

- Usuário: admin
- Senha: admin123

## Funcionalidades

- Login com mensagem de erro.
- Página inicial com usuário logado, navegação e logout.
- Cadastro, busca, edição e exclusão de clientes.
- Cadastro, listagem, edição e exclusão de agendamentos.
- Selects de tatuadores e macas carregados diretamente pela API.
- Alerta exibido quando a API bloquear conflito de horário.

A autenticação usa cookie de sessão HTTP. A API exige login; o logout invalida a sessão no servidor. A documentação completa, DER, SQL e casos de teste estão no [README principal](../README.md).
