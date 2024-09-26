<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.carwashee.model.Agendamento" %>
<%@ page import="com.example.carwashee.model.Usuario" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard Carwash</title>
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
                <img src="profile.png" alt="Profile Image">
                <h2>${usuarioLogado.nome}</h2>
            </div>
            <ul>
               <li><a href="logout.jsp">Sair</a></li>
            </ul>
        </nav>

        <!-- Main content -->
        <div class="main-content">
            <header>
                <h1>Agendamentos de Carwash</h1>
                <button class="add-btn" id="newAppointmentBtn">+ Novo Agendamento</button>
            </header>
<form method="get" action="EmpresarialServlet">
    <label for="status">Filtrar por Status:</label>
    <select id="status" name="status">
        <option value="">Todos</option>
        <option value="PENDENTE">Pendente</option>
        <option value="CONFIRMADO">Confirmado</option>
        <option value="CANCELADO">Cancelado</option>
    </select>
    <button type="submit">Filtrar</button>
</form>
            <section class="table-section">
                <table id="userTable">
                    <thead>
                        <tr>
                            <th>Cliente</th>
                            <th>Ações</th>
                        </tr>
                    </thead>
                    <tbody>
                        <!-- Loop por todos os agendamentos ativos -->
                        <%
                            List<Agendamento> agendamentos = (List<Agendamento>) request.getAttribute("agendamentos");
                            if (agendamentos != null) {
                                for (Agendamento agendamento : agendamentos) {
                                    String clienteNome = agendamento.getUsuarioNome();
                        %>
                                    <tr>
                                        <td><%= clienteNome %></td>
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

                <!-- Paginação -->
                <div class="pagination">
                    <button>Anterior</button>
                    <button>Próximo</button>
                </div>
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