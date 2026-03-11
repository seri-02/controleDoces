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
    private final EmitenteRepository emitenteRepository = new EmitenteRepository();
    private final VendaService vendaService = new VendaService();
    private boolean running = true;

    public void start() {
        while (running) {
            printMenuPrincipal();
            int opcao = readInt("Escolha uma opção: ");
            handleMenuPrincipal(opcao);
        }

        scanner.close();
        System.out.println("Programa encerrado.");
    }

    private void printMenuPrincipal() {
        printTitulo("Controle de Doces");
        System.out.println("1 - Produtos");
        System.out.println("2 - Vendas");
        System.out.println("3 - Financeiro");
        System.out.println("4 - Clientes");
        System.out.println("0 - Sair");
        printLinha();
        System.out.println();
    }

    private void printMenuProdutos() {
        printTitulo("Produtos");
        System.out.println("1 - Cadastrar produto");
        System.out.println("2 - Listar produtos");
        System.out.println("3 - Editar produto");
        System.out.println("4 - Inativar produto");
        System.out.println("5 - Ajustar estoque");
        System.out.println("0 - Voltar");
        printLinha();
        System.out.println();
    }

    private void printMenuVendas() {
        printTitulo("Vendas");
        System.out.println("1 - Registrar venda");
        System.out.println("2 - Listar vendas");
        System.out.println("0 - Voltar");
        printLinha();
        System.out.println();
    }

    private void printMenuFinanceiro() {
        printTitulo("Financeiro");
        System.out.println("1 - Registrar pagamento");
        System.out.println("2 - Listar vendas a receber");
        System.out.println("0 - Voltar");
        printLinha();
        System.out.println();
    }

    private void printMenuClientes() {
        printTitulo("Clientes");
        System.out.println("1 - Cadastrar cliente");
        System.out.println("2 - Listar clientes");
        System.out.println("0 - Voltar");
        printLinha();
        System.out.println();
    }

    private void handleMenuPrincipal(int opcao) {
        switch (opcao) {
            case 1 -> menuProdutos();
            case 2 -> menuVendas();
            case 3 -> menuFinanceiro();
            case 4 -> menuClientes();
            case 0 -> running = false;
            default -> System.out.println("Opção inválida. Tente novamente.");
        }
    }

    private void menuProdutos() {
        boolean voltar = false;

        while (!voltar) {
            printMenuProdutos();
            int opcao = readInt("Escolha uma opção: ");

            switch (opcao) {
                case 1 -> {
                    cadastrarProduto();
                    pressionarEnterParaContinuar();
                }
                case 2 -> {
                    listarProdutos();
                    pressionarEnterParaContinuar();
                }
                case 3 -> {
                    editarProduto();
                    pressionarEnterParaContinuar();
                }
                case 4 -> {
                    inativarProduto();
                    pressionarEnterParaContinuar();
                }
                case 5 -> {
                    ajustarEstoque();
                    pressionarEnterParaContinuar();
                }
                case 0 -> voltar = true;
                default -> {
                    System.out.println("Opção inválida. Tente novamente.");
                    pressionarEnterParaContinuar();
                }
            }
        }
    }

    private void menuVendas() {
        boolean voltar = false;

        while (!voltar) {
            printMenuVendas();
            int opcao = readInt("Escolha uma opção: ");

            switch (opcao) {
                case 1 -> {
                    registrarVenda();
                    pressionarEnterParaContinuar();
                }
                case 2 -> {
                    listarVendas();
                    pressionarEnterParaContinuar();
                }
                case 0 -> voltar = true;
                default -> {
                    System.out.println("Opção inválida. Tente novamente.");
                    pressionarEnterParaContinuar();
                }
            }
        }
    }

    private void menuFinanceiro() {
        boolean voltar = false;

        while (!voltar) {
            printMenuFinanceiro();
            int opcao = readInt("Escolha uma opção: ");

            switch (opcao) {
                case 1 -> {
                    registrarPagamento();
                    pressionarEnterParaContinuar();
                }
                case 2 -> {
                    listarVendasAReceber();
                    pressionarEnterParaContinuar();
                }
                case 0 -> voltar = true;
                default -> {
                    System.out.println("Opção inválida. Tente novamente.");
                    pressionarEnterParaContinuar();
                }
            }
        }
    }

    private void menuClientes() {
        boolean voltar = false;

        while (!voltar) {
            printMenuClientes();
            int opcao = readInt("Escolha uma opção: ");

            switch (opcao) {
                case 1 -> {
                    cadastrarCliente();
                    pressionarEnterParaContinuar();
                }
                case 2 -> {
                    listarClientes();
                    pressionarEnterParaContinuar();
                }
                case 0 -> voltar = true;
                default -> {
                    System.out.println("Opção inválida. Tente novamente.");
                    pressionarEnterParaContinuar();
                }
            }
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

    private String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private void printTitulo(String titulo) {
        System.out.println();
        System.out.println("============== " + titulo + " ==============");
    }

    private void printLinha() {
        System.out.println("=============================================");
    }

    private void pressionarEnterParaContinuar() {
        System.out.println();
        System.out.println("Pressione Enter para continuar..");
        scanner.nextLine();
    }

    public void cadastrarProduto() {
        printTitulo("Cadastrar produto");

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

    public void listarProdutos() {
        printTitulo("Listar produtos");

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
        printTitulo("Editar produto");

        Produto produto = selecionarProdutoParaGestao();

        if (produto == null) {
            System.out.println("Operação cancelada.");
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

        if (ativoStr.equals("s")) {
            produto.setAtivo(true);
        } else if (ativoStr.equals("n")) {
            produto.setAtivo(false);
        }

        try {
            if (!precoStr.isBlank()) {
                produto.setPrecoPadrao(new BigDecimal(precoStr));
            }

            if (!custoStr.isBlank()) {
                produto.setCustoUnitario(new BigDecimal(custoStr));
            }
        } catch (NumberFormatException e) {
            System.out.println("Preço ou custo inválido. Edição cancelada.");
            return;
        }

        produtoRepository.atualizar(produto);
    }

    private void inativarProduto() {
        printTitulo("Inativar produto");

        Produto produto = selecionarProdutoParaGestao();

        if (produto == null) {
            System.out.println("Operação cancelada.");
            return;
        }

        if (!produto.getAtivo()) {
            System.out.println("Esse produto já está inativo.");
            return;
        }

        String confirma = readString("Tem certeza que deseja inativar '" + produto.getNome() + "'? (s/n): ").toLowerCase();

        if (!confirma.equals("s")) {
            System.out.println("Operação cancelada.");
            return;
        }

        produtoRepository.inativar(produto.getId());
    }

    private void ajustarEstoque() {
        printTitulo("Ajustar estoque");

        Produto produto = selecionarProdutoParaGestao();

        if (produto == null) {
            System.out.println("Operação cancelada.");
            return;
        }

        System.out.println("Produto: " + produto.getNome() + " | Estoque atual: " + produto.getQuantidade());

        String tipo = readString("Ajuste (+/-): ").trim();
        if (!tipo.equals("+") && !tipo.equals("-")) {
            System.out.println("Tipo de ajuste inválido. Use + ou -.");
            return;
        }

        int qtd = readInt("Quantidade: ");

        if (qtd <= 0) {
            System.out.println("Quantidade deve ser maior que 0");
            return;
        }

        int delta = tipo.equals("-") ? -qtd : qtd;

        if (produto.getQuantidade() + delta < 0) {
            System.out.println("Operação inválida. Estoque não pode ficar negativo.");
            return;
        }

        produtoRepository.ajustarEstoque(produto.getId(), delta);
    }

    private BigDecimal calcularTotalItens(List<ItemVenda> itens) {
        BigDecimal total = BigDecimal.ZERO;

        for (ItemVenda item : itens) {
            BigDecimal subtotal = item.getValorUnitario()
                    .multiply(new BigDecimal(item.getQuantidade()));
            total = total.add(subtotal);
        }

        return total;
    }

    private void registrarVenda() {
        printTitulo("Registrar venda");

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

        Long clienteId = selecionarClientePorNumero();
            if (clienteId == null) {
                System.out.println("Registro venda cancelado.");
                return;
        }

        BigDecimal totalVenda = calcularTotalItens(itens);
        System.out.println("Total da venda: " + totalVenda);

        BigDecimal valorPagoInicial = readBigDecimal("Valor pago agora (0 para deixar em aberto): ");

        try {
            long vendaId = vendaService.registrarVenda(itens, clienteId, valorPagoInicial);
            System.out.println("Venda registrada com sucesso. ID: " + vendaId);
        } catch (Exception e) {
            System.out.println("Erro ao registrar venda." + e.getMessage());
        }
    }

    private void listarVendas() {
        printTitulo("Listar vendas");

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

                System.out.println("Total da venda: " + v.getValorTotal());
                totalGeral = totalGeral.add(v.getValorTotal());
            }

            System.out.println("------------------------------------------------------------");
            System.out.println("TOTAL GERAL: " + totalGeral);
        } catch (SQLException e) {
            System.out.println("Erro ao listar vendas." + e.getMessage());
        }

    }

    private void registrarPagamento() {
        printTitulo("Registrar pagamento");

        Venda venda = selecionarVendaAReceberPorNumero();

        if (venda == null) {
            System.out.println("Operação cancelada.");
            return;
        }

        try {
            BigDecimal totalPago = vendaService.obterTotalPago(venda.getId());
            BigDecimal saldoDevedor = vendaService.obterSaldoDevedor(venda.getId());

            System.out.println("Resumo da venda:");
            System.out.println("Total da venda: " + venda.getValorTotal());
            System.out.println("Total já pago: " + totalPago);
            System.out.println("Saldo restante: " + saldoDevedor);

            BigDecimal valorPago = readBigDecimal("Valor do pagamento (máximo " + saldoDevedor + "): ");

            vendaService.registrarPagamento(venda.getId(), valorPago);
            System.out.println("Pagamento registrado com sucesso.");
        } catch (SQLException e) {
            System.out.println("Erro ao registrar pagamento. " + e.getMessage());
        }
    }

    private void listarVendasAReceber() {
        printTitulo("Vendas a receber");

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

                    BigDecimal totalPago = vendaService.obterTotalPago(v.getId());
                    BigDecimal saldoDevedor = vendaService.obterSaldoDevedor(v.getId());

                    System.out.println("------------------------------------------------------------");
                    System.out.println("Venda #" + v.getId()
                            + " | Data: " + v.getDataVenda()
                            + " | Cliente: " + (v.getClienteNome() == null ? "(sem nome)" : v.getClienteNome()));

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

                    System.out.println("Total da venda: " + v.getValorTotal());
                    System.out.println("Total já pago: " + totalPago);
                    System.out.println("Saldo restante: " + saldoDevedor);
                    totalCliente = totalCliente.add(v.getValorTotal());
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
        printTitulo("Cadastrar cliente");

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

        emitenteRepository.salvar(cliente);
        System.out.println("Cliente cadastrado. ID: " + cliente.getId());
    }

    private void listarClientes() {
        printTitulo("Listar clientes");

        var clientes = emitenteRepository.listarClientesAtivos();

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
        printTitulo("Selecionar cliente");

        String termo = readString("Buscar por nome (Enter para listar todos): ");

        // TODO: Verificar depois
        var clientes = termo.isBlank()
                ? emitenteRepository.listarClientesAtivos()
                : emitenteRepository.buscarClientesPorNome(termo);

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
        printTitulo("Selecionar produto");

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

    private Produto selecionarProdutoParaGestao() {
        printTitulo("Selecionar produto");

        String termo = readString("Buscar produto por nome (Enter para listar todos): ");

        List<Produto> produtos = termo.isBlank()
                ? produtoRepository.listarTodos()
                : produtoRepository.buscarAtivosPorNome(termo);

        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto encontrado.");
            return null;
        }

        for (int i = 0; i < produtos.size(); i++) {
            Produto p = produtos.get(i);
            System.out.printf(
                    "%d) %s | estoque: %d | ativo: %s | preço: %s%n",
                    i + 1,
                    p.getNome(),
                    p.getQuantidade(),
                    p.getAtivo() ? "Sim" : "Não",
                    p.getPrecoPadrao()
            );
        }

        while (true) {
            int escolha = readInt("Escolha um produto (0 para cancelar): ");

            if (escolha == 0) return null;

            if (escolha >= 1 && escolha <= produtos.size()) {
                return produtos.get(escolha - 1);
            }

            System.out.println("Opção inválida. Digite um número entre 1 e " + produtos.size() + " (ou 0 para cancelar).");
        }
    }

    private Venda selecionarVendaAReceberPorNumero() {
        printTitulo("Selecionar vend a receber");

        try {
            List<Venda> vendas = vendaService.listarVendasAReceberComItens();

            if (vendas.isEmpty()) {
                System.out.println("Nenhum venda encontrado.");
                return null;
            }

            for (int i = 0; i < vendas.size(); i++) {
                Venda v = vendas.get(i);
                BigDecimal totalPago = vendaService.obterTotalPago(v.getId());
                BigDecimal saldoDevedor = vendaService.obterSaldoDevedor(v.getId());
                System.out.printf(
                        "%d) Venda #%d | Data: %s | Cliente: %s | Status: %s | Total: %s | Pago: %s | Falta: %s%n",
                        i + 1,
                        v.getId(),
                        v.getDataVenda(),
                        v.getClienteNome() == null ? "(sem nome)" : v.getClienteNome(),
                        v.getStatus(),
                        v.getValorTotal(),
                        totalPago,
                        saldoDevedor
                );
            }

            while (true) {
                int escolha = readInt("Escolha uma venda (0 para cancelar): ");

                if (escolha == 0) return null;

                if (escolha >= 1 && escolha <= vendas.size()) {
                    return vendas.get(escolha - 1);
                }

                System.out.println("Opção inválida. Digite um número entre 1 e " + vendas.size() + " (ou 0 para cancelar).");
            }
        }catch (Exception e) {
            System.out.println("Erro ao carregar vendas a receber: " + e.getMessage());
            return null;
        }
    }
}
