<%--
  Created by IntelliJ IDEA.
  User: Sidik
  Date: 9/26/2024
  Time: 8:39 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Produto</title>
    <link rel="stylesheet" href="estilos/produto.css">
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
            <li><a onclick="abrirTelaProdutos()">Produtos</a></li>
            <li><a href="index.jsp">Sair</a></li>
        </ul>
    </nav>

    <script>
        function abrirTelaProdutos() {
            window.location.href = 'produto';
        }
    </script>

    <!-- Main content -->
    <div class="main-content">
        <header>
            <h1>Produtos de Lavagem</h1>
            <button class="add-btn" id="newAppointmentBtn">+ Adicionar produtos</button>
        </header>

        <section class="table-section">
            <table id="produtoTable">
                <thead>
                <tr>
                    <th>Descrição</th>
                    <th>Quantidade</th>
                    <th>Ações</th>
                </tr>
                </thead>
                <tbody id="produto_body">

                </tbody>
            </table>
        </section>
    </div>

    <!-- Modal para Novo Agendamento -->
    <div id="appointmentModal" class="modal">
        <div class="modal-content">
            <span class="close" id="closeModal">&times;</span>
            <h2>Adicionar produto</h2>
            <form id="appointmentForm">
                <label for="descricao">Plano:</label>
                <select id="descricao" name="descricao">
                    <option value="DETERGENTE">Detergente</option>
                    <option value="CERA">Cera</option>
                    <option value="SPARY">Spray de Rodas</option>
                </select>

                <label for="quantidade">Quantidade:</label>
                <input type="number" id="quantidade" name="quantidade" required>

                <button type="submit" id="submitAppointment">Salvar</button>
            </form>
        </div>
    </div>

</div>

<script src="scripts/produto.js"></script>
</body>
</html>
