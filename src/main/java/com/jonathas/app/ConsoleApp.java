package com.jonathas.app;

import java.util.Scanner;
import com.jonathas.model.Produto;
import com.jonathas.repository.ProdutoRepository;
import com.jonathas.model.ItemVenda;
import com.jonathas.service.VendaService;
import java.math.BigDecimal;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConsoleApp {

    private final Scanner scanner = new Scanner(System.in);
    private final ProdutoRepository produtoRepository = new ProdutoRepository();
    private final VendaService vendaService = new VendaService();
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
        System.out.println("5 - Editar produto");
        System.out.println("6 - Inativar produto");
        System.out.println("7 - Ajustar estoque");
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
            case 5 -> editarProduto();
            case 6 -> inativarProduto();
            case 7 -> ajustarEstoque();
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
        BigDecimal precoPadrao = readBigDecimal("Preço padrão de venda: ");
        BigDecimal custoUnitario = readBigDecimal("Custo unitário (estimado): ");

        if (nome.isBlank()) {
            System.out.println("Digite um nome para que possa salvar o produto.");
            return;
        }
        if (quantidade < 0) {
            System.out.println("Quantidade precisa ser maior que 0 para salvar o produto.");
            return;
        }
        if (precoPadrao.compareTo(BigDecimal.ZERO) < 0 || custoUnitario.compareTo(BigDecimal.ZERO) < 0) {
            System.out.println("Preço e custo não podem ser negativos.");
            return;
        }

        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setDescricao(descricao.isBlank() ? null : descricao);
        produto.setQuantidade(quantidade);
        produto.setAtivo(true);
        produto.setPrecoPadrao(precoPadrao);
        produto.setCustoUnitario(custoUnitario);

        produtoRepository.salvar(produto);

        System.out.println("Produto cadastrado com sucesso. ID: " + produto.getId());
    }

    private String readString(String prompt) {
        System.out.print(prompt);
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
            String input = scanner.nextLine().trim().replace(",", ".");
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

    private void editarProduto() {
        System.out.println();
        System.out.println("============== Editar produto ==============");

        long id = readInt("ID do produto: ");
        Produto produto = produtoRepository.buscarPorId(id);

        if (produto == null) {
            System.out.println("Produto não encontrado");
            return;
        }

        System.out.println("Deixe em branco para manter o valor atual.");

        String nome = readString("Nome (" + produto.getNome() + "): ");
        String descricao = readString("Descrição (" + (produto.getDescricao() == null ? "" : produto.getDescricao()) + "): ");

        String ativoStr = readString("Ativo (s/n) (" + (produto.getAtivo() ? "s" : "n") + "): ").toLowerCase();
        String precoStr = readString("Preço padrão (" + produto.getPrecoPadrao() + "): ").replace(",", ".");
        String custoStr = readString("Custo unitário (" + produto.getCustoUnitario() + "): ").replace(",", ".");

        if (!nome.isBlank()) produto.setNome(nome);
        if (!descricao.isBlank()) produto.setDescricao(descricao);

        if (ativoStr.equals("s")) produto.setAtivo(true);
        else if (ativoStr.equals("n")) produto.setAtivo(false);

        if (!precoStr.isBlank()) produto.setPrecoPadrao(new BigDecimal(precoStr));
        if (!custoStr.isBlank()) produto.setCustoUnitario(new BigDecimal(custoStr));

        produtoRepository.atualizar(produto);
    }

    private void inativarProduto() {
        System.out.println();
        System.out.println("============== Inativar produto ==============");

        long id = readInt("ID do produto: ");
        Produto produto = produtoRepository.buscarPorId(id);

        if (produto == null) {
            System.out.println("Produto não encontrado");
            return;
        }

        String confirma = readString("Tem certeza que deseja inativar '" + produto.getNome() + "'? (s/n): ").toLowerCase();
        if (!confirma.equals("s")) {
            System.out.println("Operação cancelada.");
            return;
        }

        produtoRepository.inativar(id);
    }

    private void ajustarEstoque() {
        System.out.println();
        System.out.println("============== Ajustar estoque ==============");

        long id = readInt("ID do produto: ");
        Produto produto = produtoRepository.buscarPorId(id);

        if (produto == null) {
            System.out.println("Produto não encontrado");
            return;
        }

        System.out.println("Produto: " + produto.getNome() + " | Estoque atual: " + produto.getQuantidade());

        String tipo = readString("Ajuste (+/-): ").trim();
        int qtd = readInt("Quantidade: ");

        if (qtd <= 0) {
            System.out.println("Quantidade deve ser maior que 0");
            return;
        }

        int delta = tipo.equals("-") ? -qtd : qtd;

        produtoRepository.ajustarEstoque(id, delta);
    }

    private void registrarVenda() {
        System.out.println();
        System.out.println("============== Registrar venda ==============");

        List<ItemVenda> itens = new ArrayList<>();

        while (true) {
            long produtoId = readInt("Id do produto: ");
            int quantidade = readInt("Quantidade vendida: ");
            BigDecimal valorUnitario = readBigDecimal("Valor unitário: ");

            if (quantidade <= 0) {
                System.out.println("Quantidade precisa ser maior que 0.");
                continue;
            }

            // Valida se produto existe via console
            Produto produto = produtoRepository.buscarPorId(produtoId);
            if (produto == null) {
                System.out.println("Produto no encontrado.");
                continue;
            }

            String usarPadrao = readString(
                    "Preço padrão é " + produto.getPrecoPadrao() + ". Usar? (s/n): ").toLowerCase();

            if (usarPadrao.equals("s")) {
                valorUnitario = produto.getPrecoPadrao();
            } else {
                valorUnitario = readBigDecimal("Valor unitário: ");
            }

            itens.add(new ItemVenda(produtoId, quantidade, valorUnitario));

            String mais = readString("Adicionar mais itens? (s/n): ").toLowerCase();
            if (!mais.equals("s")) {
                break;
            }
        }

        try {
            long vendaId = vendaService.registrarVenda(itens);
            System.out.println("Venda registrada com sucesso. ID: " + vendaId);
        } catch (Exception e) {
            System.out.println("Erro ao registrar venda." + e.getMessage());
        }
    }

    private void listarVendas() {
        System.out.println();
        System.out.println("============== Listar vendas ==============");

        try {
            var vendas = vendaService.listarVendasComItens();

            if (vendas.isEmpty()) {
                System.out.println("Nenhum venda registrada.");
                return;
            }

            BigDecimal totalGeral = BigDecimal.ZERO;

            for (var v : vendas) {
                System.out.println("--------------------------------------------");
                System.out.println("Venda ID: " + v.getId() + " | Data: " + v.getDataVenda());
                System.out.printf("%-10s %-8s %-12s %-12s%n",
                        "ProdutoID", "Qtd", "V.Unit", "Subtotal");

                for (var item : v.getItens()) {
                    System.out.printf("%-10d %-8d %-12s %-12s%n",
                            item.getProdutoId(),
                            item.getQuantidade(),
                            item.getValorUnitario(),
                            item.getSubtotal()
                    );
                }

                System.out.println("Total da venda: " + v.getTotal());
                totalGeral = totalGeral.add(v.getTotal());
            }

            System.out.println("============================================");
            System.out.println("Total geral das vendas: " + totalGeral);
        } catch (SQLException e) {
            System.out.println("Erro ao listar vendas." + e.getMessage());
        }

    }
}
