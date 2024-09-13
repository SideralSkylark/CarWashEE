package com.example.carwashee.dao;

import com.example.carwashee.model.Plano;
import com.example.carwashee.model.Servico;
import com.example.carwashee.model.TipoServico;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServicoDAO {

    private final Connection connection;

    public ServicoDAO(Connection connection) {
        this.connection = connection;
    }

    public Servico buscarServicoPorId(int servicoId) throws SQLException {
        String sql = "SELECT * FROM servicos WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, servicoId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToServico(rs);
                }
            }
        }
        return null;
    }

    public List<Servico> buscarTodosServicos() throws SQLException {
        String sql = "SELECT * FROM servicos";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            List<Servico> servicos = new ArrayList<>();
            while (rs.next()) {
                Servico servico = mapResultSetToServico(rs);
                servicos.add(servico);
            }
            return servicos;
        }
    }

    private Servico mapResultSetToServico(ResultSet rs) throws SQLException {
        Servico servico = new Servico();
        servico.setId(rs.getInt("id"));
        servico.setTipoServico(rs.getString("tipo_servico"));
        servico.setPlano(rs.getString("plano"));
        servico.setDescricao(rs.getString("descricao"));
        servico.setPreco(BigDecimal.valueOf(rs.getDouble("preco")));
        return servico;
    }

    public List<Servico> obterServicosPorTipo(TipoServico tipoServico) throws SQLException {
        List<Servico> servicos = new ArrayList<>();
        String sql = "SELECT * FROM servicos WHERE tipo_servico = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, tipoServico.name());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Servico servico = new Servico();
                servico.setId(rs.getInt("id"));
                servico.setDescricao(rs.getString("descricao"));
                servico.setTipoServico(rs.getString("tipo_servico"));
                servico.setPlano(rs.getString("plano"));
                servico.setPreco(rs.getBigDecimal("preco"));
                servicos.add(servico);
            }
        }

        return servicos;
    }




    public boolean removerServico(int id) throws SQLException {
        String sql = "DELETE FROM servicos WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public Servico buscarPrecoPorTipoEPlano(TipoServico tipoServico, Plano plano) throws SQLException {
        String sql = "SELECT * FROM servicos WHERE tipo_servico = ? AND plano = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, tipoServico.name());
            stmt.setString(2, plano.name());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToServico(rs);
                }
            }
        }
        return null;
    }

    public List<Servico> obterServicosPorPlano(Plano plano) throws SQLException {
        List<Servico> servicos = new ArrayList<>();
        String sql = "SELECT * FROM servicos WHERE plano = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, plano.name());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Servico servico = new Servico();
                servico.setId(rs.getInt("id"));
                servico.setDescricao(rs.getString("descricao"));
                servico.setTipoServico(rs.getString("tipo_servico"));
                servico.setPlano(rs.getString("plano"));
                servico.setPreco(rs.getBigDecimal("preco"));
                servicos.add(servico);
            }
        }

        return servicos;
    }
}
