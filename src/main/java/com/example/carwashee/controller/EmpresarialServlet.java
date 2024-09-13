package com.example.carwashee.controller;

import com.example.carwashee.dao.AgendamentoDAO;
import com.example.carwashee.dao.DatabaseConnection;
import com.example.carwashee.dao.ServicoDAO;
import com.example.carwashee.dao.UsuarioDAO;
import com.example.carwashee.model.Agendamento;
import com.example.carwashee.model.Servico;
import com.example.carwashee.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "EmpresarialServlet", urlPatterns = {"/EmpresarialServlet"})
public class EmpresarialServlet extends HttpServlet {

    private Connection connection;
    private UsuarioDAO usuarioDAO;
    private AgendamentoDAO agendamentoDAO;

    @Override
    public void init() {
        try {
            connection = DatabaseConnection.getConnection();
            usuarioDAO = new UsuarioDAO(connection);
            agendamentoDAO = new AgendamentoDAO(connection);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String usuarioIdParam = request.getParameter("usuarioId");
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        if ("detalhes".equals(action) && usuarioLogado != null) {
            try {
                int usuarioId = usuarioLogado.getId(); // Corrigido para parse do ID
                List<Agendamento> agendamentos = agendamentoDAO.buscarAgendamentosComServicoPorUsuarioId(usuarioId);

                // Encaminha para o JSP que gera o conteúdo do modal
                request.setAttribute("agendamentos", agendamentos);
                request.getRequestDispatcher("detalhesAgendamentos.jsp").forward(request, response);
            } catch (SQLException e) {
                throw new ServletException("Erro ao buscar agendamentos", e);
            }
        } else {
            request.getRequestDispatcher("empresarial.jsp").forward(request, response);
        }
    }

    @Override
    public void destroy() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
