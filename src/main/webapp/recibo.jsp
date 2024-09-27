<%@ page import="com.example.carwashee.model.Agendamento" %>
<%@ page import="com.example.carwashee.model.Servico" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8"></meta>
    <title>Recibo de Agendamento</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }
        h1 {
            color: #2c3e50;
        }
        .detalhes {
            margin-top: 30px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
        }
        th, td {
            padding: 10px;
            border: 1px solid #ddd;
            text-align: left;
        }
        th {
            background-color: #2c3e50;
            color: white;
        }
    </style>
</head>
<body>
<%
    Agendamento agendamento = (Agendamento) request.getAttribute("agendamento");
    Servico servico = (Servico) request.getAttribute("servico");
%>

<h1>Recibo da Lavagem</h1>


<p style="margin-top: 30px">Data do Agendamento: <%= agendamento.getData() %></p>
<p>Status: <%= agendamento.getStatus() %></p>

<div class="detalhes">
    <h2>Detalhes do Serviço</h2>
    <table>
        <tr>
            <th>Descrição</th>
            <th>Tipo de Serviço</th>
            <th>Plano</th>
            <th>Preço</th>
        </tr>
        <tr>
            <td><%= servico.getDescricao() %></td>
            <td><%= servico.getTipoServico() %></td>
            <td><%= servico.getPlano() %></td>
            <td>MZ <%= servico.getPreco() %></td>
        </tr>
    </table>
</div>

<p>Recibo gerado por computador</p>

</body>
</html>
