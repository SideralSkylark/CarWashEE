package com.example.carwashee.controller;

import com.example.carwashee.dao.ProdutoDAO;
import com.example.carwashee.model.TipoProduto;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

@WebServlet("/atualizarProduto")
public class AtualizarProdutoServlet extends HttpServlet {

    private ProdutoDAO produtoDAO;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        try {
            produtoDAO = new ProdutoDAO();
            objectMapper = new ObjectMapper(); // Inicializa o ObjectMapper
        } catch (SQLException e) {
            throw new ServletException("Erro ao inicializar o ProdutoDAO", e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            System.out.println("put acionado");

            // Converte o corpo JSON para um Map
            Map<String, Object> body = objectMapper.readValue(request.getInputStream(), Map.class);

            // Extrai os valores do JSON
            String tipoProdutoStr = (String) body.get("tipoProduto");
            int valor = (int) body.get("valor");

            TipoProduto tipoProduto = TipoProduto.valueOf(tipoProdutoStr);

            System.out.println("tipo: " + tipoProdutoStr + ", valor: " + valor);

            boolean atualizado = produtoDAO.atualizarProduto(tipoProduto, valor);

            if (atualizado) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"message\": \"Produto atualizado com sucesso\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"message\": \"Produto não encontrado\"}");
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"message\": \"Erro ao atualizar produto\"}");
        }
    }
}