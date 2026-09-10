# SAEP - Estúdio de tatuagem e Piercing

Projeto com objetivo de fazer um site de gerenciamento de sessões de desenho, aplicação de tatuagem ou body piercing

## Contextualização

Você foi contratado como Desenvolvedor Fullstack por uma Software House que está lançando um novo produto no mercado: um Sistema de Agendamento Inteligente "White-Label". Esse sistema tem uma arquitetura base padronizada, mas é comercializado para diferentes nichos de mercado (clínicas, oficinas, estúdios, quadras esportivas, etc.).
O grande diferencial do sistema é acabar com as agendas de papel e planilhas confusas, prevenindo a sobreposição de horários (double-booking), que é a principal causa de prejuízos e reclamações nesses estabelecimentos.

## Escopo do Projeto

### 1. Introdução

Este Documento especifíca os requistos de software para o Sistema de Gestão de Agendamentos de Sessões de tatuagem. Será Desenvolvido uma aplicação web (BackEnd em Java SpringBoot e FrontEnd em Angular) que permita a autenticação do usuário, o gerenciamento (CRUD) de clientes e o registro dos agendamentos dessas sessões, com alertas de data ou hora igual com o mesmo tatuador, ou seja, se um profissional já estiver atrelado a uma sessão ele não podera ser registrado a outra no msm horario, mesma coisa para as mascas, não podera usar a msm maca se já estiver sendo usada.

### 2. Requisitos de InfraEstrutura e Ambiente

|Categoria |Especificações | Versão |
|-|-|-|
|Sistema Operacional | Windows | W11 |
|BackEnd | Java SpringBoot | Java 21 Spring 4.0 |
|FrontEnd | TypeScript (Angular) | Angular 21 |
|SGBD | PostgreSQL | 18 |

### 3. Requisitos do Sistema

#### 3.1 Requisitos Funcionais (RF)

- RF-01 : Tela de Login - Interface de Login
- RF-02 : Aviso de Falha de Autenticação - Interface de Login
- RF-03 : Botão de Logout - Interface Principal
- RF-04 : Navegação entre os Clientes cadastrados e os Agendamentos - Interface Principal
- RF-05 : Listar Clientes salvos - Gestão de Clientes
- RF-06 : Listar Agendamentos - Gestão de Agendamentos
- RF-07 : Criação, Edição e Exclusão de Clientes - Gestão de Clientes
- RF-08 : Criação, Edição e Exclusão de Agendamentos - Gestão de Agendamentos
- RF-09 : Inserir data de Agendamento


### 4. Modelo Lógico de Dados

#### 4.1 Diagrama de Entidades Relacionais (DER)


