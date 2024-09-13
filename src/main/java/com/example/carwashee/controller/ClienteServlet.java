package com.example.carwashee.controller;

import com.example.carwashee.dao.AgendamentoDAO;
import com.example.carwashee.dao.DatabaseConnection;
import com.example.carwashee.dao.UsuarioDAO;
import com.example.carwashee.model.Agendamento;
import com.example.carwashee.model.Usuario;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@WebServlet(name = "ClienteServlet", urlPatterns = {"/cliente", "/editarAgendamento", "/deletarAgendamento", "/adicionarAgendamento", "/obterAgendamentosAtivos"})
public class ClienteServlet extends HttpServlet {

    private Connection connection;
    private UsuarioDAO usuarioDAO;
    private AgendamentoDAO agendamentoDAO;
    private Usuario usuario;

    // Adapter para LocalDate
    public class LocalDateAdapter extends com.google.gson.TypeAdapter<LocalDate> {
        @Override
        public void write(com.google.gson.stream.JsonWriter jsonWriter, LocalDate localDate) throws IOException {
            jsonWriter.value(localDate.toString()); // Converte LocalDate para String
        }

        @Override
        public LocalDate read(com.google.gson.stream.JsonReader jsonReader) throws IOException {
            return LocalDate.parse(jsonReader.nextString()); // Converte String para LocalDate
        }
    }

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
        HttpSession session = request.getSession();
        usuario = (Usuario) session.getAttribute("usuarioLogado");

        String action = request.getServletPath();
        if (action.equals("/cliente")) {
            if (usuario == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            // Atribui os atributos para a requisição
            request.setAttribute("nomeUsuario", usuario.getNome());
            request.setAttribute("email", usuario.getEmail());
            request.setAttribute("idUsuario", usuario.getId());

            abrirTelaCliente(request, response);
        } else if (action.equals("/obterAgendamentosAtivos")) {


            List<Agendamento> agendamentos = null;
            try {
                System.out.println(usuario.getId());
                agendamentos = agendamentoDAO.buscarAgendamentosComServicoPorUsuarioId(usuario.getId());
                if (agendamentos.isEmpty()) {
                    System.out.println("Nenhum agendamento encontrado para o usuário: " + usuario.getId());
                } else {
                    System.out.println("Agendamentos encontrados: " + agendamentos.size());
                    for (Agendamento agendamento : agendamentos) {
                        System.out.println(agendamento.toString());
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }
            // Cria uma instância de Gson com o adaptador para LocalDate
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .create();

            // Serializa a lista de agendamentos para JSON
            String jsonResponse = gson.toJson(agendamentos);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonResponse);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getServletPath();
        HttpSession session = request.getSession();
        usuario = (Usuario) session.getAttribute("usuarioLogado");
        System.out.println("Ação POST capturada: " + action);

        if (action.equals("/adicionarAgendamento")) {
            String descricao = request.getParameter("descricao");
            LocalDate data = LocalDate.parse(request.getParameter("data"));
            String plano = request.getParameter("plano");
            String servico = request.getParameter("servico");
            double preco = Double.parseDouble(request.getParameter("preco"));

            System.out.println("Descrição: " + descricao);
            System.out.println("Data: " + data);
            System.out.println("Plano: " + plano);
            System.out.println("Serviço: " + servico);
            System.out.println("Preço (antes da conversão): " + request.getParameter("preco"));

            Agendamento agendamento = new Agendamento(usuario.getId(), mapearServico_id(plano, servico), descricao, data);

            try {
                agendamentoDAO.adicionarAgendamento(agendamento);
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else if (action.equals("/editarAgendamento")) {
            int id = Integer.parseInt(request.getParameter("id"));
            String descricao = request.getParameter("descricao");
            LocalDate data = LocalDate.parse(request.getParameter("data"));
            String plano = request.getParameter("plano");
            String servico = request.getParameter("servico");

            Agendamento agendamento = new Agendamento(descricao, data, mapearServico_id(plano, servico), id);

            try {
                agendamentoDAO.editarAgendamentoCliente(agendamento);
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else if (action.equals("/deletarAgendamento")) {
            int id = Integer.parseInt(request.getParameter("id"));
            try {
                agendamentoDAO.removerAgendamento(id);
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
    }

    private int mapearServico_id(String plano, String servico) {
        if (plano.equals("LIGEIRO") && servico.equals("LAVAGEM_SECO")) {
            return 1;
        } else if (plano.equals("LIGEIRO") && servico.equals("LIMPEZA_COMPLETA")) {
            return 2;
        } else if (plano.equals("LIGEIRO") && servico.equals("OUTRO")) {
            return 5;
        } else if (plano.equals("PESADO") && servico.equals("POLIMENTO")) {
            return 3;
        } else if (plano.equals("PESADO") && servico.equals("LIMPEZA_COMPLETA")) {
            return 4;
        } else {
            return 0;
        }
    }

    private void abrirTelaCliente(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("cliente.jsp").forward(request, response);
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
