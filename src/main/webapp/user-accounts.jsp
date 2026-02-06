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

    .custom-modal {
        display: none; /* hidden by default */
        position: fixed;
        z-index: 1000;
        left: 0; top: 0;
        width: 100%; height: 100%;
        background-color: rgba(0,0,0,0.5);
    }

    .custom-modal-content {
        background-color: #fff;
        margin: 10% auto;
        padding: 20px;
        width: 400px;
        border-radius: 5px;
        position: relative;
    }

    .custom-modal .close {
        position: absolute;
        top: 5px; right: 10px;
        font-size: 24px;
        cursor: pointer;
    }

    .btn-update
    {
        background-color: #4CAF50 !important;
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

<c:if test="${empty users}">
    <p>No user accounts found.</p>
</c:if>

    <table class="table" id="userTable">
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
            <a href="javascript:void(0)" onclick="openViewAndEditModal('${user.userId}')">
                View
        </a>
            |
            <a href="javascript:void(0)" onclick="openDeleteModal('${user.userId}')">
                Delete
            </a>
        </td>
        </tr>
    </c:forEach>
    </tbody>
    </table>
            </div>
        </section>
    </main>

</div>

<!-- modals -->
<!-- view & edit modal -->
<div id="viewAndEditModal" class="custom-modal">
    <div class="custom-modal-content">
        <span class="close" onclick="closeModal('viewAndEditModal')">&times;</span>
        <form method="post" action="<c:url value= '/user/update'  />">
            <input type="hidden" name="userId" id="editUserId">
            <label>Username</label>
            <input class="form-control" name="username" id="editUsername" readonly>
            <label>Email</label>
            <input class="form-control" name="email" id="editEmail" readonly>
            <label>Role</label>
            <input class="form-control" name="role" id="editRole" readonly>
            <label>First Name</label>
            <input class="form-control" name="firstName" id="editFirstName">
            <label class="mt-2">Last Name</label>
            <input class="form-control" name="lastName" id="editLastName">
            <label class="mt-2">Contact</label>
            <input class="form-control" name="contactNumber" id="editContact">
            <label class="mt-2">Address</label>
            <input class="form-control" name="address" id="editAddress">
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" onclick="closeModal('viewAndEditModal')">Cancel</button>
                <button type="submit" class="btn btn-warning btn-update">Update</button>
            </div>
        </form>
    </div>
</div>

<!-- delete a user acc modal -->
<div id="deleteModal" class="custom-modal">
    <div class="custom-modal-content">
        <span class="close" onclick="closeModal('deleteModal')">&times;</span>
        <form method="post" action="<c:url value='/user/delete' />">
            <input type="hidden" name="userId" id="deleteUserId">
            <p>Are you sure you want to delete this user?</p>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" onclick="closeModal('deleteModal')">No</button>
                <button type="submit" class="btn btn-danger">Yes</button>
            </div>
        </form>
    </div>
</div>

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

    function openViewAndEditModal(userId) {
        // validate userId
        userId = parseInt(userId);
        if (isNaN(userId) || userId <= 0) {
            alert("Invalid user ID");
            return;
        }

        // this is for form submission
        // document.getElementById("editUserId").value = userId;

        fetch('<c:url value='/user/get?id=' />' + userId)
            .then(res => {
                if (!res.ok) {
                    throw new Error("HTTP error " + res.status);
                }
                return res.json();
            })
            .then(user => {
                document.getElementById('editUserId').value = user.userId;
                document.getElementById('editUsername').value = user.username;
                document.getElementById('editEmail').value = user.email;
                document.getElementById('editRole').value = user.role;
                document.getElementById('editFirstName').value = user.firstName;
                document.getElementById('editLastName').value = user.lastName;
                document.getElementById('editContact').value = user.contactNumber;
                document.getElementById('editAddress').value = user.address;
                console.log("User ID passed :", userId);
                // Show the modal
                document.getElementById('viewAndEditModal').style.display = 'block';
            })
            .catch(err => {
                console.error(err);
                alert("Error fetching user details");
            });
    }

    function openDeleteModal(userId) {
        document.getElementById("deleteUserId").value = userId;
        document.getElementById("deleteModal").style.display = "block";
    }

    function closeModal(modalId) {
        document.getElementById(modalId).style.display = "none";
    }

    // closes modal if user clicks outside it
    window.onclick = function(event) {
        ["viewAndEditModal", "deleteModal"].forEach(id => {
            let modal = document.getElementById(id);
            if(event.target === modal) modal.style.display = "none";
        });
    }

</script>
<!-- bootstrap -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
