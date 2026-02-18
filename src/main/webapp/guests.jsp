<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <title>Guest Management</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        .dashboard-layout {
            display: flex;
            min-height: 100vh;
            background: #f4f6f9;
        }

        .dashboard-content {
            flex: 1;
            padding: 40px;
            margin-left: 250px;
        }

        .content-card {
            background: #fff;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 8px 24px rgba(0,0,0,0.05);
        }

        .header-row {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 25px;
        }

        .enter-btn {
            background: #4682A9 !important;
            margin-top: 20px;
            border-radius: 20px;
            padding: 6px 25px;
            font-size: 14px;
            color: white;
        }

        .search-filter {
            display: flex;
            gap: 15px;
            margin-bottom: 20px;
        }

        .search-filter input,
        .search-filter select {
            padding: 10px;
            border-radius: 8px;
            border: 1px solid #ddd;
        }

        .table th {
            background: #f1f3f5;
        }

        .status-checkedin {
            color: #198754;
            font-weight: 600;
        }

        .status-checkedout {
            color: #dc3545;
            font-weight: 600;
        }

        .header-row h2 {
            margin: 0 0 20px;
            font-weight: 600;
            color: #2c3e50;
        }

        /* modals */
        .custom-modal {
            display: none;
            position: fixed;
            z-index: 1000;
            left: 0; top: 0;
            width: 100%; height: 100%;
            background-color: rgba(0,0,0,0.5);
        }

        .custom-modal-content {
            background-color: #fff;
            margin: 5% auto;
            padding: 20px;
            width: 425px;
            border-radius: 5px;
            position: relative;
            max-height: 80vh;
            overflow-y: auto;
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
</head>

<body>
<div class="dashboard-layout">
    <jsp:include page="sidebar.jsp"/>

    <main class="dashboard-content">
        <div class="content-card">

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
                <input type="text" id="regSearchInput" placeholder="Search by registration number...">
                <select id="statusFilter">
                    <option value="">All Status</option>
                    <option value="Checked-In">Checked-In</option>
                    <option value="Checked-Out">Checked-Out</option>
                </select>
                <input type="text" id="nicOrPPSearchInput" placeholder="Search by NIC or Passport number...">
            </div>

                    <%--    // this is if there are no search results--%>
                    <p id="noGuestsMessage" style="display:none;">No guests found.</p>

            <div class="table-responsive">
                <table class="table table-hover" id="guestTable">
                    <thead>
                    <tr>
                        <th>Registration Num</th>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Contact</th>
<%--                        <th>Status</th>--%>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="guest" items="${guests}">
                        <tr data-name="${guest.firstName} ${guest.lastName}" data-email="${guest.email}">
                            <td>${guest.registrationNumber}</td>
                            <td>${guest.firstName} ${guest.lastName}</td>
                            <td>${guest.email}</td>
                            <td>${guest.contactNumber}</td>
                            <td>
                                <a href="javascript:void(0)" onclick="openViewAndEditModal('${guest.id}')">
                                    View
                                </a>
                                |
                                <a href="javascript:void(0)" onclick="openHistoryModal('${guest.id}')">
                                    History
                                </a>
                                |
                                <a href="javascript:void(0)" onclick="openDeleteModal('${guest.id}')">
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


<!-- modals -->
<!-- view & edit modal -->
<div id="viewAndEditModal" class="custom-modal modal-dialog-scrollable">
    <div class="custom-modal-content">
        <span class="close" onclick="closeModal('viewAndEditModal')">&times;</span>
        <form method="post" action="<c:url value= '/guest/update'  />">
            <input type="hidden" name="guestId" id="guestId">
            <label>Registration Number</label>
            <input class="form-control" name="regNum" id="RegNum" readonly>
            <label>First Name</label>
            <input class="form-control" name="firstName" id="editFirstName">
            <label class="mt-2">Last Name</label>
            <input class="form-control" name="lastName" id="editLastName">
            <label>Email</label>
            <input class="form-control" name="email" id="email" readonly>
            <label class="mt-2">Contact</label>
            <input class="form-control" name="contactNumber" id="editContact">
            <label class="mt-2">NIC</label>
            <input class="form-control" name="nic" id="nic" readonly>
            <label class="mt-2">Passport Number</label>
            <input class="form-control" name="passportNumber" id="editPassportNum">
            <label class="mt-2">Address</label>
            <input class="form-control" name="address" id="editAddress">
            <label class="mt-2">Date of Birth</label>
            <input class="form-control" name="dob" id="dob" readonly>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" onclick="closeModal('viewAndEditModal')">Cancel</button>
                <button type="submit" class="btn btn-warning btn-update">Update</button>
            </div>
        </form>
    </div>
</div>

<!-- delete a guest modal -->
<div id="deleteModal" class="custom-modal">
    <div class="custom-modal-content">
        <span class="close" onclick="closeModal('deleteModal')">&times;</span>
        <form method="post" action="<c:url value='/guest/delete' />">
            <input type="hidden" name="guestId" id="deleteGuestId">
            <p>Are you sure you want to delete this guest's profile? This action cannot be undone.</p>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" onclick="closeModal('deleteModal')">No</button>
                <button type="submit" class="btn btn-danger">Yes</button>
            </div>
        </form>
    </div>
</div>


<script>
    document.addEventListener("DOMContentLoaded", () => {
        // search and filter fields
        const nicOrPPSearchInput = document.getElementById("nicOrPPSearchInput");
        const regSearchInput = document.getElementById("regSearchInput");
        const statusFilter = document.getElementById("statusFilter");

        // guest profile tables
        const tableBody = document.getElementById("guestTable").getElementsByTagName("tbody")[0];

        function performSearch(){
            const nicOrPPSearchText = nicOrPPSearchInput.value.toLowerCase();
            const regSearchText = regSearchInput.value;
            const statusText = statusFilter.value;
            const rows = document.querySelectorAll("#guestTable tbody tr");
            const tableBody = document.querySelector("#guestTable tbody");
            tableBody.innerHTML = "";

            // if (!nicOrPPSearchText.trim() && !regSearchText.trim() && !statusText) {
            //     rows.forEach(row => {
            //         row.style.display = "";
            //     })
            //     return;
            // }

            const params = new URLSearchParams();
            if (nicOrPPSearchText) params.append("nicOrPPSearchText", nicOrPPSearchText);
            if (regSearchText) params.append("regSearchText", regSearchText);
            if (statusText) params.append("statusText", statusText);

            // sending the request to get all but filtered guests
            fetch('<c:url value="/guest/all" />?' + params.toString(),
                {
                    headers: {
                        "X-Requested-With": "XMLHttpRequest"
                    }
                })
                .then(res => {
                    if (!res.ok) throw new Error("Search failed");
                    return res.json();
                })
                .then(guests => {
                    tableBody.innerHTML = "";
                    console.log(guests);
                    rows.forEach((row) => {
                        row.style.display = "none";
                    });

                    if (!guests || guests.length === 0) {
                        tableBody.innerHTML = `<tr><td colspan="5">No guests found</td></tr>`;
                        return; // if no guests, no guests profile  msg displayed
                    }

                    guests.forEach(guest => {
                        console.log(guest);
                        const tr = document.createElement("tr");
                    tr.innerHTML =
                        "<td>" + guest.registrationNumber + "</td>" +
                        "<td>" + guest.firstName + "</td>" +
                        "<td>" + guest.lastName + "</td>" +
                        "<td>" + guest.email + "</td>" +
                        "<td>" +
                        "<a href='#' onclick=\"openViewAndEditModal('" + guest.id + "')\">View</a> | " +
                        "<a href='#' onclick=\"openDeleteModal('" + guest.id + "')\">Delete</a>" +
                        "</td>";

                        tableBody.appendChild(tr);
                        console.log(tableBody.children.length);
                        tableBody.style.display = "table-row-group";
                    });
                })
                .catch(err => console.error(err));
        }

        nicOrPPSearchInput.addEventListener("input", performSearch);
        regSearchInput.addEventListener("input", performSearch);
        statusFilter.addEventListener("change", performSearch);
    });

    // view and edit modal
    function openViewAndEditModal(id){
        guestId = parseInt(id);
        if (isNaN(parseInt(guestId)) || guestId <=0) {
            alert("Invalid guest ID.")
            return;
        }

        // this gets the guest's personal info
        fetch('<c:url value="/guest/get?id=" />' + id)
            .then(res => {
                if (!res.ok) {
                    throw new Error("HTTP error " + res.status);
                }
                return res.json();
            })
            .then(guest => {
                document.getElementById('guestId').value = guest.id;
                document.getElementById('RegNum').value = guest.registrationNumber;
                document.getElementById('editFirstName').value = guest.firstName ?? 'No first name provided';
                document.getElementById('editLastName').value = guest.lastName ?? 'No last name provided';
                document.getElementById('editContact').value = guest.contactNumber ?? 'No contact number provided';
                document.getElementById('email').value = guest.email ?? 'No email provided';
                document.getElementById('nic').value = guest.nic ?? 'No NIC Provided.';
                document.getElementById('editPassportNum').value = guest.passportNumber  ?? 'No Passport provided';
                document.getElementById('editAddress').value = guest.address ?? 'No Address provided';
                document.getElementById('dob').value = guest.dob ?? 'No Date of Birth Provided';
                console.log("Guest ID passed :", guest.id);
                // Show the modal
                document.getElementById('viewAndEditModal').style.display = 'block';
            })
            .catch(err => {
                console.error(err);
                alert("Error fetching guest details");
            });
    }

    // opening delete modal
    function openDeleteModal(guestId) {
        document.getElementById("deleteGuestId").value = guestId;
        document.getElementById("deleteModal").style.display = "block";
    }

    // history modal
    function openHistoryModal(guestId){}

    // closing all modals
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

</body>
</html>
