package com.example.carwashee.controller;

import com.example.carwashee.dao.AgendamentoDAO;
import com.example.carwashee.dao.DatabaseConnection;
import com.example.carwashee.dao.ServicoDAO;
import com.example.carwashee.model.Agendamento;
import com.example.carwashee.model.Servico;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletResponseWrapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(name = "ImprimirReciboServlet", urlPatterns = {"/imprimirRecibo"})
public class ImprimirReciboServlet extends HttpServlet {
    private Connection connection;
    private AgendamentoDAO agendamentoDAO;
    private ServicoDAO servicoDAO;

    @Override
    public void init() {
        try {
            connection = DatabaseConnection.getConnection();
            agendamentoDAO = new AgendamentoDAO(connection);
            servicoDAO = new ServicoDAO(connection);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inicializar a conexão com o banco de dados", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int agendamentoId = Integer.parseInt(request.getParameter("agendamentoId"));

        // Busca o agendamento e o serviço associado
        Agendamento agendamento;
        Servico servico;
        try {
            agendamento = agendamentoDAO.obterAgendamentoPorId(agendamentoId);
            servico = servicoDAO.obterServicoPorId(agendamento.getServicoId());
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar o agendamento ou serviço", e);
        }

        // Passa os dados para o JSP e gera o conteúdo em HTML
        request.setAttribute("agendamento", agendamento);
        request.setAttribute("servico", servico);
        String htmlContent = renderJspToHtml(request, response, "recibo.jsp");

        // Configura o response para saída de PDF
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=recibo.pdf");

        // Gera o PDF usando Flying Saucer
        try (OutputStream os = response.getOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(os);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar o PDF", e);
        }
    }

    // Função auxiliar para renderizar o JSP como HTML
    private String renderJspToHtml(HttpServletRequest request, HttpServletResponse response, String jspUrl) throws ServletException, IOException {
        StringWriter stringWriter = new StringWriter();
        HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper(response) {
            @Override
            public ServletOutputStream getOutputStream() throws IOException {
                throw new IllegalStateException("getOutputStream() já foi chamado para esta resposta.");
            }

            @Override
            public PrintWriter getWriter() {
                return new PrintWriter(stringWriter);
            }
        };
        request.getRequestDispatcher(jspUrl).include(request, responseWrapper);
        return stringWriter.toString();
    }
}
