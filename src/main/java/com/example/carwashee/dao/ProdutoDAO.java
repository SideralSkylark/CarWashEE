package com.example.carwashee.dao;

import com.example.carwashee.model.Produto;
import com.example.carwashee.model.TipoProduto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    private final Connection connection;

    public ProdutoDAO() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }

    // Método para obter todos os produtos
    public List<Produto> obterTodos() throws SQLException {
        String sql = "SELECT * FROM produto";
        List<Produto> produtos = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                TipoProduto tipoProduto = TipoProduto.valueOf(rs.getString("tipo_produto"));
                int quantia = rs.getInt("quantia");
                Produto produto = new Produto(tipoProduto, quantia);
                produtos.add(produto);
            }
        } catch (SQLException e) {
            throw new SQLException("Erro ao obter produtos", e);
        }

        return produtos;
    }

    // Método para adicionar um novo produto
    public boolean adicionarProduto(Produto produto) throws SQLException {
        String sql = "INSERT INTO produto (tipo_produto, quantia) VALUES (?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, String.valueOf(produto.getTipoProduto()));
            pstmt.setInt(2, produto.getQuantia());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new SQLException("Erro ao adicionar produto", e);
        }
    }

    // Método para editar/atualizar a quantidade de um produto existente
    public boolean editarProduto(TipoProduto tipoProduto, int novaQuantia) throws SQLException {
        String sql = "UPDATE produto SET quantia = ? WHERE tipo_produto = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, novaQuantia);
            pstmt.setString(2, tipoProduto.name());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new SQLException("Erro ao editar produto", e);
        }
    }

    // Método para remover um produto
    public boolean removerProduto(String tipoProduto) throws SQLException {
        String sql = "DELETE FROM produto WHERE tipo_produto = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, tipoProduto);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new SQLException("Erro ao remover produto", e);
        }
    }
}
