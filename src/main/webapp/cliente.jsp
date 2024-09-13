<%--
  Created by IntelliJ IDEA.
  User: Sidik
  Date: 9/12/2024
  Time: 3:07 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Cliente</title>
    <link rel="stylesheet" href="estilos/cliente.css">
</head>
<body>
<div class="container">
    <!-- Sidebar -->
    <nav class="sidebar">
        <div class="profile">
            <h2>Bem vindo, ${nomeUsuario}</h2>
            <h3>${email}</h3>
            <input type="hidden" id="idUsuario" value="${sessionScope.idUsuario}">
        </div>
        <ul>
            <li><a href="index.jsp">Sair</a></li>
        </ul>
    </nav>

    <!-- Main content -->
    <div class="main-content">
        <header>
            <h1>Agendamentos de Carwash</h1>
            <button class="add-btn" id="newAppointmentBtn">+ Novo Agendamento</button>
        </header>

        <section class="table-section">
            <table id="appointmentTable">
                <thead>
                <tr>
                    <th>Descrição</th>
                    <th>Data</th>
                    <th>Tipo de Serviço</th>
                    <th>Plano</th>
                    <th>Preço</th>
                    <th>Ações</th>
                </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
        </section>
    </div>

    <!-- Modal para Novo Agendamento -->
    <div id="appointmentModal" class="modal">
        <div class="modal-content">
            <span class="close" id="closeModal">&times;</span>
            <h2>Novo Agendamento</h2>
            <form id="appointmentForm">
                <label for="descricao">Descrição:</label>
                <input type="text" id="descricao" name="descricao" required>

                <label for="data">Data:</label>
                <input type="date" id="data" name="data" required>

                <label for="plano">Plano:</label>
                <select id="plano" name="plano">
                    <option value="LIGEIRO">Ligeiro</option>
                    <option value="PESADO">Pesado</option>
                </select>

                <label for="servico">Tipo de Serviço:</label>
                <select id="servico" name="servico" disabled>
                    <option value="">Selecione um serviço</option>
                </select>


                <label for="preco">Preço:</label>
                <input type="text" id="preco" name="preco" readonly>

                <button type="submit" id="submitAppointment">Agendar</button>
            </form>
        </div>
    </div>

</div>

<script src="scripts/cliente.js"></script>
</body>
</html>
