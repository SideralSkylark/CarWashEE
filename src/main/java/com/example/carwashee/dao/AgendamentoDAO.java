package com.example.carwashee.dao;

import com.example.carwashee.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AgendamentoDAO {

    private final Connection connection;

    public AgendamentoDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean adicionarAgendamento(Agendamento agendamento) throws SQLException {
        String sql = "INSERT INTO agendamentos (usuario_id, servico_id, data, descricao, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, agendamento.getUsuarioId());
            stmt.setInt(2, agendamento.getServicoId());
            stmt.setDate(3, java.sql.Date.valueOf(agendamento.getData())); // Converte LocalDate para java.sql.Date
            stmt.setString(4, agendamento.getDescricao());
            stmt.setString(5, "PENDENTE"); // Defina um status padrão ou ajuste conforme necessário
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        }
    }

    public void adicionarAgendamento(int usuarioId, int servicoId, LocalDate data, String descricao) throws SQLException {
        String sql = "INSERT INTO agendamentos (usuario_id, servico_id, data, descricao) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            stmt.setInt(2, servicoId);
            stmt.setDate(3, java.sql.Date.valueOf(data));
            stmt.setString(4, descricao);
            stmt.executeUpdate();
        }
    }

    public boolean editarAgendamento(Agendamento agendamento) throws SQLException {
        String sql = "UPDATE agendamentos SET descricao = ?, data = ?, servico_id = ?, status = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, agendamento.getDescricao());
            stmt.setDate(2, java.sql.Date.valueOf(agendamento.getData())); // Converte LocalDate para java.sql.Date
            stmt.setInt(3, agendamento.getServicoId());
            stmt.setString(4, agendamento.getStatus().name()); // Assume que você tem um enum StatusAgendamento
            stmt.setInt(5, agendamento.getId());
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        }
    }

    public boolean editarAgendamentoCliente(Agendamento agendamento) throws SQLException {
        String sql = "UPDATE agendamentos SET descricao = ?, data = ?, servico_id = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, agendamento.getDescricao());
            stmt.setDate(2, java.sql.Date.valueOf(agendamento.getData())); // Converte LocalDate para java.sql.Date
            stmt.setInt(3, agendamento.getServicoId());
            stmt.setInt(4, agendamento.getId());
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        }
    }

    public boolean atualizarAgendamentoInterface(Agendamento agendamento) throws SQLException {
        String sql = "UPDATE agendamentos SET servico_id = ?, data = ?, descricao = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, agendamento.getServicoId());
            stmt.setDate(2, java.sql.Date.valueOf(agendamento.getData())); // Converte LocalDate para java.sql.Date
            stmt.setString(3, agendamento.getDescricao());
            stmt.setInt(4, agendamento.getId());
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        }
    }

    public void adicionarAgendamento(int servicoId, LocalDate data, String descricao) throws SQLException {
        String sql = "INSERT INTO agendamentos (servico_id, data, descricao, status) VALUES (?, ?, ?, 'PENDENTE')";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, servicoId);
            stmt.setDate(2, java.sql.Date.valueOf(data));
            stmt.setString(3, descricao);
            stmt.executeUpdate();
        }
    }

    public List<Agendamento> buscarAgendamentosAtivosPorUsuarioId(int usuarioId) throws SQLException {
        List<Agendamento> agendamentosAtivos = new ArrayList<>();
        String sql = "SELECT * FROM agendamentos WHERE usuario_id = ? AND status <> 'CONFIRMADO'";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Agendamento agendamento = mapResultSetToAgendamento(rs);
                    agendamentosAtivos.add(agendamento);
                }
            }
        }
        return agendamentosAtivos;
    }
