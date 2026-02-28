package com.jonathas.service;

import com.jonathas.database.ConnectionFactory;
import com.jonathas.model.ItemVenda;
import com.jonathas.model.Venda;
import com.jonathas.repository.ItemVendaRepository;
import com.jonathas.repository.ProdutoRepository;
import com.jonathas.repository.VendaRepository;
import java.util.ArrayList;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class VendaService {

    private final VendaRepository vendaRepository;
    private final ProdutoRepository produtoRepository;
    private final ItemVendaRepository itemVendaRepository;

    public VendaService() {
        this.vendaRepository = new VendaRepository();
        this.produtoRepository = new ProdutoRepository();
        this.itemVendaRepository = new ItemVendaRepository();
    }

    // Registra uma venda com múltiplos itens em uma única transação
    public long registrarVenda(List<ItemVenda> itens) throws SQLException {
        validarItens(itens);

        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false);

            try {
                // Valida antes de gravar
                for (ItemVenda item : itens) {
                    validarItem(conn, item);
                }

                // Insere cabeçalho
                Venda venda = new Venda(LocalDateTime.now());
                long vendaId = vendaRepository.inserir(venda, conn);

                // Inserir e dar baixa
                for (ItemVenda item : itens) {
                    itemVendaRepository.inserir(item, vendaId, conn);
                    produtoRepository.baixarEstoque(conn, item.getProdutoId(), item.getQuantidade());
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

}
