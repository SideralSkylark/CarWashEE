package com.example.carwashee.controller;

import com.example.carwashee.dao.ProdutoDAO;
import com.example.carwashee.model.Produto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "ObterProdutosServlet", urlPatterns = {"/obterProdutos"})
public class ObterProdutosServlet extends HttpServlet {

    private ProdutoDAO produtoDAO;

    public ObterProdutosServlet() {
        try {
            produtoDAO = new ProdutoDAO();  // Inicializa o ProdutoDAO
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Produto> produtos = null;  // Obtém todos os produtos
        try {
            produtos = produtoDAO.obterTodos();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (produtos.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);  // Nenhum conteúdo encontrado
            return;
        }

        // Converte a lista de produtos para JSON
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(produtos);

        // Define o tipo de conteúdo e escreve o JSON no response
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }
}
