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
    public long registrarVenda(List<ItemVenda> itens, Long clienteId, String status) throws SQLException {
        validarItens(itens);

        if (status == null || status.isBlank()) {
            status = "PAGO";
        }
        status = status.toUpperCase();

        if (!status.equals("PAGO") && !status.equals("A_RECEBER")) {
            throw new SQLException("Status inválido. Use PAGO ou A_RECEBER");
        }

        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false);

            try {
                // Valida antes de gravar
                for (ItemVenda item : itens) {
                    validarItem(conn, item);
                }

                // Valida cliente se vier preenchido
                if (clienteId != null) {
                    //Valida se existe na tabela emitente
                    try (var stmt = conn.prepareStatement("SELECT id FROM emitente WHERE id = ? AND ativo = 1")) {
                        stmt.setLong(1, clienteId);
                        try (var rs = stmt.executeQuery()) {
                            if (rs.next()) {
                                throw new SQLException("Cliente não encontrado ou inativo (id= " + clienteId + ").");
                            }
                        }
                    }
                }

                // Insere cabeçalho
                Venda venda = new Venda(LocalDateTime.now());
                venda.setClienteId(clienteId);
                venda.setStatus(status);

                long vendaId = vendaRepository.inserir(venda, conn);

                // Inserir e dar baixa
                for (ItemVenda item : itens) {
                    itemVendaRepository.inserir(item, vendaId, conn);
                    produtoRepository.baixarEstoque(conn, item.getProdutoId(), item.getQuantidade());
                }

                // Se status PAGO, registra pagamento
                if (status.equals("PAGO")) {
                    BigDecimal total = calcularTotalVenda(itens);
                    Pagamento pagamento = new Pagamento(vendaId, LocalDateTime.now(), total);
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
            List<Venda> vendas = vendaRepository.listarCabecalhos(conn);

            for (Venda v : vendas) {
                v.setItens(itemVendaRepository.listarPorVendaId(v.getId(), conn));
            }
            return vendas;
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
                throw new SQLException("Venda no encontrada status (id= " + vendaId + ").");
            }
        }
    }

    public List<Venda> listarVendasAReceberComItens() throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection()) {
            List<Venda> vendas = vendaRepository.listarAReceber(conn);

            for (Venda v : vendas) {
                v.setItens(itemVendaRepository.listarPorVendaId(v.getId(), conn));
            }

            return vendas;
        }
    }

}
