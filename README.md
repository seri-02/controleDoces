# Controle de Doces

Sistema de controle de produtos desenvolvido em Java utilizando Maven, JDBC e MySQL.

O projeto tem como objetivo praticar:

- Arquitetura em camadas
- Persistência com JDBC
- Organização de projeto Maven
- Versionamento com Git

---

## 🛠 Tecnologias utilizadas

- Java 17+
- Maven
- MySQL
- JDBC

---

## 📂 Estrutura do projeto

<pre>
src/main/java/com/jonathas
├── database
│   └── ConnectionFactory.java
├── model
│   ├── Produto.java
│   └── Emitente.java
├── repository
│   ├── ProdutoRepository.java
│   └── EmitenteRepository.java
└── Main.java
</pre>

---

## 🚀 Funcionalidades implementadas

- CRUD de Produto
- CRUD de Emitente
- Integração com MySQL via JDBC

---

## 📌 Status

Em desenvolvimento 🚧

Já possui cadastro e listagem de Produtos e Emitentes com persistência no banco.

Agora vou implementar o relacionamento entre as entidades para evoluir a modelagem.
