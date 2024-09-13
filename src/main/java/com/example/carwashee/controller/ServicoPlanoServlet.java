package com.example.carwashee.controller;

import com.example.carwashee.dao.DatabaseConnection;
import com.example.carwashee.dao.ServicoDAO;
import com.example.carwashee.model.Plano;
import com.example.carwashee.model.Servico;
import com.google.gson.Gson;
import com.mysql.cj.xdevapi.JsonArray;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/obterServicosPorPlano")
public class ServicoPlanoServlet extends HttpServlet {

    private Connection connection;
    private ServicoDAO servicoDAO;

    @Override
    public void init() {
        try {
            connection = DatabaseConnection.getConnection();
            servicoDAO = new ServicoDAO(connection);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String planoStr = req.getParameter("plano");
        Plano plano = Plano.valueOf(planoStr);

        List<Servico> servicos;

        try {
            servicos = servicoDAO.obterServicosPorPlano(plano);
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Erro ao obter serviços");
            return;
        }

        // Converter a lista de serviços em JSON
        Gson gson = new Gson();
        String json = gson.toJson(servicos);

        resp.setContentType("application/json");
        resp.getWriter().write(json);
    }
}
