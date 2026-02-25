package com.jonathas.app;

import java.util.Scanner;
import com.jonathas.model.Produto;
import com.jonathas.repository.ProdutoRepository;
import com.jonathas.database.ConnectionFactory;
import com.jonathas.model.Venda;
import com.jonathas.repository.VendaRepository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ConsoleApp {

    private final Scanner scanner = new Scanner(System.in);
    private final ProdutoRepository produtoRepository = new ProdutoRepository();
    private boolean running = true;

    public void start() {
        while (running) {
            printMenu();
            int opcao = readInt("Escolha uma opção: ");
            handleOption(opcao);
        }

        scanner.close();
        System.out.println("Programa encerrado.");
    }

    private void printMenu() {
        System.out.println();
        System.out.println("============= Controle de Doces =============");
        System.out.println("1 - Cadastrar produto");
        System.out.println("2 - Listar produtos");
        System.out.println("3 - Registrar venda");
        System.out.println("4 - Listar vendas");
        System.out.println("0 - Sair");
        System.out.println("=============================================\n");
        System.out.println();
    }

    private void handleOption(int opcao) {
        switch (opcao) {
            case 1 -> cadastrarProduto();
            case 2 -> listarProdutos();
            case 3 -> registrarVenda();
            case 4 -> listarVendas();
            case 0 -> running = false;
            default -> System.out.println("Opção inválida. Tente novamente.");
        }
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número do menu.");
            }
        }
    }

    public void cadastrarProduto() {
        System.out.println();
        System.out.println("============= Cadastrar produto =============");

        String nome = readString("Nome do produto: ");
        String descricao = readString("Descrição (opcional): ");
        int quantidade = readInt("Quantidade inicial: ");

        if (nome.isBlank()) {
            System.out.println("Digite um nome para que possa salvar o produto.");
            return;
        }
        if (quantidade < 0) {
            System.out.println("Quantidade precisa ser maior que 0 para salvar o produto.");
            return;
        }

        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setDescricao(descricao.isBlank() ? null : descricao);
        produto.setQuantidade(quantidade);
        produto.setAtivo(true);

        produtoRepository.salvar(produto);

        System.out.println("Produto cadastrado com sucesso. ID: " + produto.getId());
    }

    private String readString(String prompt) {
        System.out.println(prompt);
        return scanner.nextLine().trim();
    }

    public void listarProdutos() {
        System.out.println();
        System.out.println("============== Listar produtos ==============");

        var produtos = produtoRepository.listarTodos();

        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado até o momento.");
            return;
        }

        System.out.printf("%-5s %-25s %-10s %-6s%n", "ID", "Nome", "Qtd", "Ativo");
        System.out.println("------------------------------------------------------");

        for (var p : produtos) {
            System.out.printf("%-5s %-25s %-10s %-6s%n",
                    p.getId(),
                    trunc(p.getNome(), 25),
                    p.getQuantidade(),
                    p.getAtivo() ? "Sim" : "Não"
            );
        }
    }

    private String trunc(String text, int max) {
        if (text == null) return "";
        if (text.length() <= max) return text;
        return text.substring(0, max - 3) + "...";
    }

    private BigDecimal readBigDecimal(String prompt) {
        while (true) {
            System.out.println(prompt);
            String input = scanner.nextLine().trim().replace(",", ",");
            try {
                BigDecimal value = new BigDecimal(input);
                if (value.compareTo(BigDecimal.ZERO) < 0) {
                    System.out.println("Valor não pode ser negativo.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite novamente.");
            }
        }
    }

    private void registrarVenda() {
        System.out.println();
        System.out.println("============== Registrar venda ==============");

        long produtoId = readInt("ID do produto: ");
        int quantidade = readInt("Quantidade vendida: ");
        BigDecimal valorUnitario = readBigDecimal("Valor unitário: ");

        if (quantidade < 0) {
            System.out.println("Quantidade precisa ser maior que 0.");
            return;
        }

        // Verifica se produto existe
        Produto produto  = produtoRepository.buscarPorId(produtoId);
        if (produto == null) {
            System.out.println("Produto não encontrado.");
            return;
        }

        Venda venda = new Venda(produto, quantidade, valorUnitario, LocalDateTime.now());

        try (Connection connection = ConnectionFactory.getConnection()) {
            VendaRepository vendaRepository = new VendaRepository(connection);
            vendaRepository.registrarVenda(venda);
            System.out.println("Venda registrada com sucesso.");
        } catch (SQLException e) {
            // Estoque insuficiente ou Produto não encontrado
            System.out.println("Erro ao registrar venda: " + e.getMessage());
        }
    }

    private void listarVendas() {
        System.out.println();
        System.out.println("============== Listar vendas ==============");

        try (Connection connection = ConnectionFactory.getConnection()) {

            VendaRepository vendaRepository = new VendaRepository(connection);
            var vendas = vendaRepository.listarTodas();

            if (vendas.isEmpty()) {
                System.out.println("Nenhum venda registrada.");
                return;
            }

            System.out.printf("%-5s %-10s %-8s %-12s %-12s%n",
                    "ID", "ProdutoID", "Qtd", "V.Unit", "Total");
            System.out.println("------------------------------------------------------------");

            BigDecimal totalGeral = BigDecimal.ZERO;

            for (var v : vendas) {

                BigDecimal totalVenda =
                        v.getValorUnitario().multiply(BigDecimal.valueOf(v.getQuantidade()));

                totalGeral = totalGeral.add(totalVenda);

                System.out.printf("%-5d %-10d %-8d %-12s %-12s%n",
                        v.getId(),
                        v.getProduto().getId(),
                        v.getQuantidade(),
                        v.getValorUnitario(),
                        totalVenda
                );
            }

            System.out.println("------------------------------------------------------------");
            System.out.println("Total geral das vendas: " + totalGeral);
        } catch (SQLException e) {
            System.out.println("Erro ao listar vendas: " + e.getMessage());
        }
    }


}
