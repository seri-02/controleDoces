package com.jonathas.repository;

import com.jonathas.model.Venda;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VendaRepository {

    // Cabeçalho da venda
    public long inserir(Venda venda, Connection conn) throws SQLException {
        String sql = "INSERT INTO venda (data_venda, cliente_id, status) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setTimestamp(1, Timestamp.valueOf(venda.getDataVenda()));

            if (venda.getClienteId() == null) {
                stmt.setNull(2, Types.BIGINT);
            } else {
                stmt.setLong(2, venda.getClienteId());
            }

            stmt.setString(3, venda.getStatus());

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
            SELECT id, data_venda, cliente_id, status
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

                long clienteId = rs.getLong("cliente_id");
                if (rs.wasNull()) {
                    venda.setClienteId(null);
                } else {
                    venda.setClienteId(clienteId);
                }

                venda.setStatus(rs.getString("status"));

                vendas.add(venda);
            }
        }

        return vendas;
    }

    public void atualizarStatus(long vendaId, String status, Connection conn) throws SQLException {
        String sql = "UPDATE venda SET status = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setLong(2, vendaId);

            int linhas = stmt.executeUpdate();
            if (linhas == 0) {
                throw new SQLException("Falha ao atualizar status da venda (ID: " + vendaId + ").");
            }
        }
    }

    public List<Venda> listarAReceber(Connection conn) throws SQLException {
        String sql = """
        SELECT id, data_venda, cliente_id, status
        FROM venda
        WHERE status = 'A_RECEBER'
        ORDER BY data_venda DESC
    """;

        List<Venda> vendas = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Venda venda = new Venda();
                venda.setId(rs.getLong("id"));
                venda.setDataVenda(rs.getTimestamp("data_venda").toLocalDateTime());

                long cid = rs.getLong("cliente_id");
                venda.setClienteId(rs.wasNull() ? null : cid);

                venda.setStatus(rs.getString("status"));
                vendas.add(venda);
            }
        }
        return vendas;
    }
}