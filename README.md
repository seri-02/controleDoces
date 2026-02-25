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
  │   ├── Produto.java
  │   └── Emitente.java
  │   └── Venda.java 
  ├── repository
  │   ├── ProdutoRepository.java
  │   └── EmitenteRepository.java
  │   └── VendaRepository.java 
  └── Main.java
</pre>

---

## 🚀 Funcionalidades implementadas

- CRUD de Produto
- CRUD de Emitente
- CRUD de Venda
- Integração com MySQL via JDBC

---

## 📌 Status

✅ MVP v1 concluído e em uso para controle real das vendas.

### Próximos passos (V2):

- Relatórios por período
- Relatórios por produto
- Integração de Emitente no fluxo de venda
- Introduzir camada service
