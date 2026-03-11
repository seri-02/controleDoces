# Controle de Doces 📦

Sistema real de gerenciamento de vendas e controle de estoque para doces, desenvolvido como projeto pessoal para aplicar e aprofundar conhecimentos em Java, JDBC e MySQL.

O sistema é utilizado para registrar vendas reais e controlar estoque de produção.

Projeto console (CLI), desenvolvido em Java puro com JDBC.

## 🎯 Objetivo

- Arquitetura em camadas
- Separação de responsabilidades
- Persistência com JDBC
- Modelagem relacional
- Regras de negócio
- Boas práticas de versionamento

⚠️ O sistema está em uso real para controle de vendas semanais.

---

## 🛠 Tecnologias utilizadas

- Java 17+
- Maven
- MySQL
- JDBC
- IntelliJ IDEA

---

## 📂 Estrutura do projeto (V2.2.1)

<pre>
 com.jonathas
 ├── app
 │   └── ConsoleApp.java
 ├── database
 │   └── ConnectionFactory.java
 ├── model
 │   ├── Produto.java
 │   ├── Emitente.java 
 │   ├── Venda.java
 │   ├── ItemVenda.java
 │   └── Pagamento.java
 ├── repository
 │   ├── ProdutoRepository.java
 │   ├── EmitenteRepository.java 
 │   ├── VendaRepository.java
 │   ├── ItemVendaRepository.java
 │   └── PagamentoRepository.java
 ├── service
 │    └── VendaService.java
 └── Main.java
</pre>

---

## 🚀 Funcionalidades implementadas

### 📦 Produto
- Cadastrar produto
- Editar produto
- Inativar produto
- Ajustar estoque manualmente
- Definir preço padrão de venda
- Definir custo unitário
- Controle de estoque automático via venda

---

### 👤 Clientes
- Cadastro de cliente
- Listagem de clientes ativos
- Busca por nome
- Seleção de cliente por lista numerada

---

### 💰 Venda
- Registrar vendas com múltiplos itens
- Seleção de produto por lista numerada
- Cliente obrigatório em toda venda
- Cálculo automático do valor total da venda
- Pagamento inicial no momento da venda
- Status da venda:
  - PAGO
  - PARCIAL
  - A_RECEBER
- Registro automático de pagamento inicial quando informado
- Registro manual de pagamentos complementares
- Atualização automática do status da venda com base no total pago
- Listagens mais legíveis:
  - Listar vendas com nome do cliente e nome do produto (JOIN)
  - Listar vendas a receber agrupadas por cliente
  - Exibir total da venda, total já pago e saldo restante

---

### 🔄 Regras de Negócio
- Venda deve conter ao menos 1 item
- Cliente é obrigatório em toda venda
- Validação de estoque antes da confirmação
- Transação única por venda:
  - insere venda
  - insere N itens
  - baixa estoque
  - registra pagamento inicial (quando houver)
  - commit/rollback automático
- O status da venda é calculado automaticamente com base no valor pago:
  - A_RECEBER: nenhum valor pago
  - PARCIAL: valor pago menor que o total
  - PAGO: valor pago igual ao total
- Não é permitido registrar pagamento maior que o saldo devedor

---

### 🖥 UX do Console
- Menu principal organizado por módulos:
  - Produtos
  - Vendas
  - Financeiro
  - Clientes
- Submenus por contexto para reduzir poluição visual
- Seleção por lista numerada em vez de digitação de IDs em fluxos principais
- Melhor feedback de cancelamento e mensagens mais claras ao usuário

---

## 📌 Status
✅ V2.2.1: pagamento parcial, saldo devedor e melhoria de UX no CLI

### Próximos passos:
- V2.3: Relatórios
  - vendas por período
  - vendas por produto
  - ranking de produtos
  - ranking de clientes
  - devedores
- Melhorias adicionais no módulo financeiro
- Evolução futura para ficha técnica e precificação
