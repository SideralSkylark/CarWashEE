<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
                <li><a href="#">Dashboard</a></li>
                <li><a href="#">Relatórios</a></li>
                <li><a href="#">Clientes</a></li>
                <li><a href="logout.jsp">Sair</a></li>
            </ul>
        </nav>

        <!-- Main content -->
        <div class="main-content">
            <header>
                <h1>Agendamentos de Carwash</h1>
                <button class="add-btn" id="newAppointmentBtn">+ Novo Agendamento</button>
            </header>

            <section class="table-section">
                <table id="userTable">
                    <thead>
                        <tr>
                            <th>Cliente</th>
                            <th>Ações</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>${usuarioLogado.nome}</td>
                            <td>
                                <!-- Passar o ID do usuário no botão -->
                                <button class="details-btn" data-id="${usuarioLogado.id}">Ver Agendamentos</button>
                            </td>
                        </tr>
                    </tbody>
                </table>

                <div class="modal-overlay" id="detailsModal">
                    <div class="modal-content">
                        <span class="modal-close" id="modalClose">&times;</span>
                        <h5 class="modal-title">Detalhes do Agendamento</h5>
                        <div class="modal-body">
                            <!-- Aqui os detalhes serão carregados via AJAX -->
                            <p>Carregando detalhes...</p>
                        </div>
                        <div class="modal-footer">
                            <button class="modal-btn" id="modalCloseBtn">Fechar</button>
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
        <div id="appointmentModal" class="modal">
            <div class="modal-content">
                <span class="close" id="closeModal">&times;</span>
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
           const detailsBtns = document.querySelectorAll('.details-btn');
           const modalOverlay = document.getElementById('detailsModal');
           const modalClose = document.getElementById('modalClose');
           const modalCloseBtn = document.getElementById('modalCloseBtn');

           detailsBtns.forEach(btn => {
               btn.addEventListener('click', function() {
                   const usuarioId = this.getAttribute('data-id');

                   fetch(`EmpresarialServlet?usuarioId=${usuarioId}&action=detalhes`)
                       .then(response => response.text())
                       .then(html => {
                           document.querySelector('.modal-body').innerHTML = html;
                           modalOverlay.style.display = 'flex';
                       })
                       .catch(err => console.error('Erro ao carregar detalhes:', err));
               });
           });

           modalClose.addEventListener('click', function() {
               modalOverlay.style.display = 'none';
           });

           modalCloseBtn.addEventListener('click', function() {
               modalOverlay.style.display = 'none';
           });

           window.addEventListener('click', function(event) {
               if (event.target === modalOverlay) {
                   modalOverlay.style.display = 'none';
               }
           });
       });
   </script>


</body>
</html>