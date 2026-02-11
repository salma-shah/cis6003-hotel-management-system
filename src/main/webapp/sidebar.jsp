<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page isELIgnored="false"%>
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
        margin: 12px 0;
        display: flex;
        align-items: center;
        gap: 12px;
        font-size: 15px;
    }

    .sidebar i {
        width: 20px;
        text-align: center;
        font-size: 18px;
    }

    .sidebar a:hover {
        text-decoration: underline;
        background: rgba(255,255,255,0.12);
        border-radius: 6px;
        padding-left: 6px;
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
    .custom-modal {
        display: none;
        position: fixed;
        inset: 0;
        background: rgba(0, 0, 0, 0.55);
        z-index: 1000;
        align-items: center;
        justify-content: center;
    }

    /* modal box */
    .custom-modal-content {
        background: #ffffff;
        width: 360px;
        max-width: 90%;
        border-radius: 8px;
        padding: 24px;
        position: relative;
    }

    /* close icon */
    .custom-modal .close {
        position: absolute;
        top: 12px;
        right: 14px;
        font-size: 20px;
        cursor: pointer;
        color: #666;
    }

    /* text */
    .custom-modal-content p {
        margin: 10px 0 20px;
        font-size: 15px;
        color: #333;
        text-align: center;
    }

    /* footer */
    .modal-footer {
        display: flex;
        justify-content: flex-end;
        gap: 10px;
    }

    /* buttons */
    #confirmLogout {
        background-color: #dc3545;
        color: #fff;
        border: none;
        padding: 8px 16px;
        border-radius: 5px;
        cursor: pointer;
    }

    #confirmLogout:hover {
        background-color: #bb2d3b;
    }

    #cancelLogout {
        background-color: #e0e0e0;
        color: #333;
        border: none;
        padding: 8px 16px;
        border-radius: 5px;
        cursor: pointer;
    }

</style>

<div class="sidebar p-3">

    <h4 class="mb-4">Dashboard</h4>

    <nav class="nav flex-column sidebar-nav">

        <a class="nav-link" href="reservations.jsp">
            <i class="bi bi-book"></i> Reservations
        </a>

        <a class="nav-link" href="<c:url value='/room/all'/>">
            <i class="bi bi-house-door"></i>  Rooms
        </a>

    <%--        this is a section only manager can view--%>
       <c:if test="${sessionScope.userRole == 'Manager'}">
        <a class="nav-link" href="<c:url value='/user/register' />">
            <i class="bi bi-person-circle"></i>    Create New Account
        </a>
        <a class="nav-link" href="<c:url value='/user/all' />">
            <i class="bi bi-gear"></i> Manage User Accounts
        </a>
           <a class="nav-link" href="reports.jsp">
               <i class="bi bi-journal-check"></i>    Generate Reports
           </a>
       </c:if>

        <a class="nav-link" href="<c:url value='/help' />">
            <i class="bi bi-question-square"></i>  Help
        </a>
    </nav>

    <button class="logout-btn" onclick="openLogoutModal()">
        Logout
    </button>

<%--    we will use a pop up modal--%>
    <div id="logoutModal" class="custom-modal">
            <div class="custom-modal-content">
            <span class="close" onclick="closeLogoutModal()">&times;</span>
            <p>Are you sure you want to logout?</p>
                <div class="modal-footer">
            <button id="confirmLogout" class="btn btn-danger">Yes</button>
            <button id="cancelLogout">No</button>
        </div>
            </div>
    </div>
</div>

<script>
    function openLogoutModal() {
        document.getElementById('logoutModal').style.display = 'flex';
    }

    function closeLogoutModal() {
        document.getElementById('logoutModal').style.display = 'none';
    }

    document.getElementById('confirmLogout').onclick = () => {
        window.location.href = '<c:url value='/auth/logout' />' ;
    };

    document.getElementById('cancelLogout').onclick = closeLogoutModal;

    window.onclick = (e) => {
        const modal = document.getElementById('logoutModal');
        if (e.target === modal) {
            closeLogoutModal();
        }
    };


</script>

