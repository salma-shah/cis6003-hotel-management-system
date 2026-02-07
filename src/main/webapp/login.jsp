<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Ocean View Hotel - Login</title>

    <!-- bootstrap CSS -->
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">

    <!-- bootstrap Icons -->
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">

    <!-- creating custom css for rounded corners and edges-->
    <style>
        body {
            min-height: 100vh;
            background: linear-gradient(to bottom, #4682A9, #FFFBDE);
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .login-card {
            width: 380px;
            border-radius: 16px;
            overflow: hidden;
            box-shadow: 0 10px 25px rgba(0,0,0,0.2);
        }

        .login-header {
            background: white;
            padding: 30px 20px;
            text-align: center;
        }

        .login-header h1 {
            font-size: 22px;
            margin-bottom: 5px;
            color: #4682A9;
        }

        .login-header h2 {
            font-size: 24px;
            font-weight: 700;
            color: #4682A9;
        }

        .login-body {
            background: #4682A9;
            padding: 30px;
            color: white;
        }

        .login-body label {
            font-size: 16px;
            margin-bottom: 6px;
        }

        .login-body input {
            border-radius: 20px;
            padding: 8px 15px;
        }

        .login-btn {
            margin-top: 20px;
            border-radius: 20px;
            padding: 6px 25px;
            font-size: 14px;
        }

        .form-error {
            margin-top: 12px;
            padding: 10px 14px;
            border-radius: 6px;
            background-color: #fdecea;
            color: #b42318;
            border: 1px solid #f5c2c7;
            font-size: 0.95rem;
            font-weight: 500;
        }

        .form-error i {
            margin-right: 6px;
        }

    </style>
</head>
<body>

<div class="login-card">

    <!-- heading part of form -->
    <div class="login-header">
        <h1>Welcome to</h1>
        <h2>Ocean View Hotel</h2>
    </div>

    <!-- form body -->
    <div class="login-body">
        <form action="<c:url value='/auth/login' />" method="post" id="loginForm">
        <div class="mb-3">
                <label>Username</label>
                <input type="text" class="form-control" id="username" name="username" required>
            </div>

            <div class="mb-3">
                <label>Password</label>
                <div style="position: relative;">
                    <input type="password" class="form-control" id="password" name="password" required>
                    <button type="button" id="togglePassword" style="position: absolute; right: 10px; top: 50%; transform: translateY(-50%); background: none; border: none; cursor: pointer;">
                        <i class="bi bi-eye" id="eyeIcon"></i>
                    </button>
                </div>
            </div>

            <div class="text-center">
                <button type="submit" class="btn btn-light login-btn" >
                    Login
                </button>
            </div>
        </form>

<%-- ensuring input is valid  --%>

<c:if test="${not empty param.error}">
    <c:choose>
        <c:when test="${param.error == 'empty_fields'}">
            <p class="form-error"> Please provide both username and password.</p>
        </c:when>
    </c:choose>
    <c:choose>
        <c:when test="${param.error == 'invalid_credentials'}">
            <p class="form-error"> You have entered an invalid username or password.</p>
        </c:when>
    </c:choose>
<%--    <c:choose>--%>
<%--        <c:when test="${param.error == 'user_not_found'}">--%>
<%--            <p class="form-error"> User could not be found.</p>--%>
<%--        </c:when>--%>
<%--    </c:choose>--%>
    <c:choose>
        <c:when test="${param.error == 'system_error'}">
            <p class="form-error"> Something went wrong. We apologize.</p>
        </c:when>
    </c:choose>
    <c:otherwise>
        <p class="form-error">Login failed. Please try again.</p>
    </c:otherwise>
</c:if>
    </div>
</div>

<%--if user enters password, they may toggle the eye icon and choose between viewing the password text or not--%>
<%--this controls showing/not showing the text --%>
<%--by toggling the eye icon--%>
<script>
        const passwordInput = document.getElementById('password');
        const togglePassword = document.getElementById('togglePassword');
        const eyeIcon = document.getElementById('eyeIcon');

        togglePassword.addEventListener('click', function () {

        if (passwordInput.type === 'password')
        {
            passwordInput.type = 'text';
            eyeIcon.classList.remove('bi-eye');
            eyeIcon.classList.add('bi-eye-slash');
        } else {
            passwordInput.type = 'password';
            eyeIcon.classList.remove('bi-eye-slash');
            eyeIcon.classList.add('bi-eye');
        }
        });
</script>
</body>
</html>
