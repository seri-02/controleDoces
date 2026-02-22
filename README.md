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
- Integração com MySQL via JDBC
- Registro de venda com:
  - Validação de estoque
  - Baixa automática na quantidade do produto
  - Transação (commit/rollback)

---

## 📌 Status

Em desenvolvimento 🚧

Já dá pra cadastrar produto com quantidade e registrar vendas com baixa automática no estoque.

### Próximo passo:
  - Fazer um menu no console.
