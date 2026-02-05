<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <title>Manage Users</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
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
        background: #ffffff;
        border-radius: 10px;
        padding: 25px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
    }

    .content-header h2 {
        margin: 0 0 20px;
        font-weight: 600;
        color: #2c3e50;
    }

    /* search */
    .search-bar {
        margin-bottom: 15px;
    }

    .search-bar input {
        width: 320px;
        padding: 10px 14px;
        border-radius: 6px;
        border: 1px solid #ccc;
    }

    /* table */
    .user-table {
        width: 100%;
        border-collapse: collapse;
    }

    .user-table thead {
        background-color: #4b86a5; /* matches sidebar */
        color: #fff;
    }

    .user-table th,
    .user-table td {
        padding: 12px;
        text-align: left;
    }

    .user-table tbody tr {
        border-bottom: 1px solid #eee;
    }

    .user-table tbody tr:hover {
        background-color: #f1f6fa;
    }

    /* buttons */
    .user-table button {
        padding: 5px 10px;
        border-radius: 5px;
        font-size: 13px;
    }

    .btn-view {
        background-color: #4b86a5;
        color: white;
        border: none;
    }

    .btn-edit {
        background-color: #f0ad4e;
        color: white;
        border: none;
    }

    .btn-delete {
        background-color: #d9534f;
        color: white;
        border: none;
    }

</style>
<body>

<div class="dashboard-layout">

    <jsp:include page="sidebar.jsp"/>

    <main class="dashboard-content">
        <section class="content-card">

            <div class="content-header">
                <h2>Manage User Accounts</h2>
            </div>

            <div class="content-body">

                <div class="search-bar">
                    <input type="text" id="searchInput"
                           placeholder="Search by ID, username or email">
                </div>

<%--<c:if test="${empty users}">--%>
<%--    <p>No user accounts found.</p>--%>
<%--</c:if>--%>

                <c:if test="${not empty users}">
    <table class="table">
    <thead>
    <tr>
    <th>ID</th>
    <th>Username</th>
    <th>Email</th>
    <th>Role</th>
    <th>Actions</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="user" items="${users}">
        <tr>
        <td>${user.userId}</td>
        <td>${user.username}</td>
        <td>${user.email}</td>
        <td>${user.role}</td>
        <td>
        <a href="${pageContext.request.contextPath}/user/view?id=${user.userId}">
        View
        </a>
        |
        <a href="${pageContext.request.contextPath}/user/edit?id=${user.userId}">
        Edit
        </a>
            |
            <a href="${pageContext.request.contextPath}/user/delete?id=${user.userId}"
               onclick="return confirm('Are you sure?');">
                Delete
            </a>
        </td>
        </tr>
    </c:forEach>
    </tbody>
    </table>
</c:if>
            </div>
        </section>
    </main>

</div>


<!-- modals -->
<!-- viewing details of an account-->
<div class="modal fade" id="viewModal">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5>User Details</h5>
                <button class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body" id="viewModalBody"></div>
        </div>
    </div>
</div>

<!-- editing / updating details -->
<div class="modal fade" id="editModal">
    <div class="modal-dialog">
        <form method="post" action="${pageContext.request.contextPath}/users/update" class="modal-content">
            <div class="modal-header">
                <h5>Edit User</h5>
                <button class="btn-close" data-bs-dismiss="modal"></button>
            </div>

            <div class="modal-body">
                <input type="hidden" name="userId" id="editUserId">

                <label>First Name</label>
                <input class="form-control" name="firstName" id="editFirstName">

                <label class="mt-2">Last Name</label>
                <input class="form-control" name="lastName" id="editLastName">

                <label class="mt-2">Contact</label>
                <input class="form-control" name="contactNumber" id="editContact">

                <label class="mt-2">Address</label>
                <input class="form-control" name="address" id="editAddress">
            </div>

            <div class="modal-footer">
                <button class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                <button class="btn btn-warning">Update</button>
            </div>
        </form>
    </div>
</div>

<!-- deelting account -->
<div class="modal fade" id="deleteModal">
    <div class="modal-dialog">
        <form method="post" action="${pageContext.request.contextPath}/users/delete" class="modal-content">
            <div class="modal-header">
                <h5>Confirm Delete</h5>
            </div>

            <div class="modal-body">
                Are you sure you want to delete this user?
                <input type="hidden" name="userId" id="deleteUserId">
            </div>

            <div class="modal-footer">
                <button class="btn btn-secondary" data-bs-dismiss="modal">No</button>
                <button class="btn btn-danger">Yes, Delete</button>
            </div>
        </form>
    </div>
</div>

<!-- bootstrap -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

<script>
    // search bar
    document.getElementById("searchInput").addEventListener("keyup", function () {
        const filter = this.value.toLowerCase();
        const rows = document.querySelectorAll("#userTable tbody tr");

        rows.forEach(row => {
            const text = row.innerText.toLowerCase();
            row.style.display = text.includes(filter) ? "" : "none";
        });
    });

    // These would usually fetch data via AJAX.
    // For now, assume backend preloads data in hidden fields or JS map.

    function openViewModal(userId) {
        document.getElementById("viewModalBody").innerHTML =
            `<p><strong>User ID:</strong> ${userId}</p>
         <p>More details can be loaded here.</p>`;

        new bootstrap.Modal(document.getElementById("viewModal")).show();
    }

    function openEditModal(userId) {
        document.getElementById("editUserId").value = userId;
        new bootstrap.Modal(document.getElementById("editModal")).show();
    }

    function openDeleteModal(userId) {
        document.getElementById("deleteUserId").value = userId;
        new bootstrap.Modal(document.getElementById("deleteModal")).show();
    }

</script>

</body>
</html>
