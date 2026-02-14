<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page isELIgnored="false"%>
<head>
    <meta charset="UTF-8">
    <title>Ocean View Hotel</title>

    <!-- bootstrap 5 -->
    <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
            rel="stylesheet"
    >

    <style>
        body {
            min-height: 100vh;
            background: linear-gradient(to bottom, #4682A9, #FFFBDE);
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .logo-card {
            background: #f3fdfd;
            padding: 50px 60px;
            border-radius: 18px;
            text-align: center;
            box-shadow: 0 12px 30px rgba(0,0,0,0.2);
        }

        .logo-card img {
            width: 120px;
            margin-bottom: 20px;
        }

        .logo-card h1 {
            font-size: 26px;
            font-weight: 700;
            color: #4682A9;
        }

        .logo-card p {
            font-size: 14px;
            color: #555;
        }

        .enter-btn {
            background: #4682A9 !important;
            margin-top: 20px;
            border-radius: 20px;
            padding: 6px 25px;
            font-size: 14px;
        }
    </style>
</head>
<body>

<div class="logo-card">
    <img src="images/logo.png" alt="Ocean View Hotel Logo">

    <h1>Ocean View Hotel</h1>
    <p><b>Comfort</b> 𓆝 <b>Galle</b>  𓆝<b>Experience</b></p>

    <a class="btn enter-btn" href="<c:url value='/auth/login' />" >
        Enter
    </a>
</div>

</body>
</html>
