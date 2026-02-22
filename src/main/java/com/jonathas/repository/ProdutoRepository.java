package com.jonathas.repository;

import com.jonathas.database.ConnectionFactory;
import com.jonathas.model.Produto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoRepository {

    public void salvar(Produto produto) {
        String sql = "INSERT INTO produto (nome, descricao, ativo, quantidade) VALUES (?, ?, ?, ?)";

        try(Connection connection = ConnectionFactory.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setBoolean(3, produto.getAtivo());
            stmt.setInt(4, produto.getQuantidade());

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

                    return produto;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}
