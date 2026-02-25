package com.jonathas.repository;

import com.jonathas.database.ConnectionFactory;
import com.jonathas.model.Venda;
import com.jonathas.model.Produto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VendaRepository {

    private Connection connection;

    public VendaRepository(Connection connection) {
        this.connection = connection;
    }

    public void registrarVenda(Venda venda) throws SQLException {

        String selectEstoqueSQL = """
        SELECT quantidade
        FROM produto
        WHERE id = ?
    """;

        String insertVendaSQL = """
        INSERT INTO venda (produto_id, quantidade, valor_unitario)
        VALUES (?, ?, ?)
    """;

        String updateEstoqueSQL = """
        UPDATE produto
        SET quantidade = quantidade - ?
        WHERE id = ?
    """;

        try {connection.setAutoCommit(false);

            int estoqueAtual;

            // 1- Buscar estoque atual
            try (PreparedStatement stmtSelect = connection.prepareStatement(selectEstoqueSQL)) {
                stmtSelect.setLong(1, venda.getProduto().getId());

                try (ResultSet rs = stmtSelect.executeQuery()) {

                    if (!rs.next()) {
                        throw new SQLException("Produto não encontrado.");
                    }

                    estoqueAtual = rs.getInt("quantidade");
                }
            }

            // 2- Validar estoque
            if (estoqueAtual < venda.getQuantidade()) {
                throw new SQLException("Estoque insuficiente.");
            }

            // 3- Inserir venda
            try (PreparedStatement stmtVenda = connection.prepareStatement(insertVendaSQL)) {
                stmtVenda.setLong(1, venda.getProduto().getId());
                stmtVenda.setInt(2, venda.getQuantidade());
                stmtVenda.setBigDecimal(3, venda.getValorUnitario());

                stmtVenda.executeUpdate();
            }

            // 4- Atualizar estoque
            try (PreparedStatement stmtEstoque = connection.prepareStatement(updateEstoqueSQL)) {
                stmtEstoque.setInt(1, venda.getQuantidade());
                stmtEstoque.setLong(2, venda.getProduto().getId());

                stmtEstoque.executeUpdate();
            }

            connection.commit();
            System.out.println("Venda registrada com sucesso!");

        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public List<Venda> listarTodas() throws SQLException {

        String sql = """
            SELECT id, produto_id, quantidade, valor_unitario, data_venda
            FROM venda
            ORDER BY data_venda DESC
        """;

        List<Venda> vendas = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Produto produto = new Produto();
                produto.setId(rs.getLong("produto_id"));

                Venda venda = new Venda();
                venda.setId(rs.getLong("id"));
                venda.setProduto(produto);
                venda.setQuantidade(rs.getInt("quantidade"));
                venda.setValorUnitario(rs.getBigDecimal("valor_unitario"));

                vendas.add(venda);
            }
        }

        return vendas;
    }

}
