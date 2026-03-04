package com.jonathas.app;

import java.util.Scanner;

import com.jonathas.model.Venda;
import com.jonathas.repository.EmitenteRepository;
import com.jonathas.repository.ProdutoRepository;
import com.jonathas.model.Produto;
import com.jonathas.model.ItemVenda;
import com.jonathas.model.Emitente;
import com.jonathas.service.VendaService;
import java.math.BigDecimal;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConsoleApp {

    private final Scanner scanner = new Scanner(System.in);
    private final ProdutoRepository produtoRepository = new ProdutoRepository();
    private final EmitenteRepository emitterRepository = new EmitenteRepository();
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
        System.out.println("8 - Registrar pagamento");
        System.out.println("9 - Listar pagamento");
        System.out.println("10 - Cadastrar cliente");
        System.out.println("11 - Listar clientes");
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
            case 8 -> registrarPagamento();
            case 9 -> listarVendasAReceber();
            case 10 -> cadastrarCliente();
            case 11 -> listarClientes();
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

            Produto produto = selecionarProdutoPorNumero();
            if (produto == null) {
                System.out.println("Seleção de produto cancelada.");
                return;
            }

            int quantidade = readInt("Quantidade vendida: ");

            if (quantidade <= 0) {
                System.out.println("Quantidade precisa ser maior que 0.");
                continue;
            }

            BigDecimal valorUnitario;

            String usarPadrao = readString(
                    "Preço padrão é " + produto.getPrecoPadrao() + ". Usar? (s/n): ").toLowerCase();

            if (usarPadrao.equals("s")) {
                valorUnitario = produto.getPrecoPadrao();
            } else {
                valorUnitario = readBigDecimal("Valor unitário: ");
            }

            itens.add(new ItemVenda(produto.getId(), quantidade, valorUnitario));

            String mais = readString("Adicionar mais itens? (s/n): ").toLowerCase();
            if (!mais.equals("s")) {
                break;
            }
        }

        String pago = readString("Venda foi paga? (s/n): ").toLowerCase();
        String status = pago.equals("s") ? "PAGO" : "A_RECEBER";

        Long clienteId = selecionarClientePorNumero();
            if (clienteId == null) {
                System.out.println("Venda fiado exige um cliente. Cadastre ou selecione um cliente para prosseguir.");
                return;
        }

        try {
            long vendaId = vendaService.registrarVenda(itens, clienteId, status);
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

            java.math.BigDecimal totalGeral = java.math.BigDecimal.ZERO;

            for (var v : vendas) {
                System.out.println("--------------------------------------------");
                System.out.println("Venda #" + v.getId()
                        + " | Data: " + v.getDataVenda()
                        + " | Cliente: " + (v.getClienteNome() == null ? "(sem nome)" : v.getClienteNome())
                        + " | Status: " + v.getStatus());

                System.out.printf("%-25s %-6s %-12s %-12s%n", "Produto", "Qtd", "V.Unit", "Subtotal");

                for (var item : v.getItens()) {
                    System.out.printf("%-25s %-6d %-12s %-12s%n",
                            trunc(item.getProdutoNome(), 25),
                            item.getQuantidade(),
                            item.getValorUnitario(),
                            item.getSubtotal()
                    );
                }

                System.out.println("Total da venda: " + v.getTotal());
                totalGeral = totalGeral.add(v.getTotal());
            }

            System.out.println("------------------------------------------------------------");
            System.out.println("TOTAL GERAL: " + totalGeral);
        } catch (SQLException e) {
            System.out.println("Erro ao listar vendas." + e.getMessage());
        }

    }

    private void registrarPagamento() {
        System.out.println();
        System.out.println("============== Registrar pagamento ==============");

        long vendaId = readInt("ID da venda para quitar: ");

        try {
            vendaService.registrarPagamentoIntegral(vendaId);
            System.out.println("Pagamento registrado e venda marcada como PAGO.");
        } catch (SQLException e) {
            System.out.println("Erro ao registrar pagamento." + e.getMessage());
        }
    }

    private void listarVendasAReceber() {
        System.out.println();
        System.out.println("============== Vendas a Receber ==============");

        try {
            var vendas =  vendaService.listarVendasAReceberComItens();

            if (vendas.isEmpty()) {
                System.out.println("Nenhum venda a receber no momento.");
                return;
            }

            // Agrupar por cliente
            java.util.Map<String, java.util.List<Venda>> porCliente = new java.util.LinkedHashMap<>();

            for (var v : vendas) {
                porCliente
                        .computeIfAbsent(v.getClienteNome(), k -> new java.util.ArrayList<>())
                        .add(v);
            }

            BigDecimal totalGeral = BigDecimal.ZERO;

            for (var entry : porCliente.entrySet()) {

                String clienteNome = entry.getKey();
                var vendaDoCliente = entry.getValue();

                System.out.println();
                System.out.println("============================================================");
                System.out.println("CLIENTE: " + trunc(clienteNome, 40));
                System.out.println("============================================================");

                BigDecimal totalCliente = BigDecimal.ZERO;

                for (var v : vendaDoCliente) {

                    System.out.println("------------------------------------------------------------");
                    System.out.println("Venda #" + v.getId()
                            + " | Data: " + v.getDataVenda());

                    System.out.printf("%-25s %-6s %-12s %-12s%n",
                            "Produto", "Qtd", "V.Unit", "Subtotal");

                    for (var item : v.getItens()) {
                        System.out.printf("%-25s %-6d %-12s %-12s%n",
                                trunc(item.getProdutoNome(), 25),
                                item.getQuantidade(),
                                item.getValorUnitario(),
                                item.getSubtotal()
                        );
                    }

                    System.out.println("Total da venda: " + v.getTotal());
                    totalCliente = totalCliente.add(v.getTotal());
                }

                System.out.println("------------------------------------------------------------");
                System.out.println("TOTAL DEVIDO POR " + clienteNome.toUpperCase() + ": " + totalCliente);

                totalGeral = totalGeral.add(totalCliente);
            }

            System.out.println();
            System.out.println("============================================================");
            System.out.println("TOTAL GERAL A RECEBER: " + totalGeral);
            System.out.println("============================================================");
        }catch (SQLException e) {
            System.out.println("Erro ao listar vendas a receber: " + e.getMessage());
        }
    }

    private void cadastrarCliente() {
        System.out.println();
        System.out.println("============== Cadastrar cliente ==============");

        String nome = readString("Nome do cliente: ");
        if (nome.isBlank()) {
            System.out.println("Nome é obrigatório");
            return;
        }

        String documento = readString("Documento (opcional): ");
        String tipo = "CLIENTE";

        Emitente cliente = new Emitente();
        cliente.setNome(nome);
        cliente.setDocumento(documento.isBlank() ? null : documento);
        cliente.setTipo(tipo);
        cliente.setAtivo(true);

        emitterRepository.salvar(cliente);
        System.out.println("Cliente cadastrado. ID: " + cliente.getId());
    }

    private void listarClientes() {
        System.out.println();
        System.out.println("============== Listar clientes ==============");

        var clientes = emitterRepository.listarClientesAtivos();

        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente encontrado.");
            return;
        }

        System.out.printf("%-5s %-30s %-15s%n", "ID", "Nome", "Documento");
        System.out.println("-----------------------------------------------------");

        for (var c : clientes) {
            System.out.printf("%-5d %-30s %-15s%n",
                    c.getId(),
                    trunc(c.getNome(), 30),
                    c.getDocumento() == null ? "" : trunc(c.getDocumento(), 15));
        }
    }

    private Long selecionarClientePorNumero() {
        System.out.println();
        System.out.println("============== Selecionar cliente ==============");

        String termo = readString("Buscar por nome (Enter para listar todos): ");

        // TODO: Verificar depois
        var clientes = termo.isBlank()
                ? emitterRepository.listarClientesAtivos()
                : emitterRepository.buscarClientesPorNome(termo);

        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente encontrado.");
            return null;
        }

        for (int i = 0; i < clientes.size(); i++) {
            var c = clientes.get(i);
            System.out.printf("%d) %s %s%n",
                    (i + 1),
                    c.getNome(),
                    c.getDocumento() == null ? "" : ("- " + c.getDocumento()));
        }

        while (true) {
            int escolha = readInt("Escolha um número (0 para cancelar): ");

            if (escolha == 0) {
                return null;
            }

            if (escolha >= 1 && escolha <= clientes.size()) {
                return clientes.get(escolha - 1).getId();
            }

            System.out.println("Opção inválida.Digite um número entre 1 e " + clientes.size() + " (ou 0 para cancelar).");
        }

    }

    private Produto selecionarProdutoPorNumero() {
        System.out.println();
        System.out.println("============== Selecionar produto ==============");

        String termo = readString("Buscar produto por nome: ");
        if (termo.isBlank()) {
            System.out.println("Digite ao menos  uma parte do produto.");
            return null;
        }

        var produtos = produtoRepository.buscarAtivosPorNome(termo);

        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto encontrado para: " + termo);
            return null;
        }

        for (int i = 0; i < produtos.size(); i++) {
            var p = produtos.get(i);
            System.out.printf(
                    "%d) %s | estoque: %d | preço padrão: %s%n",
                    (i + 1),
                    p.getNome(),
                    p.getQuantidade(),
                    p.getPrecoPadrao()
            );
        }

        while (true) {
            int escolha = readInt("Escolha um produto (0 para cancelar): ");

            if (escolha == 0) return null;

            if (escolha >= 1 && escolha <= produtos.size()) {
                return produtos.get(escolha - 1);
            }

            System.out.println("Opção inválida. Digite entre 1 e " + produtos.size() + " (ou 0 para cancelar).");
        }
    }
}
