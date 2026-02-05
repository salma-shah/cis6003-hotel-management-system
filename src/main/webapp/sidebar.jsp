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

     /*modal css */
    .modal {
        display: none;
        position: fixed;
        z-index: 1000;
        left: 0;
        top: 0;
        width: 100%;
        height: 100%;
        overflow: auto;
        background-color: rgba(0,0,0,0.5);
    }
    .modal-content {
        background-color: #fff;
        margin: 15% auto;
        padding: 20px;
        width: 300px;
        border-radius: 8px;
        text-align: center;
    }
    .close {
        float: right;
        font-size: 20px;
        cursor: pointer;
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

        <a href="<c:url value='/help' />">
            <i class="bi bi-question-square me-2"></i> Help
        </a>

<%--        this is a section only manager can view--%>
<%--        <c:if test="${sessionScope.userRole == 'manager'}">--%>
        <a href="<c:url value='/user/register' />">
            <i class="bi bi-person-circle"></i>    Create New Account
        </a>
        <a href="<c:url value='/user/all' />">
            <i class="bi bi-gear"></i> Manage User Accounts
        </a>

        <a class="nav-link" href="reports.jsp">
            <i class="bi bi-journal-check"></i>    Generate Reports
        </a>
<%--        </c:if>--%>
    </nav>

    <button class="logout-btn">
        Logout
    </button>

<%--    we will use a pop up modal--%>
    <div id="logoutModal" class="modal">
        <div class="modal-content">
            <span class="close">&times;</span>
            <p>Are you sure you want to logout?</p>
            <button id="confirmLogout">Yes</button>
            <button id="cancelLogout">No</button>
        </div>
    </div>
</div>

<script>
    const logoutBtn = document.getElementById('logout-btn');
    const confirmLogoutBtn = document.getElementById('confirmLogoutBtn');
    const cancelLogoutBtn = document.getElementById('cancelLogoutBtn');
    const modal = document.getElementById('logoutModal');
    const closeBtn = modal.querySelector('.close');

    // open modal
    logoutBtn.addEventListener('click', () => {
        modal.style.display = 'block';
    });

    // close modal
    closeBtn.addEventListener('click', () => {
        modal.style.display = 'none';});

    cancelLogoutBtn.addEventListener('click', () => {
        modal.style.display = 'none';
    });

    // confirm logout
    confirmLogoutBtn.addEventListener('click', () => {
        window.location.href = '/auth/logout'; // redirect to logout servlet
    });

    // close modal if clicked outside content
    window.addEventListener('click', (e) => {
        if (e.target === modal) {
            modal.style.display = 'none';
        }
    });

</script>

