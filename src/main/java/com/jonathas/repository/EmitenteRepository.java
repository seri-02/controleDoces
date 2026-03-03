package com.jonathas.repository;

import com.jonathas.database.ConnectionFactory;
import com.jonathas.model.Emitente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmitenteRepository {

    public void salvar(Emitente emitente) {
        String sql = "INSERT INTO emitente (nome, documento, tipo, ativo) VALUES (?, ?, ?, ?)";

        try(Connection connection = ConnectionFactory.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, emitente.getNome());
            stmt.setString(2, emitente.getDocumento());
            stmt.setString(3, emitente.getTipo());
            stmt.setBoolean(4, emitente.getAtivo());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                emitente.setId(rs.getLong(1));
            }

            System.out.println("Emitente salvo com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao salvar emitente:");
            e.printStackTrace();
        }
    }

    public List<Emitente> listarTodos() {

        String sql = "SELECT * FROM emitente";

        List<Emitente> emitentes = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Emitente emitente = new Emitente();
                emitente.setId(rs.getLong("id"));
                emitente.setNome(rs.getString("nome"));
                emitente.setDocumento(rs.getString("documento"));
                emitente.setTipo(rs.getString("tipo"));
                emitente.setAtivo(rs.getBoolean("ativo"));

                emitentes.add(emitente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return emitentes;
    }

    public Emitente buscarPorId(Long id) {
        String sql = "SELECT * FROM emitente WHERE id = ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Emitente emitente = new Emitente();
                    emitente.setId(rs.getLong("id"));
                    emitente.setNome(rs.getString("nome"));
                    emitente.setDocumento(rs.getString("documento"));
                    emitente.setTipo(rs.getString("tipo"));
                    emitente.setAtivo(rs.getBoolean("ativo"));
                    return emitente;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Emitente> listarClientesAtivos() {
        String sql = """
            SELECT id, nome, documento, tipo, ativo
            FROM emitente
            WHERE ativo = 1 AND tipo IN ('CLIENTE', 'AMBOS')
            ORDER BY nome ASC
        """;

        List<Emitente> clientes = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Emitente e = new Emitente();
                e.setId(rs.getLong("id"));
                e.setNome(rs.getString("nome"));
                e.setDocumento(rs.getString("documento"));
                e.setTipo(rs.getString("tipo"));
                e.setAtivo(rs.getBoolean("ativo"));
                clientes.add(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }

    public List<Emitente> buscarClientesPorNome(String termo) {
        String sql = """
            SELECT id, nome, documento, tipo, ativo
            FROM emitente
            WHERE ativo = 1
              AND tipo IN ('CLIENTE', 'AMBOS')
              AND nome LIKE ?
            ORDER BY nome ASC
        """;

        List<Emitente> clientes = new ArrayList<>();
        String like = "%" + termo + "%";

        try (Connection connection = ConnectionFactory.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, like);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Emitente e = new Emitente();
                    e.setId(rs.getLong("id"));
                    e.setNome(rs.getString("nome"));
                    e.setDocumento(rs.getString("documento"));
                    e.setTipo(rs.getString("tipo"));
                    e.setAtivo(rs.getBoolean("ativo"));
                    clientes.add(e);
                }
            }
        } catch (SQLException e) {
                e.printStackTrace();
        }

        return clientes;
    }
}
