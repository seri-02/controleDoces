package com.jonathas.repository.report;

import com.jonathas.database.ConnectionFactory;
import com.jonathas.model.report.RelatorioVendasPeriodoItem;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RelatorioVendaRepository {

    public List<RelatorioVendasPeriodoItem> buscarVendasPorPeriodo(LocalDate dataInicial, LocalDate dataFinal) {
        List<RelatorioVendasPeriodoItem> itens = new ArrayList<>();

        String sql = """
                SELECT
                    v.id,
                    v.data_venda,
                    e.nome AS cliente_nome,
                    v.status,
                    v.valor_total
                FROM venda v
                INNER JOIN emitente e ON e.id = v.cliente_id
                WHERE v.data_venda BETWEEN ? AND ?
                ORDER BY v.data_venda ASC, v.id ASC
                """;

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(dataInicial));
            stmt.setDate(2, Date.valueOf(dataFinal));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    RelatorioVendasPeriodoItem item = new RelatorioVendasPeriodoItem();
                    item.setVendaId(rs.getLong("id"));
                    item.setDataVenda(rs.getDate("data_venda").toLocalDate());
                    item.setClienteNome(rs.getString("cliente_nome"));
                    item.setStatus(rs.getString("status"));
                    item.setValorTotal(rs.getBigDecimal("valor_total"));

                    itens.add(item);
                }
            }
        }catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar relatório de vendas por período.", e);
        }
        return itens;
    }
}
