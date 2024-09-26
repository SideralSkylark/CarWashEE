package com.example.carwashee.controller;

import com.example.carwashee.dao.ProdutoDAO;
import com.example.carwashee.model.ProdutoEdicao;
import com.example.carwashee.model.TipoProduto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

import com.google.gson.Gson;

@WebServlet("/editarProduto")
public class EditarProdutoServlet extends HttpServlet {
    private final Gson gson = new Gson();
    private ProdutoDAO produtoDAO;

    public EditarProdutoServlet() {
        try {
            this.produtoDAO = new ProdutoDAO();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados", e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        try {
            String json = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);

            if (json.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(gson.toJson("Corpo da requisição vazio."));
                return;
            }

            ProdutoEdicao produtoEdicao = gson.fromJson(json, ProdutoEdicao.class);

            if (produtoEdicao.getTipoProduto() == null || produtoEdicao.getNovaQuantia() <= 0) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(gson.toJson("Dados de produto inválidos."));
                return;
            }

            boolean sucesso = produtoDAO.editarProduto(TipoProduto.valueOf(String.valueOf(produtoEdicao.getTipoProduto())), produtoEdicao.getNovaQuantia());

            if (sucesso) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(gson.toJson("Produto editado com sucesso."));
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write(gson.toJson("Produto não encontrado."));
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Erro ao editar produto: " + e.getMessage()));
        }
    }
}