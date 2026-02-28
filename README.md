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
 │   ├── Venda.java
 │   └── ItemVenda.java
 ├── repository
 │   ├── ProdutoRepository.java
 │   ├── VendaRepository.java
 │   └── ItemVendaRepository.java
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
- Registrar venda com múltiplos itens
- Sugestão automática de preço padrão
- Transação única:
  - insere venda
  - insere N itens
  - baixa estoque de cada item
  - commit/rollback
- Listar vendas com itens e total geral

---

## 📌 Status

✅ V2.0.1 concluída: camada de serviço + venda com múltiplos itens + CRUD completo de produto

### Próximos passos:

- V2.1: Cliente + contas a receber + pagamento
- V2.2: Relátórios (período, produto, devedores)
