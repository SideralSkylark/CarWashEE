<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>LogIn</title>
  <link rel="stylesheet" href="estilos/login.css">
</head>
<body>
<div class="login-container">
  <form action="login" method="POST" class="login-form">
    <h2>Login</h2>

    <div class="input-field">
      <input type="email" id="email" name="email" placeholder="Email" required>
    </div>

    <div class="input-field">
      <input type="password" id="password" name="password" placeholder="Password" required>
    </div>

    <div class="options">
      <label class="remember-me">
        <input type="checkbox" name="remember"> Lembre-se de mim
      </label>
      <a href="#" class="forgot-password">Esqueceu sua senha</a>
    </div>

    <button type="submit" class="btn-signin">Log in</button>

    <div class="signup-link">
      <p>Nao tem uma conta? <a href="signup.html">Cadastro</a></p>
    </div>
  </form>
</div>
</body>
</html>