<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <title>Reservation Management</title>
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
            <div class="header-row">
                <h2>Reservations</h2>
                <a href="<c:url value='/reservation/create' />" class="btn enter-btn">
                    + Make Reservation
                </a>
            </div>

            <div class="search-filter">
                <input type="text" id="regSearchInput" placeholder="Search by reservation number...">
                <select id="statusFilter">
                    <option value="">All Status</option>
                    <option value="Pending">Pending</option>
                    <option value="Confirmed">Confirmed</option>
                    <option value="Cancelled">Cancelled</option>
                    <option value="CheckedIn">Checked-In</option>
                    <option value="CheckedOut">Checked-Out</option>
                </select>
            </div>

            <%--    // this is if there are no search results--%>
            <p id="noGuestsMessage" style="display:none;">No guests found.</p>

            <div class="table-responsive">
                <table class="table table-hover" id="guestTable">
                    <thead>
                    <tr>
                        <th>Reservation Num</th>
                        <th>Room Type</th>
                        <th>Start Date</th>
                        <th>End Date</th>
                        <th>Status</th>
                        <th>Payment Status</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="guest" items="${guests}">
                        <tr data-name="${reservation.reservationNum} ${guest.lastName}" data-email="${guest.email}">
                            <td>${guest.registrationNumber}</td>
                            <td>${guest.firstName} ${guest.lastName}</td>
                            <td>${guest.email}</td>
                            <td>${guest.contactNumber}</td>
                            <td>
                                <a href="javascript:void(0)" onclick="openViewAndEditModal('${reservation.id}')">
                                    View
                                </a>
                                |
                                <a href="javascript:void(0)" onclick="openHistoryModal('${reservation.id}')">
                                    Update
                                </a>

                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </main>


<!-- modals -->
<!-- view & edit modal -->
<div id="viewAndEditModal" class="custom-modal modal-dialog-scrollable">
    <div class="custom-modal-content">
        <span class="close" onclick="closeModal('viewAndEditModal')">&times;</span>
        <form method="post" action="<c:url value= '/reservation/update'  />">
            <input type="hidden" name="resId" id="resId">
            <label>Reservation Number</label>
            <input class="form-control" name="resNum" id="resNum" readonly>
            <label>Room Type</label>
            <input class="form-control" name="firstName" id="editFirstName" readonly>
            <label class="mt-2">Room Number</label>
            <input class="form-control" name="lastName" id="editLastName" readonly>
            <label>Floor Num</label>
            <input class="form-control" name="email" id="email" readonly>
            <label class="mt-2">Guest IDt</label>
            <input class="form-control" name="contactNumber" id="editContact" readonly>
            <label class="mt-2">Guest Name</label>
            <input class="form-control" name="nic" id="nic" readonly>
            <label class="mt-2">Check-in Date</label>
            <input class="form-control" name="passportNumber" id="editPassportNum" readonly>
            <label class="mt-2">Check-out Date</label>
            <input class="form-control" name="nationality" id="editNationality">
            <label class="mt-2">Date of Reservation</label>
            <input class="form-control" name="address" id="editAddress" readonly>
            <label class="mt-2">Payment Status</label>
            <input class="form-control" name="dob" id="dob" readonly>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" onclick="closeModal('viewAndEditModal')">Cancel</button>
                <button type="submit" class="btn btn-warning btn-update">Update</button>
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
                document.getElementById('editNationality').value = guest.nationality ?? 'No Nationality provided';
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
