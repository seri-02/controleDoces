package com.jonathas.repository;

import com.jonathas.database.ConnectionFactory;
import com.jonathas.model.Produto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoRepository {

    public void salvar(Produto produto) {
        String sql = "INSERT INTO produto (nome, descricao, ativo, quantidade, preco_padrao, custo_unitario) VALUES (?, ?, ?, ?, ?, ?)";

        try(Connection connection = ConnectionFactory.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setBoolean(3, produto.getAtivo());
            stmt.setInt(4, produto.getQuantidade());
            stmt.setBigDecimal(5, produto.getPrecoPadrao());
            stmt.setBigDecimal(6, produto.getCustoUnitario());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                produto.setId(rs.getLong(1));
            }

            System.out.println("Produto salvo com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao salvar produto:");
            e.printStackTrace();
        }
    }

    public List<Produto> listarTodos() {

        String sql = "SELECT * FROM produto";

        List<Produto> produtos = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Produto produto = new Produto();
                produto.setId(rs.getLong("id"));
                produto.setNome(rs.getString("nome"));
                produto.setDescricao(rs.getString("descricao"));
                produto.setAtivo(rs.getBoolean("ativo"));
                produto.setQuantidade(rs.getInt("quantidade"));
                produto.setPrecoPadrao(rs.getBigDecimal("preco_padrao"));
                produto.setCustoUnitario(rs.getBigDecimal("custo_unitario"));

                produtos.add(produto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return produtos;
    }

    public Produto buscarPorId(Long id) {

        String sql = "SELECT * FROM produto WHERE id = ?";

        try (Connection connection = ConnectionFactory.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Produto produto = new Produto();
                    produto.setId(rs.getLong("id"));
                    produto.setNome(rs.getString("nome"));
                    produto.setDescricao(rs.getString("descricao"));
                    produto.setAtivo(rs.getBoolean("ativo"));
                    produto.setQuantidade(rs.getInt("quantidade"));
                    produto.setPrecoPadrao(rs.getBigDecimal("preco_padrao"));
                    produto.setCustoUnitario(rs.getBigDecimal("custo_unitario"));

                    return produto;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Produto buscarPorId (Connection conn, Long id) throws SQLException {
        String sql = "SELECT * FROM produto WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Produto produto = new Produto();
                    produto.setId(rs.getLong("id"));
                    produto.setNome(rs.getString("nome"));
                    produto.setDescricao(rs.getString("descricao"));
                    produto.setAtivo(rs.getBoolean("ativo"));
                    produto.setQuantidade(rs.getInt("quantidade"));
                    return produto;
                }
            }
        }
        return null;
    }

    public int buscarEstoqueAtual(Connection conn, Long produtoId) throws SQLException {
        String sql = "SELECT quantidade FROM produto WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, produtoId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    throw new SQLException("Produto não encontrado (id=" + produtoId + ").");
                }
                return rs.getInt("quantidade");
            }
        }
    }

    public void baixarEstoque(Connection conn, Long produtoId, int quantidade) throws SQLException {
            String sql = """
            UPDATE produto
            SET quantidade = quantidade - ?
            WHERE id = ?
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantidade);
            stmt.setLong(2, produtoId);

            int linhas = stmt.executeUpdate();
            if (linhas == 0) {
                throw new SQLException("Falha ao atualizar estoque do produto.");
            }
        }
    }

    public void atualizar(Produto produto) {
        String sql = "UPDATE produto " +
                "SET nome = ?, descricao = ?, ativo = ?, preco_padrao = ?, custo_unitario = ? " +
                "WHERE id = ?";

        try (Connection connection = ConnectionFactory.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)) {


            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setBoolean(3, produto.getAtivo());
            stmt.setBigDecimal(4, produto.getPrecoPadrao());
            stmt.setBigDecimal(5, produto.getCustoUnitario());
            stmt.setLong(6, produto.getId());

            int linhas = stmt.executeUpdate();
            if (linhas == 0) {
                System.out.println("Produto não encontrado para atualizar.");
                return;
            }
            System.out.println("Produto atualizado com sucesso.");
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar produto.");
            e.printStackTrace();
        }
    }

    public void inativar(Long id) {
        String sql = "UPDATE produto SET ativo = 0 WHERE id = ?";

        try (Connection connection = ConnectionFactory.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, id);

            int linhas = stmt.executeUpdate();
            if (linhas == 0) {
                System.out.println("Produto não encontrado");
                return;
            }

            System.out.println("Produto inativado com sucesso");
        } catch (SQLException e) {
            System.out.println("Erro ao inativar produto.");
            e.printStackTrace();
        }
    }

    public void ajustarEstoque(Long id, int delta) {
        String selectSql = "SELECT quantidade FROM produto WHERE id = ?";
        String updateSql = "UPDATE produto SET quantidade = ? WHERE id = ?";

        try (Connection connection = ConnectionFactory.getConnection()) {
            connection.setAutoCommit(false);

            try {
                int atual;

                // Buscar estoque atual
                try (PreparedStatement stmt = connection.prepareStatement(selectSql)) {
                    stmt.setLong(1, id);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (!rs.next()) {
                            System.out.println("Produto não encontrado");
                            connection.rollback();
                            return;
                        }
                        atual = rs.getInt("quantidade");
                    }
                }

                int novo = atual + delta;
                if (novo < 0) {
                    System.out.println("Ajuste inválido, estoque não pode ficar negativo.");
                    connection.rollback();
                    return;
                }

                // Atualizar
                try (PreparedStatement stmt = connection.prepareStatement(updateSql)) {
                    stmt.setInt(1, novo);
                    stmt.setLong(2, id);
                    stmt.executeUpdate();
                }

                connection.commit();
                System.out.println("Estoque ajustado com sucesso. Novo estoque: " + novo);
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao ajustar estoque: ");
            e.printStackTrace();
        }
    }

    public List<Produto> buscarAtivosPorNome(String termo) {
        String sql = """
            SELECT id, nome, descricao, ativo, quantidade, preco_padrao, custo_unitario
            FROM produto
            WHERE ativo = 1 AND nome LIKE ?
            ORDER BY nome ASC
        """;

        List<Produto> produtos = new ArrayList<>();
        String like = "%" + termo + "%";

        try (Connection connection = ConnectionFactory.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, like);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Produto p = new Produto();
                    p.setId(rs.getLong("id"));
                    p.setNome(rs.getString("nome"));
                    p.setDescricao(rs.getString("descricao"));
                    p.setAtivo(rs.getBoolean("ativo"));
                    p.setQuantidade(rs.getInt("quantidade"));
                    p.setPrecoPadrao(rs.getBigDecimal("preco_padrao"));
                    p.setCustoUnitario(rs.getBigDecimal("custo_unitario"));
                    produtos.add(p);
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return produtos;
    }

    public List<Produto> buscarPorNome(String termo) {
        String sql = "SELECT * FROM produto WHERE nome LIKE ? ORDER BY nome ASC";

        List<Produto> produtos = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + termo + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()){
                    Produto produto = new Produto();
                    produto.setId(rs.getLong("id"));
                    produto.setNome(rs.getString("nome"));
                    produto.setDescricao(rs.getString("descricao"));
                    produto.setAtivo(rs.getBoolean("ativo"));
                    produto.setQuantidade(rs.getInt("quantidade"));
                    produto.setPrecoPadrao(rs.getBigDecimal("preco_padrao"));
                    produto.setCustoUnitario(rs.getBigDecimal("custo_unitario"));
                    produtos.add(produto);
                }
            }
        }catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar produtos por nome.", e);
        }

        return produtos;
    }

}
