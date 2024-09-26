package com.example.carwashee.controller;

import com.example.carwashee.dao.DatabaseConnection;
import com.example.carwashee.dao.UsuarioDAO;
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

@WebServlet(name = "logInServlet", urlPatterns = {"/login"})
public class LogInServlet extends HttpServlet {

    private Connection connection;
    private UsuarioDAO usuarioDAO;
    private Usuario usuario;

    @Override
    public void init() {
        try {
            connection = DatabaseConnection.getConnection();
            usuarioDAO = new UsuarioDAO(connection);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados", e);
        }
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String action = request.getServletPath();
        if (action.equals("/login")) {
            try {
                logIn(request, response);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void logIn(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        usuario = usuarioDAO.buscarUsuarioPorEmailESenha(email, password);
        if (usuario != null) {
            HttpSession session = request.getSession();
            session.setAttribute("usuarioLogado", usuario);

            if (usuario.getTipoUsuario().equals("EMPRESARIAL")) {
                response.sendRedirect("EmpresarialServlet");
            } else {
                response.sendRedirect("cliente");
            }
        } else {
            // Login falhou, redireciona de volta para a página de login com uma mensagem de erro
            request.setAttribute("erroLogin", "Email ou senha incorretos.");
            request.getRequestDispatcher("index.jsp").forward(request, response);
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
