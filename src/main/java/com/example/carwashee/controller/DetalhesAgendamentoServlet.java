package com.example.carwashee.controller;

import com.example.carwashee.dao.AgendamentoDAO;
import com.example.carwashee.dao.DatabaseConnection;
import com.example.carwashee.dao.ServicoDAO;
import com.example.carwashee.model.Agendamento;
import com.example.carwashee.model.Servico;
import com.example.carwashee.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
@WebServlet("/detalhesAgendamento")
public class DetalhesAgendamentoServlet extends HttpServlet {

    private Connection connection;
    private AgendamentoDAO agendamentoDAO;
    private ServicoDAO servicoDAO;

    @Override
    public void init() throws ServletException {
        try {
            connection = DatabaseConnection.getConnection();
            agendamentoDAO = new AgendamentoDAO(connection);
            servicoDAO = new ServicoDAO(connection);
        } catch (SQLException e) {
            throw new ServletException("Erro ao conectar ao banco de dados", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, IOException {
        String usuarioIdStr = request.getParameter("usuarioId");

        // Verifica se o parâmetro está presente e não é vazio
        if (usuarioIdStr == null || usuarioIdStr.isEmpty()) {
            // Lide com o erro de parâmetro ausente
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "usuarioId não pode ser vazio.");
            return;
        }
        try {
            // Buscar agendamentos ativos pelo ID do usuário
            System.out.println(usuarioIdStr);
               int id = Integer.parseInt(usuarioIdStr);



            Agendamento agendamento = agendamentoDAO.obterAgendamentoPorId(id);

                Servico servico = servicoDAO.obterServicoPorId(agendamento.getServicoId());
                Usuario usuarios =  agendamentoDAO.buscarUsuarioPorId(agendamento.getUsuarioId());
                agendamento.setServico(servico);
                agendamento.setUsuarioNome(usuarios.getNome());


            // Enviar os detalhes dos agendamentos como resposta para o AJAX
            request.setAttribute("agendamento", agendamento);
            request.getRequestDispatcher("detalhesAgendamentos.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}