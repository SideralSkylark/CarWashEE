package com.example.carwashee.controller;

import com.example.carwashee.dao.ProdutoDAO;
import com.example.carwashee.model.Produto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import com.google.gson.Gson;

@WebServlet("/adicionarProduto")
public class AdicionarProdutoServlet extends HttpServlet {
    private final Gson gson = new Gson();
    private ProdutoDAO produtoDAO;

    public AdicionarProdutoServlet() {
        try {
            this.produtoDAO = new ProdutoDAO();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        try {
            String json = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);

            if (json.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(gson.toJson("Corpo da requisição vazio."));
                return;
            }

            Produto produto = gson.fromJson(json, Produto.class);

            if (produto.getTipoProduto() == null || produto.getQuantia() <= 0) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(gson.toJson("Dados de produto inválidos."));
                return;
            }

            // Adiciona o produto
            produtoDAO.adicionarProduto(produto);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(gson.toJson("Produto adicionado com sucesso."));

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Erro ao adicionar produto: " + e.getMessage()));
        }
    }
}
