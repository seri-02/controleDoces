# Controle de Doces 📦

Sistema real de gerenciamento de vendas e controle de estoque para doces, desenvolvido como projeto pessoal para aplicar e aprofundar conhecimentos em Java, JDBC e MySQL.

O sistema é utilizado para registrar vendas reais e controlar estoque de produção.

Projeto console (CLI), desenvolvido em Java puro.

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

## 📂 Estrutura do projeto (V2.0.1)

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

--

### 👤 Cliente (Emitente)
- Cadastro de cliente
- Listagem de clientes ativos
- Busca por nome
- Seleção de cliente por lista numerada

--

### 💰 Venda
- Registrar vendas com múltiplos itens
- Seleção de produto por ID
- Suporte a cliente (Emitente)
- Status da venda:
  - PAGO
  - A_RECEBER (fiado)
- Registro automático de pagamento para vendas pagas
- Registro manual de pagamento integral
- Listagem de vendas a receber (controle de fiado)
- Cálculo automático de total da venda

--

### 🔄 Regras de Negócio
- Venda deve conter ao menos 1 item
- Validação de estoque antes da confirmação
- Transação única por venda:
  - insere venda
  - insere N itens
  - baixa estoque
  - registra pagamento (quando aplicável)
  - commit/rollback automático
- Venda fiado exige cliente válido

---

## 📌 Status

✅ V2.1.1: Cadastro e listagem de clientes no fluxo de venda

### Próximos passos:

- V2.1.2: Cliente obrigatório em toda venda + seleção por nome/lista (sem ID) + listagens mais user friendly
- V2.2: Relátórios (período, produto, devedores) + Permitir pagamento parcial
