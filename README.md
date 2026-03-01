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

### 💰 Venda
- Registrar vendas com múltiplos itens
- Suporte a cliente (Emitente)
- Status da venda:
  - PAGO
  - A_RECEBER (fiado)
- Registro automático de pagamento para vendas pagas
- Registro manual de pagamento integral
- Listagem de vendas a receber (fiado)
- Transação única:
  - insere venda
  - insere N itens
  - baixa estoque
  - registra pagamento (quando aplicável)

---

## 📌 Status

✅ V2.1: Controle de fiado (A_RECEBER) + Pagamento Integral + Listagem de vendas pendentes

### Próximos passos:

- V2.1.1: Cadastro e listagem de clientes + Permitir pagamento parcial
- V2.2: Relátórios (período, produto, devedores)
