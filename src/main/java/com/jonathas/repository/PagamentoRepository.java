package com.jonathas.repository;

import com.jonathas.model.Pagamento;
import java.sql.*;

public class PagamentoRepository {

    public long inserir(Pagamento pagamento, Connection conn) throws SQLException {
        String sql = "INSERT INTO pagamento (venda_id, data_pagamento, valor_pago) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, pagamento.getVendaId());
            stmt.setTimestamp(2, Timestamp.valueOf(pagamento.getDataPagamento()));
            stmt.setBigDecimal(3, pagamento.getValorPago());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) return keys.getLong(1);
            }
        }

        throw new SQLException("Falha ao inserir pagamento");
    }
}
