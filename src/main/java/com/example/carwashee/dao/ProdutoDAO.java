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

    // Método para atualizar a quantidade de um produto existente
    public boolean atualizarProduto(TipoProduto tipoProduto, int valor) throws SQLException {
        String sqlObterQuantidade = "SELECT quantia FROM produto WHERE tipo_produto = ?";
        String sqlAtualizar = "UPDATE produto SET quantia = ? WHERE tipo_produto = ?";

        try (PreparedStatement pstmtObterQuantidade = connection.prepareStatement(sqlObterQuantidade);
             PreparedStatement pstmtAtualizar = connection.prepareStatement(sqlAtualizar)) {

            // Primeiro, buscamos a quantidade atual do produto
            pstmtObterQuantidade.setString(1, tipoProduto.name());
            ResultSet rs = pstmtObterQuantidade.executeQuery();

            if (rs.next()) {
                int quantidadeAtual = rs.getInt("quantia");
                int novaQuantidade = quantidadeAtual + valor;  // Incrementa ou decrementa a quantidade

                // Impede que a quantidade fique negativa
                if (novaQuantidade < 0) {
                    novaQuantidade = 0;
                }

                pstmtAtualizar.setInt(1, novaQuantidade);
                pstmtAtualizar.setString(2, tipoProduto.name());
                pstmtAtualizar.executeUpdate();

                return true;
            } else {
                return false;  // Produto não encontrado
            }

        } catch (SQLException e) {
            throw new SQLException("Erro ao atualizar produto", e);
        }
    }

    // Método para adicionar ou atualizar o produto
    public boolean adicionarOuAtualizarProduto(Produto produto) throws SQLException {
        String sqlObterQuantidade = "SELECT quantia FROM produto WHERE tipo_produto = ?";
        String sqlAtualizar = "UPDATE produto SET quantia = ? WHERE tipo_produto = ?";
        String sqlInserir = "INSERT INTO produto (tipo_produto, quantia) VALUES (?, ?)";

        try (PreparedStatement pstmtObterQuantidade = connection.prepareStatement(sqlObterQuantidade);
             PreparedStatement pstmtAtualizar = connection.prepareStatement(sqlAtualizar);
             PreparedStatement pstmtInserir = connection.prepareStatement(sqlInserir)) {

            pstmtObterQuantidade.setString(1, produto.getTipoProduto().name());
            ResultSet rs = pstmtObterQuantidade.executeQuery();

            if (rs.next()) {
                // Produto já existe, então vamos atualizar
                int quantidadeAtual = rs.getInt("quantia");
                int novaQuantidade = quantidadeAtual + produto.getQuantia();
                pstmtAtualizar.setInt(1, novaQuantidade);
                pstmtAtualizar.setString(2, produto.getTipoProduto().name());
                pstmtAtualizar.executeUpdate();
            } else {
                // Produto não existe, vamos inserir um novo
                pstmtInserir.setString(1, produto.getTipoProduto().name());
                pstmtInserir.setInt(2, produto.getQuantia());
                pstmtInserir.executeUpdate();
            }

            return true;

        } catch (SQLException e) {
            throw new SQLException("Erro ao adicionar ou atualizar produto", e);
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