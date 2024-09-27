<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.carwashee.model.Agendamento" %>
<%@ page import="com.example.carwashee.model.StatusAgendamento" %>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <title>Detalhes do Agendamento</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }
        .agendamento {
            border: 1px solid #ccc;
            border-radius: 8px;
            padding: 15px;
            margin-bottom: 15px;
            background-color: #f9f9f9;
        }
        .agendamento h3 {
            margin: 0 0 10px 0;
            color: #333;
        }
        .agendamento p {
            margin: 5px 0;
        }
        .divider {
            border-top: 1px solid #ddd;
            margin: 15px 0;
        }
        .no-agendamentos {
            color: red;
            font-weight: bold;
        }
    </style>
</head>
<body>
    <div id="detalhesAgendamentos">
        <%
            Agendamento agendamento = (Agendamento) request.getAttribute("agendamento");
            if (agendamento != null) {
        %>
                    <div class="agendamento">
                        <h3>Nome do cliente: <%= agendamento.getUsuarioNome() %></h3>
                        <p><strong>Descrição:</strong> <%= agendamento.getServico().getDescricao() %></p>
                        <p><strong>Tipo de Serviço:</strong> <%= agendamento.getServico().getTipoServico() %></p>
                        <p><strong>Plano:</strong> <%= agendamento.getServico().getPlano() %></p>
                        <p><strong>Data:</strong> <%= agendamento.getData() %></p>
                        <p><strong>Preço:</strong> MZ <%= agendamento.getServico().getPreco() %></p>

                        <!-- Verifica o status do agendamento -->
                        <%
                            StatusAgendamento status = agendamento.getStatus();
                            switch (status) {
                                case PENDENTE:
                        %>
                                    <button class="modal-btn"
                                            data-idS="<%= agendamento.getId() %>"
                                            onclick="(function(agendamentoId) {
                                                console.log('Agendamento ID: <%= agendamento.getId() %>');
                                                fetch('marcarConcluido?agendamentoId=<%= agendamento.getId() %>', {
                                                    method: 'POST'
                                                })
                                                .then(response => response.json())
                                                .then(data => {
                                                    if (data.success) {
                                                        alert('Agendamento marcado como confirmado.');
                                                        location.reload(); // Recarrega a página
                                                    } else {
                                                        alert('Erro: ' + data.message);
                                                    }
                                                })
                                                .catch(err => {
                                                    console.error('Erro ao marcar agendamento:', err);
                                                    alert('Erro ao marcar o agendamento.');
                                                });
                                            })(this.dataset.idS)">
                                        Confirmar Agendamento
                                    </button>
                        <%
                                    break;

                                case CONFIRMADO:
                        %>
                                    <button class="modal-btn"
                                            data-idS="<%= agendamento.getId() %>"
                                            onclick="(function(agendamentoId) {
                                                console.log('Gerando recibo para o agendamento ID: <%= agendamento.getId() %>');
                                                fetch('imprimirRecibo?agendamentoId=<%= agendamento.getId() %>', {
                                                    method: 'GET'
                                                })
                                                .then(response => {
                                                    if (response.ok) {
                                                        return response.blob();
                                                    } else {
                                                        throw new Error('Erro ao gerar o recibo');
                                                    }
                                                })
                                                .then(blob => {
                                                    const url = window.URL.createObjectURL(blob);
                                                    const a = document.createElement('a');
                                                    a.href = url;
                                                    a.download = 'recibo.pdf'; // Nome do arquivo que será baixado
                                                    document.body.appendChild(a);
                                                    a.click();
                                                    a.remove();
                                                    window.URL.revokeObjectURL(url); // Libera o objeto URL
                                                })
                                                .catch(err => {
                                                    console.error('Erro ao imprimir recibo:', err);
                                                    alert('Erro ao imprimir o recibo.');
                                                });
                                            })(this.dataset.idS)" style="margin-top: 20px">
                                        Imprimir Recibo
                                    </button>
                        <%
                                    break;

                                case CANCELADO:
                        %>
                                    <p class="no-agendamentos">Agendamento Cancelado.</p>
                        <%
                                    break;
                            }
                        %>
                    </div>

                    <div class="divider"></div>
        <%
            } else {
        %>
                <p class="no-agendamentos">Nenhum agendamento encontrado.</p>
        <%
            }
        %>
    </div>
</body>
</html>