package com.example.carwashee.controller;

import com.example.carwashee.dao.DatabaseConnection;
import com.example.carwashee.dao.UsuarioDAO;
import com.example.carwashee.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(name = "CadastroServlet", urlPatterns = {"/cadastro"})
public class CadastroServlet extends HttpServlet {
    private Connection connection;
    private UsuarioDAO usuarioDAO;

    private String nome, email, password;

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
        if (action.equals("/cadastro")) {
            try {
                cadastro(request, response);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void cadastro(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, SQLException {
        nome = request.getParameter("nome");
        email = request.getParameter("email");
        password = request.getParameter("password");

        if (camposPrenchidos()) {
            Usuario usuario = criarUsuario(nome, email, password);
            usuarioDAO.adicionarUsuario(usuario);

            response.sendRedirect("index.jsp");
        } else {
            request.setAttribute("mensagemErro", "Todos os campos são obrigatórios.");
            request.getRequestDispatcher("cadastro.jsp").forward(request, response);
            return;
        }
    }

    private Usuario criarUsuario(String nome, String email, String password) {
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(password);
        usuario.setTipoUsuario("SINGULAR");

        return usuario;
    }

    private boolean camposPrenchidos() {
        if (nome == null || nome.isEmpty() || email == null || email.isEmpty() || password == null || password.isEmpty()) {
            return false;
        } else {
            return true;
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
