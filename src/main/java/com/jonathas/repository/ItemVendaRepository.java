package com.jonathas.repository;

import com.jonathas.model.ItemVenda;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemVendaRepository {

    public void inserir(ItemVenda item, long vendaId, Connection conn) throws SQLException {
        String sql = """
            INSERT INTO item_venda (venda_id, produto_id, quantidade, valor_unitario)
            VALUES (?, ?, ?, ?)
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, vendaId);
            stmt.setLong(2, item.getProdutoId());
            stmt.setInt(3, item.getQuantidade());
            stmt.setBigDecimal(4, item.getValorUnitario());

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    item.setId(keys.getLong(1));
                }
            }
        }
    }

    public List<ItemVenda> listarPorVendaId(long vendaId, Connection conn) throws SQLException {
        String sql = """
            SELECT id, venda_id, produto_id, quantidade, valor_unitario
            FROM item_venda
            WHERE venda_id = ?
            ORDER BY id ASC
        """;

        List<ItemVenda> itens = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, vendaId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ItemVenda itemVenda = new ItemVenda();
                    itemVenda.setId(rs.getLong("venda_id"));
                    itemVenda.setProdutoId(rs.getLong("produto_id"));
                    itemVenda.setQuantidade(rs.getInt("quantidade"));
                    itemVenda.setValorUnitario(rs.getBigDecimal("valor_unitario"));
                    itens.add(itemVenda);
                }
            }
        }
        return itens;
    }


}
