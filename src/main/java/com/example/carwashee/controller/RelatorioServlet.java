package com.example.carwashee.controller;

import com.example.carwashee.dao.AgendamentoDAO;
import com.example.carwashee.dao.DatabaseConnection;
import com.example.carwashee.dao.UsuarioDAO;
import com.example.carwashee.model.Agendamento;
import com.example.carwashee.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "RelatorioServlet", urlPatterns = {"/telaRelatorio"})
public class RelatorioServlet extends HttpServlet {
    private Connection connection;
    private UsuarioDAO usuarioDAO;
    private AgendamentoDAO agendamentoDAO;
    private double lucros;
    private List<Agendamento> agendamentos;

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
        String periodo = request.getParameter("periodo");
        HttpSession session = request.getSession();
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            if (periodo != null && !periodo.isEmpty()) {
                switch (periodo) {
                    case "SEMANA" -> {
                        agendamentos = agendamentoDAO.obterAgendamentosSemana();
                        lucros = obterLucros(agendamentos);
                    }
                    case "MEZ" -> {
                        agendamentos = agendamentoDAO.obterAgendamentosMez();
                        lucros = obterLucros(agendamentos);
                        ;
                    }
                    case "ANO" -> {
                        agendamentos = agendamentoDAO.obterAgendamentosAno();
                        lucros = obterLucros(agendamentos);
                    }
                    case "" -> {
                        agendamentos = agendamentoDAO.obterAgendamentosDia();
                        lucros = obterLucros(agendamentos);
                    }
                }
            } else {
                agendamentos = new ArrayList<>();
                lucros = 0;
            }

            for (Agendamento agendamento : agendamentos) {
                Usuario cliente = usuarioDAO.buscarUsuarioPorId(agendamento.getUsuarioId());
                agendamento.setUsuarioNome(cliente.getNome());
            }

            request.setAttribute("agendamentos", agendamentos);
            request.setAttribute("lucros", lucros);
            request.getRequestDispatcher("telaRelatorio.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Erro ao buscar agendamentos", e);
        }
    }

    private double obterLucros(List<Agendamento> agendamentos) {
        double resultado = 0;
        for (Agendamento agendamento : agendamentos) {
            if (agendamento.getServicoId() == 1) {
                resultado += 50;
            } else if (agendamento.getServicoId() == 2) {
                resultado += 120;
            } else if (agendamento.getServicoId() == 3) {
                resultado +=200;
            } else if (agendamento.getServicoId() == 4) {
                resultado +=250;
            } else {
                resultado += 150;
            }
        }

        return resultado;
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
