<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Guest Management</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        .dashboard-layout {
            display: flex;
            height: 100vh;
            background-color: #f5f7fa;
        }

        .dashboard-content {
            flex: 1;
            padding: 30px;
            overflow-y: auto;
            margin-left: 250px;
        }

        .content-card {
            background: #fff;
            border-radius: 10px;
            padding: 25px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.05);
        }

        .header-row {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }

        .search-filter {
            display: flex;
            gap: 15px;
            margin-bottom: 15px;
        }

        .search-filter input, .search-filter select {
            padding: 8px 12px;
            border-radius: 6px;
            border: 1px solid #ccc;
            font-size: 14px;
        }

        table {
            width: 100%;
        }

        th, td {
            padding: 12px 10px;
            text-align: left;
            font-size: 14px;
        }

        th {
            background-color: #f8f9fa;
        }

        .status-checkedin {
            color: green;
            font-weight: 600;
        }

        .status-checkedout {
            color: red;
            font-weight: 600;
        }

        .btn-action {
            padding: 4px 10px;
            font-size: 13px;
            border-radius: 6px;
            text-decoration: none;
        }

        .btn-view {
            background-color: #4682A9;
            color: white;
        }

        .btn-edit {
            background-color: #FFC107;
            color: white;
        }
    </style>
</head>

<body>
<div class="dashboard-layout">
    <jsp:include page="sidebar.jsp"/>

    <main class="dashboard-content">
        <div class="content-card">
            <div class="header-row">

                <%--        this is a section only manager can view--%>
                <c:if test="${sessionScope.userRole == 'Manager'}">
                    <div class="header-row">
                        <h2>Guest Profiles</h2>
                        <a href="<c:url value='/guest/register' />" class="btn enter-btn">
                            + Register Guest
                        </a>
                    </div>
                </c:if>

            <div class="search-filter">
                <input type="text" id="searchInput" placeholder="Search by name or email...">
                <select id="statusFilter">
                    <option value="">All Status</option>
                    <option value="Checked-in">Checked-in</option>
                    <option value="Checked-out">Checked-out</option>
                </select>
            </div>

            <div class="table-responsive">
                <table class="table table-hover" id="guestTable">
                    <thead>
                    <tr>
                        <th>Reg. Number</th>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Contact</th>
                        <th>Address</th>
                        <th>DOB</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="guest" items="${guests}">
                        <tr data-status="${guest.status}" data-name="${guest.firstName} ${guest.lastName}" data-email="${guest.email}">
                            <td>${guest.registrationNumber}</td>
                            <td>${guest.firstName} ${guest.lastName}</td>
                            <td>${guest.email}</td>
                            <td>${guest.contactNumber}</td>
                            <td>${guest.address}</td>
                            <td><fmt:formatDate value="${guest.dob}" pattern="yyyy-MM-dd"/></td>
                            <td class="${guest.status == 'Checked-in' ? 'status-checkedin' : 'status-checkedout'}">
                                    ${guest.status}
                            </td>
                            <td>
                                <a href="javascript:void(0)" onclick="openViewAndEditModal('${guest.guestId}')">
                                    View
                                </a>
                                |
                                <a href="javascript:void(0)" onclick="openDeleteModal('${guest.guestId}')">
                                    Edit
                                </a>
                                |
                                <a href="javascript:void(0)" onclick="openDeleteModal('${guest.guestId}')">
                                    Delete
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </main>
</div>

<script>
    document.addEventListener("DOMContentLoaded", () => {
        const searchInput = document.getElementById("searchInput");
        const statusFilter = document.getElementById("statusFilter");
        const table = document.getElementById("guestTable").getElementsByTagName("tbody")[0];

        function filterTable() {
            const searchText = searchInput.value.toLowerCase();
            const statusText = statusFilter.value;

            Array.from(table.rows).forEach(row => {
                const name = row.getAttribute("data-name").toLowerCase();
                const email = row.getAttribute("data-email").toLowerCase();
                const status = row.getAttribute("data-status");

                const matchesSearch = name.includes(searchText) || email.includes(searchText);
                const matchesStatus = !statusText || status === statusText;

                row.style.display = (matchesSearch && matchesStatus) ? "" : "none";
            });
        }

        searchInput.addEventListener("input", filterTable);
        statusFilter.addEventListener("change", filterTable);
    });
</script>

</body>
</html>
