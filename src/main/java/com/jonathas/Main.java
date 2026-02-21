package com.jonathas;

import com.jonathas.model.Produto;
import com.jonathas.repository.ProdutoRepository;

/*import com.jonathas.database.ConnectionFactory;
import java.sql.Connection;
import java.sql.SQLException;*/

public class Main {
    public static void main(String[] args) {

        ProdutoRepository repository = new ProdutoRepository();

        Produto novoProduto = new Produto("Teste Produto Java", "Produto criado via JDBC");

        repository.salvar(novoProduto);

        System.out.println("ID gerado: " + novoProduto.getId());

        System.out.println("Lista de produtos:");
        repository.listarTodos().forEach(System.out::println);

    }
}
