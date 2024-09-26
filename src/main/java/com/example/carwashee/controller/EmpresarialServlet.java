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
        String action = request.getParameter("action");
        String status = request.getParameter("status"); // Parâmetro para o status
        HttpSession session = request.getSession();
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            List<Agendamento> agendamentos;

            if (status != null && !status.isEmpty()) {
                // Busca agendamentos filtrados por status
                agendamentos = agendamentoDAO.buscarAgendamentosPorStatus(status);
            } else {
                // Busca todos os agendamentos ativos
                agendamentos = agendamentoDAO.buscarTodosAgendamentos();
            }

            for (Agendamento agendamento : agendamentos) {
                Usuario cliente = usuarioDAO.buscarUsuarioPorId(agendamento.getUsuarioId());
                agendamento.setUsuarioNome(cliente.getNome());
            }

            request.setAttribute("agendamentos", agendamentos);
            request.getRequestDispatcher("empresarial.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Erro ao buscar agendamentos", e);
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
