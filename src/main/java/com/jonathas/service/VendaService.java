package com.jonathas.service;

import com.jonathas.database.ConnectionFactory;
import com.jonathas.model.ItemVenda;
import com.jonathas.model.Venda;
import com.jonathas.model.Pagamento;
import com.jonathas.repository.*;
import com.jonathas.repository.VendaRepository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class VendaService {

    private final VendaRepository vendaRepository;
    private final ProdutoRepository produtoRepository;
    private final ItemVendaRepository itemVendaRepository;
    private final PagamentoRepository pagamentoRepository;

    public VendaService() {
        this.vendaRepository = new VendaRepository();
        this.produtoRepository = new ProdutoRepository();
        this.itemVendaRepository = new ItemVendaRepository();
        this.pagamentoRepository = new PagamentoRepository();
    }

    // Registra uma venda com múltiplos itens em uma única transação
    public long registrarVenda(List<ItemVenda> itens, Long clienteId, BigDecimal valorPagoInicial) throws SQLException {
        validarItens(itens);
        validarCliente(clienteId);

        BigDecimal valorTotal = calcularTotalVenda(itens);
        validarValorPagoInicial(valorPagoInicial, valorTotal);

        String status = definirStatus(valorTotal, valorPagoInicial);

        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false);

            try {
                // Valida antes de gravar
                for (ItemVenda item : itens) {
                    validarItem(conn, item);
                }

                // Insere cabeçalho
                Venda venda = new Venda(LocalDateTime.now());
                venda.setClienteId(clienteId);
                venda.setStatus(status);
                venda.setValorTotal(valorTotal);

                long vendaId = vendaRepository.inserir(venda, conn);

                // Inserir e dar baixa
                for (ItemVenda item : itens) {
                    itemVendaRepository.inserir(item, vendaId, conn);
                    produtoRepository.baixarEstoque(conn, item.getProdutoId(), item.getQuantidade());
                }

                // Registra pagamento inicial, se houver
                if (valorPagoInicial.compareTo(BigDecimal.ZERO) > 0) {
                    Pagamento pagamento = new Pagamento(vendaId, LocalDateTime.now(), valorPagoInicial);
                    pagamentoRepository.inserir(pagamento, conn);
                }

                conn.commit();
                return vendaId;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    private void validarCliente(Long clienteId) throws SQLException {
        if (clienteId == null || clienteId <= 0) {
            throw new SQLException("Cliente é obrigatório para registar venda.");
        }
    }

    private void validarValorPagoInicial(BigDecimal valorPagoInicial, BigDecimal valorTotal) throws SQLException {
        if (valorPagoInicial == null) {
            throw new SQLException("O valor pago inicial não pode ser nulo.");
        }

        if (valorPagoInicial.compareTo(BigDecimal.ZERO) < 0) {
            throw new SQLException("O valor pago inicial não pode ser negativo.");
        }

        if (valorPagoInicial.compareTo(valorTotal) > 0) {
            throw new SQLException("O valor pago inicial não pode ser maior que o total da venda.");
        }
    }

    private String definirStatus(BigDecimal valorTotal, BigDecimal valorPagoInicial) throws SQLException {
        if (valorPagoInicial.compareTo(BigDecimal.ZERO) == 0) {
            return "A_RECEBER";
        }

        if (valorPagoInicial.compareTo(valorTotal) < 0) {
            return "PARCIAL";
        }

        if (valorPagoInicial.compareTo(valorTotal) == 0) {
            return "PAGO";
        }

        throw new SQLException("Não foi possível definir o status da venda.");
    }

    private BigDecimal calcularTotalVenda(List<ItemVenda> itens) {
        BigDecimal total = BigDecimal.ZERO;
        for (ItemVenda item : itens) {
            total = total.add(item.getSubtotal());
        }
        return total;
    }

    public Venda buscarVendaComItens(long vendaId) throws SQLException {
        try(Connection conn = ConnectionFactory.getConnection()) {
            // buscar cabeçalho por ID
            String sql = "SELECT id, data_venda, cliente_id, status FROM venda WHERE id = ?";
            Venda venda = null;

            try (var stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, vendaId);
                try (var rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        venda = new Venda();
                        venda.setId(rs.getLong("id"));
                        venda.setDataVenda(rs.getTimestamp("data_venda").toLocalDateTime());
                    }
                }
            }
            if (venda == null) return null;

            venda.setItens(itemVendaRepository.listarPorVendaId(vendaId, conn));
            return venda;
        }
    }

    public void registrarPagamentoIntegral(long vendaId) throws SQLException {
        try(Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false);

            try {
                Venda venda = buscarVendaComItensDentroDaMesmaConexao(vendaId, conn);
                if (venda == null) {
                    throw new SQLException("Venda não encontrada (id= " + vendaId + ").");
                }

                if ("PAGO".equalsIgnoreCase(venda.getStatus())) {
                    throw new SQLException("Venda já está como PAGO.");
                }

                BigDecimal total = venda.getTotal();
                Pagamento pagamento = new Pagamento(vendaId, LocalDateTime.now(), total);
                pagamentoRepository.inserir(pagamento, conn);

                vendaRepository.atualizarStatus(vendaId, "PAGO", conn);

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    private Venda buscarVendaPorId (long vendaId, Connection conn) throws SQLException {
        return vendaRepository.buscarPorId(vendaId, conn);
    }

    public void registrarPagamento(long vendaId, BigDecimal valorPago) throws SQLException {
        if (valorPago == null || valorPago.compareTo(BigDecimal.ZERO) <= 0) {
            throw new SQLException("O valor do pagamento deve ser maior que zero.");
        }

        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false);

            try {
                Venda venda = buscarVendaPorId(vendaId, conn);

                if (venda == null) {
                    throw new SQLException("Venda não encontrada.");
                }

                if ("PAGO".equalsIgnoreCase(venda.getStatus())) {
                    throw new SQLException("Essa venda já está totalmente paga.");
                }

                BigDecimal totalPagoAtual = pagamentoRepository.somarPagamentosPorVenda(vendaId, conn);
                BigDecimal saldoAtual = venda.getValorTotal().subtract(totalPagoAtual);

                if (valorPago.compareTo(saldoAtual) > 0) {
                    throw new SQLException("O valor do pagamento não pode ser maior que o saldo devedor.");
                }

                Pagamento pagamento = new Pagamento(vendaId, LocalDateTime.now(), valorPago);
                pagamentoRepository.inserir(pagamento, conn);

                BigDecimal novoTotalPago = totalPagoAtual.add(valorPago);
                String novoStatus = definirStatus(venda.getValorTotal(), novoTotalPago);

                vendaRepository.atualizarStatus(vendaId, novoStatus, conn);

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public BigDecimal obterTotalPago(long vendaId) throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection()) {
            return pagamentoRepository.somarPagamentosPorVenda(vendaId, conn);
        }
    }

    public BigDecimal obterSaldoDevedor(long vendaId) throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection()) {
            Venda venda = buscarVendaPorId(vendaId, conn);

            if (venda == null) {
                throw new SQLException("Venda não encontrada.");
            }

            BigDecimal totalPago = pagamentoRepository.somarPagamentosPorVenda(vendaId, conn);
            return venda.getValorTotal().subtract(totalPago);
        }
    }

    // Evitar abrir outra conexão dentro da transação
    private Venda buscarVendaComItensDentroDaMesmaConexao(long vendaId, Connection conn) throws SQLException {
        String sql = "SELECT id, data_venda, cliente_id, status FROM venda WHERE id = ?";
        Venda venda = null;

        try (var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, vendaId);
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    venda = new Venda();
                    venda.setId(rs.getLong("id"));
                    venda.setDataVenda(rs.getTimestamp("data_venda").toLocalDateTime());

                    long cid = rs.getLong("cliente_id");
                    venda.setClienteId(rs.wasNull() ? null : cid);

                    venda.setStatus(rs.getString("status"));
                }
            }
        }

        if (venda == null) return null;

        venda.setItens(itemVendaRepository.listarPorVendaId(vendaId, conn));
        return venda;
    }

    public List<Venda> listarVendasComItens() throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection()) {
            return vendaRepository.listarVendasDetalhadas(conn);
        }
    }

    private void validarItens(List<ItemVenda> itens) throws SQLException {
        if (itens == null || itens.isEmpty()) {
            throw new SQLException("A venda precisa ter pelo menos 1 item");
        }
    }

    private void validarItem(Connection conn, ItemVenda item) throws SQLException {
        if (item == null) {
            throw new SQLException("Item da venda inválido.");
        }
        if (item.getProdutoId() == null) {
            throw new SQLException("Item da venda sem produto Id");
        }
        if (item.getQuantidade() <= 0) {
            throw new SQLException("Quantidade deve ser maior que 0");
        }
        if (item.getValorUnitario() == null || item.getValorUnitario().signum() <= 0) {
            throw new SQLException("Valor unitário deve ser maior que 0");
        }

        // Produto existe?
        var produto = produtoRepository.buscarPorId(conn, item.getProdutoId());
        if (produto == null) {
            throw new SQLException("Produto não encontrado (id=" + item.getProdutoId() + ").");
        }

        // Estoque suficiente?
        int estoqueAtual = produtoRepository.buscarEstoqueAtual(conn, item.getProdutoId());
        if (estoqueAtual < item.getQuantidade()) {
            throw new SQLException("Estoque insuficiente para produto id=" + item.getProdutoId() + "(estoque=" + estoqueAtual + ", solicitado=" + item.getQuantidade() + ").");
        }

    }

    public void atualizarStatus(long vendaId, String satus, Connection conn) throws SQLException {
        String sql = "UPDATE venda SET status = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, satus);
            stmt.setLong(2, vendaId);

            int linhas = stmt.executeUpdate();
            if (linhas == 0) {
                throw new SQLException("Venda não encontrada status (id= " + vendaId + ").");
            }
        }
    }

    public List<Venda> listarVendasAReceberComItens() throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection()) {
            return vendaRepository.listarAReceberDetalhadas(conn);
        }
    }

}
