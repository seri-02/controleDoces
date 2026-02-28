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

---

## 🛠 Tecnologias utilizadas

- Java 17+
- Maven
- MySQL
- JDBC
- IntelliJ IDEA

---

## 📂 Estrutura do projeto (MVP)

<pre>
 com.jonathas
  ├── app
  │   └── ConsoleApp.Java
  ├── database
  │   └── ConnectionFactory.java
  ├── model
  │   └──  Produto.java
  │   └── ItemVenda.java
  │   └── Venda.java 
  ├── repository
  │   └── ProdutoRepository.java
  │   └── ItemVendaRepository.java
  │   └── VendaRepository.java 
  ├── service
  │   └── VendaService.Java   
  ├──  Main.java
</pre>

---

## 🚀 Funcionalidades implementadas

- CRUD de Produto
- Venda:
  - Registrar vendas com múltiplos itens
  - Transação única:
    - insere venda
    - insere N itens
    - baixa estoque para cada item
  - Listar vendas com itens + total
- Integração com MySQL via JDBC

---

## 📌 Status

✅ V2.0 concluída: service layer + venda com múltiplos itens

### Próximos passos (V2):

- V2.1: Cliente + contas a receber + pagamento
- V2.2: Relátórios (período, produto, devedores)
