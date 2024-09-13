package com.example.carwashee.controller;

import com.example.carwashee.dao.DatabaseConnection;
import com.example.carwashee.dao.ServicoDAO;
import com.example.carwashee.dao.UsuarioDAO;
import com.example.carwashee.model.Plano;
import com.example.carwashee.model.Servico;
import com.example.carwashee.model.TipoServico;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(name = "PrecoServlet", urlPatterns = {"/obterPreco"})
public class PrecoServlet extends HttpServlet {

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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String tipoServico = req.getParameter("tipoServico");
        String plano = req.getParameter("plano");
        try {
            Servico servico = servicoDAO.buscarPrecoPorTipoEPlano(TipoServico.valueOf(tipoServico), Plano.valueOf(plano));
            if (servico != null) {
                resp.setContentType("application/json");
                resp.getWriter().write("{\"preco\": " + servico.getPreco().toPlainString() + "}"); // Use toPlainString() para evitar problemas de notação científica
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