/*
    public Servico encontrarServicoPorTipoEPlano(TipoServico tipoServico, Plano plano) throws SQLException {
        String sql = "SELECT * FROM servicos WHERE tipo_servico = ? AND plano = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, String.valueOf(tipoServico));
            stmt.setString(2, String.valueOf(plano));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Supondo que você tenha um construtor em Servico que aceita os parâmetros abaixo
                    return new Servico(
                            rs.getInt("id"),
                            rs.getString("descricao"),
                            rs.getString("tipo_servico"),
                            rs.getString("plano"),
                            rs.getBigDecimal("preco")
                    );
                } else {
                    return null; // Serviço não encontrado
                }
            }
        }
    }

 */

    public List<Agendamento> buscarTodosAgendamentos() throws SQLException {
        String sql = "SELECT * FROM agendamentos";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            List<Agendamento> agendamentos = new ArrayList<>();
            while (resultSet.next()) {
                Agendamento agendamento = mapearAgendamento(resultSet);
                agendamentos.add(agendamento);
            }

            return agendamentos;
        }
    }

    public List<Agendamento> buscarAgendamentosAtivos() throws SQLException {
        List<Agendamento> agendamentosAtivos = new ArrayList<>();
        String sql = "SELECT * FROM agendamentos WHERE status <> 'CONFIRMADO'";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Agendamento agendamento = mapResultSetToAgendamento(rs);
                agendamentosAtivos.add(agendamento);
            }
        }
        return agendamentosAtivos;
    }

    private Agendamento mapResultSetToAgendamento(ResultSet rs) throws SQLException {
        Agendamento agendamento = new Agendamento();
        agendamento.setId(rs.getInt("id"));
        agendamento.setUsuarioId(rs.getInt("usuario_id"));
        agendamento.setServicoId(rs.getInt("servico_id"));
        agendamento.setData(rs.getDate("data").toLocalDate());
        agendamento.setStatus(StatusAgendamento.valueOf(rs.getString("status")));
        agendamento.setDescricao(rs.getString("descricao")); // Verifique se 'descricao' é retornada pela consulta
        return agendamento;
    }
    public List<Agendamento> buscarAgendamentosPorUsuarioId(int usuarioId) throws SQLException {
        String sql = "SELECT a.id, a.usuario_id, a.servico_id, a.data, a.status, s.descricao " +
                "FROM agendamentos a " +
                "JOIN servicos s ON a.servico_id = s.id " +
                "WHERE a.usuario_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, usuarioId);
            try (ResultSet resultSet = statement.executeQuery()) {

                List<Agendamento> agendamentos = new ArrayList<>();
                while (resultSet.next()) {
                    Agendamento agendamento = mapResultSetToAgendamento(resultSet);
                    agendamentos.add(agendamento);
                }

                return agendamentos;
            }
        }
    }




    private Agendamento mapearAgendamento(ResultSet resultSet) throws SQLException {
        Agendamento agendamento = new Agendamento();
        agendamento.setId(resultSet.getInt("id"));
        agendamento.setUsuarioId(resultSet.getInt("usuario_id"));
        agendamento.setServicoId(resultSet.getInt("servico_id"));
        agendamento.setData(resultSet.getDate("data").toLocalDate());
        agendamento.setStatus(StatusAgendamento.valueOf(resultSet.getString("status")));
        return agendamento;
    }

    public Usuario buscarUsuarioPorId(int usuarioId) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("email"),
                            rs.getString("senha"),
                            rs.getString("tipo_usuario")
                    );
                }
            }
        }
        return null;
    }

    public boolean atualizarStatusAgendamento(int id, String status) throws SQLException {
        String sql = "UPDATE agendamentos SET status = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, id);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        }
    }

    public boolean removerAgendamento(int id) throws SQLException {
        String sql = "DELETE FROM agendamentos WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public List<Agendamento> buscarAgendamentosComServicoPorUsuarioId(int usuarioId) throws SQLException {
        List<Agendamento> agendamentos = new ArrayList<>();
        String sql = "SELECT a.id, a.data, a.status, s.descricao AS servico, s.plano, s.preco, s.tipo_servico " +
                "FROM agendamentos a " +
                "JOIN servicos s ON a.servico_id = s.id " +
                "WHERE a.usuario_id = ? AND a.status <> 'CONFIRMADO'";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Agendamento agendamento = new Agendamento();
                    agendamento.setId(rs.getInt("id"));
                    agendamento.setData(rs.getDate("data").toLocalDate());
                    agendamento.setStatus(StatusAgendamento.valueOf(rs.getString("status")));

                    // Dados do serviço
                    Servico servico = new Servico();
                    servico.setDescricao(rs.getString("servico"));
                    servico.setPlano(rs.getString("plano"));
                    servico.setPreco(rs.getBigDecimal("preco"));
                    servico.setTipoServico(rs.getString("tipo_servico"));

                    agendamento.setServico(servico);

                    agendamentos.add(agendamento);
                }
            }
        }
        return agendamentos;
    }

}
