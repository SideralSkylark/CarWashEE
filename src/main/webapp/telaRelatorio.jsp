<%@ page import="com.example.carwashee.model.Agendamento" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: Sidik
  Date: 9/27/2024
  Time: 8:05 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Relatorio</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet" href="estilos/styles.css">
    <style>
      /* Estilos para o modal */
      .modal-overlay {
        display: none;
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background-color: rgba(0, 0, 0, 0.5);
        justify-content: center;
        align-items: center;
      }
      .modal-content {
        background: #fff;
        padding: 20px;
        border-radius: 5px;
        width: 80%;
        max-width: 600px;
        position: relative;
      }
      .modal-close {
        position: absolute;
        top: 10px;
        right: 10px;
        cursor: pointer;
      }
      .modal-footer {
        margin-top: 20px;
        text-align: right;
      }
      .modal-btn {
        cursor: pointer;
      }

      .details-btn {
        padding: 8px 15px;
        background-color: #3498db; /* Azul padrão */
        color: #fff; /* Texto branco */
        border: none; /* Sem borda */
        border-radius: 5px; /* Bordas arredondadas */
        cursor: pointer; /* Mão ao passar o mouse */
        transition: background-color 0.3s, transform 0.2s; /* Transições suaves */
        font-size: 14px; /* Tamanho da fonte */
      }

      .details-btn:hover {
        background-color: #2980b9; /* Azul escuro ao passar o mouse */
        transform: translateY(-2px); /* Leve elevação do botão */
      }
    </style>
</head>
<body>
    <div class="container">
      <!-- Sidebar -->
      <nav class="sidebar">
        <div class="profile">
          <h1 style="margin-bottom: 15px">Dashbord</h1>
          <h2>${usuarioLogado.nome}</h2>
        </div>
        <ul>
          <li><a onclick="abrirTelaGeral()">Geral</a></li>
          <li><a onclick="abrirTelaProdutos()">Produtos</a></li>
          <li><a onclick="abrirTelaRelatorio()">Relatorio</a></li>
          <li><a href="index.jsp">Sair</a></li>
        </ul>
      </nav>

      <script>
        function abrirTelaProdutos() {
          window.location.href = 'produto';
        }

        function abrirTelaGeral() {
          window.location.href = 'EmpresarialServlet'
        }

        function abrirTelaRelatorio() {
          window.location.href = 'telaRelatorio'
        }
      </script>

      <!-- Main content -->
      <div class="main-content">
        <header>
          <h1>Relatorio do Carwash</h1>
        </header>
        <form method="get" action="telaRelatorio">
          <label for="periodo">Filtrar por Periodo:</label>
          <select id="periodo" name="periodo">
            <option value="">Dia</option>
            <option value="SEMANA">Semana</option>
            <option value="MEZ">Mez</option>
            <option value="ANO">Ano</option>
          </select>
          <button type="submit" style="margin-bottom: 20px">Filtrar</button>
        </form>
        <section class="table-section">
          <table id="userTable">
            <thead>
            <tr>
              <th>Cliente</th>
              <th>Data do Agendamento</th>
              <th>Ações</th>
            </tr>
            </thead>
            <tbody>
            <!-- Loop por todos os agendamentos ativos -->
            <%
              double lucros = (double) request.getAttribute("lucros");
              List<Agendamento> agendamentos = (List<Agendamento>) request.getAttribute("agendamentos");
              if (agendamentos != null) {
                for (Agendamento agendamento : agendamentos) {
                  String clienteNome = agendamento.getUsuarioNome();
                  LocalDate dataAgendamento = agendamento.getData();
            %>
            <tr>
              <td><%= clienteNome %></td>
              <td><%= dataAgendamento%></td>
              <td>
                <!-- Botão para ver detalhes do agendamento -->
                <button class="details-btn" data-id="<%= agendamento.getId() %>">Ver Agendamento</button>
              </td>
            </tr>
            <%
              }
            } else {
            %>
            <tr>
              <td colspan="2">Nenhum agendamento encontrado.</td>
            </tr>
            <%
              }
            %>
            </tbody>
          </table>

          <!-- Modal para exibir detalhes -->
          <div id="detailsModal" class="modal-overlay">
            <div class="modal-content">
              <span class="modal-close" id="modalClose">&times;</span>
              <h5>Detalhes do Agendamento</h5>
              <div class="modal-body">
                <!-- Os detalhes do agendamento serão carregados aqui via AJAX -->
              </div>
            </div>
          </div>

          <div style="margin-top: 20px; color: red; font-weight: bold">Rendimento: MZ <%= lucros%></div>
        </section>
      </div>

      <!-- Modal para Novo Agendamento -->
      <div id="appointmentModal" class="modal-overlay">
        <div class="modal-content">
          <span class="modal-close" id="closeModal">&times;</span>
          <h2>Novo Agendamento</h2>
          <form id="appointmentForm" action="createAppointment" method="post">
            <label for="cliente">Cliente:</label>
            <input type="text" id="cliente" name="cliente" required>

            <label for="data">Data:</label>
            <input type="date" id="data" name="data" required>

            <label for="servico">Serviço:</label>
            <select id="servico" name="servico">
              <option value="Lavagem Completa">Lavagem Completa</option>
              <option value="Lavagem Simples">Lavagem Simples</option>
              <option value="Polimento">Polimento</option>
            </select>

            <button type="submit" id="submitAppointment">Agendar</button>
          </form>
        </div>
      </div>
    </div>
    <script>
      document.addEventListener("DOMContentLoaded", function() {
        // Selecionar todos os botões de detalhes
        document.querySelectorAll('.details-btn').forEach(button => {
          button.addEventListener('click', function() {
            const usuarioId = event.currentTarget.dataset.id; // Usando event.currentTarget
            console.log("SIIII  "+usuarioId);
            // Fazer a requisição AJAX para buscar os detalhes do agendamento
            fetch(`detalhesAgendamento?usuarioId=`+usuarioId)
                    .then(response => response.text())
                    .then(html => {
                      // Insere os detalhes retornados no corpo do modal
                      document.querySelector('.modal-body').innerHTML = html;

                      // Exibe o modal
                      document.getElementById('detailsModal').style.display = 'flex';
                    })
                    .catch(err => console.error('Erro ao carregar detalhes:', err));
          });
        });

        // Fechar o modal ao clicar no botão de fechar
        document.getElementById('modalClose').addEventListener('click', function() {
          document.getElementById('detailsModal').style.display = 'none';
        });

        // Fechar o modal ao clicar fora do conteúdo do modal
        window.addEventListener('click', function(event) {
          const modal = document.getElementById('detailsModal');
          if (event.target === modal) {
            modal.style.display = 'none';
          }
        });

        // Mostrar o modal para novo agendamento
        document.getElementById('newAppointmentBtn').addEventListener('click', function() {
          document.getElementById('appointmentModal').style.display = 'flex';
        });

        // Fechar o modal de novo agendamento
        document.getElementById('closeModal').addEventListener('click', function() {
          document.getElementById('appointmentModal').style.display = 'none';
        });
      });


    </script>
</body>
</html>
