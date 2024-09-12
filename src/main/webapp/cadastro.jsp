<%--
  Created by IntelliJ IDEA.
  User: Sidik
  Date: 9/12/2024
  Time: 2:33 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Cadastro</title>
  <link rel="stylesheet" href="estilos/login.css">
</head>
<body>
<div class="login-container">
  <form action="cadastro" method="POST" class="login-form">
    <h2>Sign Up</h2>

    <div class="input-field">
      <input type="text" id="nome" name="nome" placeholder="Nome" required>
    </div>

    <div class="input-field">
      <input type="email" id="email" name="email" placeholder="Email" required>
    </div>

    <div class="input-field">
      <input type="password" id="password" name="password" placeholder="Password" required>
    </div>

    <div class="input-field">
      <input type="password" id="confirm-password" name="confirm-password" placeholder="Confirme a Password" required>
    </div>

    <button type="submit" class="btn-signin">Sign Up</button>

    <div class="signup-link">
      <p>Ja tem uma conta? <a href="/login.html">Sign In</a></p>
    </div>
  </form>
</div>
</body>
</html>
