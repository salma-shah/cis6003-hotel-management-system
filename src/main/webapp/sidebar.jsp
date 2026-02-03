<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<head>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css"/>
</head>
<style>
    body {
        margin: 0;
        overflow-x: hidden;
    }

    .sidebar {
        height: 100vh;
        background-color: #4682A9;
        color: white;
        padding: 30px 20px;
        position: fixed;
        width: 240px;
        display: flex;
        flex-direction: column;
    }

    .sidebar h4 {
        font-weight: 600;
        margin-bottom: 40px;
    }

    .sidebar a {
        color: white;
        text-decoration: none;
        margin: 15px 0;
        display: block;
    }

    .sidebar a:hover {
        text-decoration: underline;
    }

    .logout-btn {
        margin-top: auto;
        background: #e6e6e6;
        border: none;
        padding: 8px 20px;
        border-radius: 20px;
        width: fit-content;
    }
</style>

<div class="sidebar p-3">

    <h4 class="mb-4">Dashboard</h4>

    <nav class="nav flex-column sidebar-nav">

        <a class="nav-link" href="reservations.jsp">
            <i class="bi bi-book me-2"></i> Reservations
        </a>

        <a class="nav-link" href="rooms.jsp">
            <i class="bi bi-house-door"></i>    Rooms
        </a>

        <a class="nav-link" href="help.jsp">
            <i class="bi bi-question-square me-2"></i> Help
        </a>

<%--        this is a section only manager can view--%>
<%--        <c:if test="${sessionScope.userRole == 'manager'}">--%>
        <a class="nav-link" href="register.jsp">
            <i class="bi bi-person-circle"></i>    Create New Account
        </a>

        <a class="nav-link" href="register.jsp">
            <i class="bi bi-gear"></i>   Manager User Accounts
        </a>

        <a class="nav-link" href="register.jsp">
            <i class="bi bi-journal-check"></i>    Generate Reports
        </a>
<%--        </c:if>--%>
    </nav>

    <button class="logout-btn">
        Logout
    </button>

</div>

