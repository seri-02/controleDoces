package com.jonathas.repository;

import com.jonathas.model.Venda;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VendaRepository {

    // Cabeçalho da venda
    public long inserir(Venda venda, Connection conn) throws SQLException {
        String sql = "INSERT INTO venda (data_venda) VALUES (?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setTimestamp(1, Timestamp.valueOf(venda.getDataVenda()));
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getLong(1);
                }
            }
        }

        throw new SQLException("Falha ao inserir venda: ID não retornado.");
    }

    // Lista o cabeçalho
    public List<Venda> listarCabecalhos(Connection conn) throws SQLException {
        String sql = """
            SELECT id, data_venda
            FROM venda
            ORDER BY data_venda DESC
        """;

        List<Venda> vendas = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Venda venda = new Venda();
                venda.setId(rs.getLong("id"));
                venda.setDataVenda(rs.getTimestamp("data_venda").toLocalDateTime());
                vendas.add(venda);
            }
        }

        return vendas;
    }
}