package com.example.carwashee.controller;

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
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@WebServlet(name = "ProdutoServlet", urlPatterns = {"/produto"})
public class ProdutoServlet extends HttpServlet {

    private Usuario usuario;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        usuario = (Usuario) session.getAttribute("usuarioLogado");

        String action = request.getServletPath();
        if (action.equals("/produto")) {
            if (usuario == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            // Atribui os atributos para a requisição
            request.setAttribute("nomeUsuario", usuario.getNome());
            request.setAttribute("email", usuario.getEmail());
            request.setAttribute("idUsuario", usuario.getId());

            abrirTelaProdutos(request, response);
        }
    }

    private void abrirTelaProdutos(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("produto.jsp").forward(request, response);
    }
}
