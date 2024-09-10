package com.example.carwashee.dao;

import com.example.carwashee.model.HistoricoServico;
import com.example.carwashee.model.Servico;
import com.example.carwashee.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistoricoServicoDAO {

    private Connection conexao;

    public HistoricoServicoDAO(Connection conexao) {
        this.conexao = conexao;
    }

    public void adicionarHistoricoServico(HistoricoServico historicoServico) throws SQLException {
        String sql = "INSERT INTO historicos_servicos (usuario_id, servico_id, data, detalhes) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, historicoServico.getUsuario().getId());
            stmt.setInt(2, historicoServico.getServico().getId());
            stmt.setDate(3, new java.sql.Date(historicoServico.getData().getTime()));
            stmt.setString(4, historicoServico.getDetalhes());
            stmt.executeUpdate();
        }
    }

    public List<HistoricoServico> listarHistoricosServicos() throws SQLException {
        List<HistoricoServico> historicosServicos = new ArrayList<>();
        String sql = "SELECT * FROM historicos_servicos";

        try (Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Usuario usuario = buscarUsuarioPorId(rs.getInt("usuario_id"));
                Servico servico = buscarServicoPorId(rs.getInt("servico_id"));

                HistoricoServico historicoServico = new HistoricoServico(
                        rs.getInt("id"),
                        usuario,
                        servico,
                        rs.getDate("data"),
                        rs.getString("detalhes")
                );
                historicosServicos.add(historicoServico);
            }
        }
        return historicosServicos;
    }

    public HistoricoServico buscarHistoricoServicoPorId(int id) throws SQLException {
        String sql = "SELECT * FROM historicos_servicos WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = buscarUsuarioPorId(rs.getInt("usuario_id"));
                    Servico servico = buscarServicoPorId(rs.getInt("servico_id"));

                    return new HistoricoServico(
                            rs.getInt("id"),
                            usuario,
                            servico,
                            rs.getDate("data"),
                            rs.getString("detalhes")
                    );
                }
            }
        }
        return null;
    }

    private Usuario buscarUsuarioPorId(int id) throws SQLException {
        // Implemente a busca do usuário pelo ID
        // Por exemplo: SELECT * FROM usuarios WHERE id = ?
        return null; // Retorne o objeto Usuario correspondente
    }

    private Servico buscarServicoPorId(int id) throws SQLException {
        // Implemente a busca do serviço pelo ID
        // Por exemplo: SELECT * FROM servicos WHERE id = ?
        return null; // Retorne o objeto Servico correspondente
    }
}
