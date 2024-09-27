package com.example.carwashee.controller;

import com.example.carwashee.dao.AgendamentoDAO;
import com.example.carwashee.dao.DatabaseConnection;
import com.example.carwashee.model.ProdutoGasto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/marcarConcluido")
public class ActualizarAgendamentoServlet extends HttpServlet {

    private AgendamentoDAO agendamentoDAO;
    private Connection connection;

    @Override
    public void init() {
        try {
            connection = DatabaseConnection.getConnection();
            agendamentoDAO = new AgendamentoDAO(connection);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados", e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String agendamentoId = request.getParameter("agendamentoId");
        Gson gson = new Gson();
        ResponseData responseData = new ResponseData();

        System.out.println("Agendamento ID recebido: " + agendamentoId);

        try {
            boolean atualizado = agendamentoDAO.atualizarStatusAgendamento(Integer.parseInt(agendamentoId), "CONFIRMADO");
            if (atualizado) {

                int idServico = agendamentoDAO.obterAgendamentoPorId(Integer.parseInt(agendamentoId)).getServicoId();

                // Cria uma instância de ProdutoGasto e chama o método concluirAgendamento
                ProdutoGasto produtoGasto = new ProdutoGasto(idServico);
                produtoGasto.concluirAgendamento();

                responseData.success = true;
                responseData.message = "Agendamento confirmado e produtos deduzidos com sucesso."; // Mensagem de sucesso
            } else {
                responseData.success = false;
                responseData.message = "Não foi possível atualizar o status."; // Mensagem de falha
            }
        } catch (SQLException e) {
            responseData.success = false;
            responseData.message = "Erro ao acessar o banco de dados: " + e.getMessage(); // Mensagem de erro detalhada
            e.printStackTrace();
        } catch (NumberFormatException e) {
            responseData.success = false;
            responseData.message = "ID de agendamento inválido."; // Mensagem para ID inválido
        }

        // Configura o tipo de resposta como JSON
        response.setContentType("application/json");
        String jsonResponse = gson.toJson(responseData);
        System.out.println("Resposta JSON: " + jsonResponse); // Log da resposta JSON
        response.getWriter().write(jsonResponse);
    }

    private class ResponseData {
        boolean success;
        String message;
    }
}