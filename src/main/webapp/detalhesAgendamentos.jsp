<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <title>Detalhes do Agendamento</title>
</head>
<body>
    <c:forEach var="agendamento" items="${agendamentos}">
    <p>Nome do usuario: ${usuarioLogado.nome}</p><br>
            <p>Descricao: ${agendamento.getServico().getDescricao()}</p><br>
                <p>Tipo de Servico: ${agendamento.getServico().getTipoServico()}</p><br>
                <p>Tipo de Servico: ${agendamento.getServico().getPlano()}</p><br>
        <p>Data: ${agendamento.data}</p><br>
                <p>Tipo de Servico: ${agendamento.getServico().getPreco()}</p>




        <hr>
    </c:forEach>
</body>
</html>
