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
            top: 0; left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0,0,0,0.5);
            z-index: 1000;
            overflow-y: auto;
        }

        .custom-modal-content {
            background: #fff;
            margin: 5% auto;
            padding: 30px;
            width: 90%;
            border-radius: 12px;
            position: relative;
            display: flex;               /* make columns */
            flex-wrap: wrap;             /* responsive on smaller screens */
            gap: 20px;                   /* spacing between columns */
            box-shadow: 0 8px 24px rgba(0,0,0,0.1);
        }

        /* individual columns */
        .modal-column {
            flex: 1 1 30%;
            min-width: 250px;
        }

        .modal-column h5 {
            font-weight: 600;
            color: #2c3e50;
            margin-bottom: 15px;
        }

        .modal-column table {
            width: 100%;
            border-collapse: collapse;
        }

        .modal-column th {
            text-align: left;
            background-color: #f1f3f5;
            padding: 8px;
            font-weight: 600;
        }

        .modal-column td {
            padding: 8px;
        }

        .custom-modal .close {
            position: absolute;
            top: 10px; right: 15px;
            font-size: 28px;
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
                <input type="text" id="resSearchInput" placeholder="Search by reservation number...">
                <select id="statusFilter">
                    <option value="">All Status</option>
                    <option value="Pending">Pending</option>
                    <option value="Confirmed">Confirmed</option>
                    <option value="Cancelled">Cancelled</option>
                    <option value="CheckedIn">Checked-In</option>
                    <option value="CheckedOut">Checked-Out</option>
                </select>

                <div class="form-group">
                    <label>Check-In Date</label>
                    <input type="date" name="checkInDate" id="checkInDate" required>
                    <span class="error-message" id="checkInError"></span>
                </div>
                <br>
                <div class="form-group">
                    <label>Check-Out Date</label>
                    <input type="date" name="checkOutDate" id="checkOutDate" required>
                    <span class="error-message" id="checkOutError"></span>
                </div>

            </div>

            <%--    // this is if there are no search results--%>
            <p id="noResMsg" style="display:none;">No reservations found.</p>

            <div class="table-responsive">
                <table class="table table-hover" id="guestTable">
                    <thead>
                    <tr>
                        <th>Reservation Num</th>
<%--                        <th>Room Type</th>--%>
                        <th>Start Date</th>
                        <th>End Date</th>
                        <th>Status</th>
<%--                        <th>Payment Status</th>--%>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="reservation" items="${reservations}">
                        <tr data-name="${reservation.id}">
                            <td>${reservation.reservationNumber}</td>
<%--                            <td>${reservation.roomId}</td>--%>
                            <td>${reservation.checkInDate}</td>
                            <td>${reservation.checkOutDate}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${reservation.status == 'CheckedIn'}">
                                        <span class="status-checkedin">${reservation.status}</span>
                                    </c:when>
                                    <c:when test="${reservation.status == 'CheckedOut'}">
                                        <span class="status-checkedout">${reservation.status}</span>
                                    </c:when>
                                    <c:otherwise>${reservation.status}</c:otherwise>
                                </c:choose>
                            </td>
                            <td class="d-flex flex-wrap gap-1">
                                <button class ="btn btn-sm btn-primary flex-fill" onclick="openReservationModal('${reservation.id}')">
                                    View
                                </button>
                                <c:choose>
                                <c:when test="${reservation.status == 'Confirmed'}">
                                <button id="checkInBtn" value="checkin" class ="btn btn-sm btn-success flex-fill" onclick="changeResStatus('${reservation.id}', 'CheckedIn')">
                                    Check-In
                                </button>
                                <button id="cancelBtn" value="cancel" class ="btn btn-sm btn-danger flex-fill" onclick="changeResStatus('${reservation.id}', 'Cancelled')">
                                    Cancel
                                </button>
                                </c:when>
                                <c:when test="${reservation.status == 'CheckedIn'}">
                                <button id="checkOutBtn" value="checkout" class ="btn btn-sm btn-warning flex-fill" onclick="changeResStatus('${reservation.id}', 'CheckedOut')">
                                    Check-Out
                                </button>
                                </c:when>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </main>


<!-- modals -->
        <div id="reservationModal" class="custom-modal">
            <div class="custom-modal-content">
                <span class="close" onclick="closeModal()">&times;</span>

                <div class="modal-column">
                    <h5>Reservation</h5>
                    <div class="form-group">
                        <label>Reservation Number</label>
                        <input type="text" id="reservationNumber" readonly class="form-control">
                    </div>
                    <div class="form-group">
                        <label>Guest ID</label>
                        <input type="text" id="guestId" readonly class="form-control">
                    </div>
                    <div class="form-group">
                        <label>Room Type</label>
                        <input type="text" id="roomType" readonly class="form-control">
                    </div>
                    <div class="form-group">
                        <label>Room Number</label>
                        <input type="text" id="roomNum" readonly class="form-control">
                    </div>
                    <div class="form-group">
                        <label>Room ID</label>
                        <input type="text" id="roomId" readonly class="form-control">
                    </div>
                    <div class="form-group">
                        <label>Status</label>
                        <input type="text" id="resStatus" readonly class="form-control">
                    </div>
                    <div class="form-group">
                        <label>Total Stay Cost</label>
                        <input type="text" id="stayCost" readonly class="form-control">
                    </div>
                    <div class="form-group">
                        <label>Check In</label>
                        <input type="text" id="modalCheckInDate" readonly class="form-control">
                    </div>
                    <div class="form-group">
                        <label>Check Out</label>
                        <input type="text" id="modalCheckOutDate" readonly class="form-control">
                    </div>
                    <div class="form-group">
                        <label>Number of Adults</label>
                        <input type="text" id="numAdults" readonly class="form-control">
                    </div>
                    <div class="form-group">
                        <label>Number of Children</label>
                        <input type="text" id="numChildren" readonly class="form-control">
                    </div>
                    <div class="form-group">
                        <label>Date of Reservation</label>
                        <input type="text" id="dateOfRes" readonly class="form-control">
                    </div>
                </div>

                <div class="modal-column">
                    <h5>Guest</h5>
                    <div class="form-group">
                        <label>Registration Number</label>
                        <input type="text" id="guestReg" readonly class="form-control">
                    </div>
                    <div class="form-group">
                        <label>Name</label>
                        <input type="text" id="name" readonly class="form-control">
                    </div>
                    <div class="form-group">
                        <label>Email</label>
                        <input type="text" id="email" readonly class="form-control">
                    </div>
                    <div class="form-group">
                        <label>Contact</label>
                        <input type="text" id="contactNumber" readonly class="form-control">
                    </div>
                    <div class="form-group">
                        <label>NIC</label>
                        <input type="text" id="nic" readonly class="form-control">
                    </div>
                    <div class="form-group">
                        <label>Passport Number</label>
                        <input type="text" id="passportNum" readonly class="form-control">
                    </div>
                    <div class="form-group">
                        <label>Address</label>
                        <input type="text" id="address" readonly class="form-control">
                    </div>
                </div>

                <div class="modal-column">
                    <h5>Bill</h5>
                    <div class="form-group">
                        <label>Bill ID</label>
                        <input type="text" id="billId" readonly class="form-control">
                    </div>
                    <div class="form-group">
                        <label>Stay Cost</label>
                        <input type="text" id="billStayCost" readonly class="form-control">
                    </div>
                    <div class="form-group">
                        <label>Tax</label>
                        <input type="text" id="tax" readonly class="form-control">
                    </div>
                    <div class="form-group">
                        <label>Discount</label>
                        <input type="text" id="discount" readonly class="form-control">
                    </div>
                    <div class="form-group">
                        <label>Amount</label>
                        <input type="text" id="totalBillAmount" readonly class="form-control">
                    </div>
                    <div class="form-group">
                        <label>Status</label>
                        <input type="text" id="billStatus" readonly class="form-control">
                    </div>
                </div>

                <div class="modal-column">
                    <h5>Payment</h5>
                    <div class="form-group">
                        <label>Total Payment</label>
                        <input type="text" id="totalPaymentAmount" readonly class="form-control">
                    </div>
                    <div class="form-group">
                        <label>Method</label>
                        <input type="text" id="paymentMethod" readonly class="form-control">
                    </div>
                    <div class="form-group">
                        <label>Paid Date</label>
                        <input type="text" id="paymentDate" readonly class="form-control">
                    </div>
                    <button id="makePayment" type="submit" class="btn enter-btn" hidden="hidden" onClick="goToMakePaymentPage()">
                        Make Payment
                    </button>
                </div>
            </div>
        </div>

<script>
    document.addEventListener("DOMContentLoaded", () => {

        // search and filter fields
        const resSearchInput = document.getElementById("resSearchInput");
        const statusInput = document.getElementById("statusFilter");
        const checkInDate = document.getElementById("checkInDate");
        const checkOutDate = document.getElementById("checkOutDate");

        function performSearch(){
            const regSearchText = resSearchInput.value;
            const statusText = statusInput.value;
            const rows = document.querySelectorAll("#guestTable tbody tr");
            const tableBody = document.querySelector("#guestTable tbody");
            tableBody.innerHTML = "";

            const params = new URLSearchParams();
            if (resSearchInput) params.append("resSearchInput", regSearchText);
            if (statusInput) params.append("statusInput", statusText);
            if (checkInDate.value) params.append("checkInDate", checkInDate.value);
            if (checkOutDate.value) params.append("checkOutDate", checkOutDate.value);

            // sending the request to get all but filtered res
            fetch('<c:url value="/reservation/all" />?' + params.toString(),
                {
                    headers: {
                        "X-Requested-With": "XMLHttpRequest"
                    }
                })
                .then(res => {
                    if (!res.ok) throw new Error("Search failed");
                    return res.json();
                })
                .then(reservations => {
                    tableBody.innerHTML = "";
                    console.log(reservations);
                    rows.forEach((row) => {
                        row.style.display = "none";
                    });

                    if (!reservations || reservations.length === 0) {
                        tableBody.innerHTML = `<tr><td colspan="5">No reservations found</td></tr>`;
                        return; // if no guests, no guests profile  msg displayed
                    }

                    reservations.forEach(reservation => {
                        console.log(reservation);
                        const tr = document.createElement("tr");

                        const statusClass =
                            reservation.status === "CheckedIn" ? "<span class='status-checkedin'>Checked In</span>" :
                                reservation.status === "CheckedOut"  ? "<span class='status-checkedout'>Checked Out</span>" :
                                    reservation.status;

                        let btns = "<button class='btn btn-sm btn-primary flex-fill' " +
                            "onclick=\"openReservationModal('" + reservation.id + "')\">" +
                            "View</button>";

                        // add btns based on the res status
                        if (reservation.status === "CheckedIn") {
                            btns += "<button class='btn btn-sm btn-warning flex-fill' " +
                                "onclick=\"changeResStatus('" + reservation.id + "', 'CheckedOut')\">" +
                                "Check Out</button>";
                        }
                        else if (reservation.status === "Confirmed") {
                            btns += "<button class='btn btn-sm btn-success flex-fill' " +
                                "onclick=\"changeResStatus('" + reservation.id + "','CheckedIn')\">" +
                                "Check In</button>" +

                                "<button class='btn btn-sm btn-danger flex-fill' " +
                                "onclick=\"changeResStatus('" + reservation.id + "', 'Cancelled')\">" +
                                "Cancel</button>";
                        }

                        tr.innerHTML =
                            "<td>" + reservation.reservationNumber + "</td>" +
                            "<td>" + reservation.checkInDate + "</td>" +
                            "<td>" + reservation.checkOutDate + "</td>" +
                            "<td>" + statusClass + "</td>" +
                            "<td class='d-flex flex-wrap gap-1'>" + btns + "</td>";

                        tableBody.appendChild(tr);
                        console.log(tableBody.children.length);
                        tableBody.style.display = "table-row-group";
                    });
                })
                .catch(err => console.error(err));
        }

        resSearchInput.addEventListener("input", performSearch);
        statusFilter.addEventListener("change", performSearch);

        function checkDateChanges() {
            if (checkInDate.value !== "" && checkOutDate.value !== "")
            {
                performSearch();
            }
        }

        checkInDate.addEventListener("change", checkDateChanges);
        checkOutDate.addEventListener("change", checkDateChanges);
    });

    // view res modal
    function openReservationModal(id) {

        fetch('<c:url value="/reservation/details?id=" />' + id)
            .then(response => response.json())
            .then(data => {
                console.log(data);
                // reservation
                document.getElementById("reservationNumber").value =
                    data.reservationDTO.reservationNumber;

                document.getElementById("guestId").value =
                    data.reservationDTO.guestId;

                document.getElementById("roomType").value =
                    data.roomTypeName + " Room";

                document.getElementById("roomNum").value =
                    data.roomNum;

                document.getElementById("roomId").value =
                    data.reservationDTO.roomId;

                document.getElementById("resStatus").value =
                    data.reservationDTO.status;

                document.getElementById("stayCost").value =
                    data.reservationDTO.totalCost;

                const checkIn = new Date(data.reservationDTO.checkInDate);
                const checkOut = new Date(data.reservationDTO.checkOutDate);
                document.getElementById("modalCheckInDate").value = checkIn.toLocaleDateString();
                document.getElementById("modalCheckOutDate").value = checkOut.toLocaleDateString();

                document.getElementById("numAdults").value =
                    data.reservationDTO.numOfAdults;

                document.getElementById("numChildren").value =
                    data.reservationDTO.numOfChildren;

                document.getElementById("dateOfRes").value =
                    data.reservationDTO.dateOfReservation;

                document.getElementById("guestReg").value =
                    data.guestDTO.registrationNumber;

                document.getElementById("name").value =
                    data.guestDTO.firstName + " " + data.guestDTO.lastName;

                document.getElementById("email").value =
                    data.guestDTO.email;

                document.getElementById("contactNumber").value =
                    data.guestDTO.contactNumber;

                document.getElementById("passportNum").value =
                    data.guestDTO.passportNumber ?? 'N/A';

                document.getElementById("nic").value =
                    data.guestDTO.nic ?? 'N/A';

                document.getElementById("address").value =
                    data.guestDTO.address;



                // this bill area is only if a bill for it is made
                if (data.billDTO) {
                    document.getElementById("billId").value = data.billDTO.id;
                    document.getElementById("billStayCost").value = data.billDTO.stayCost;
                    document.getElementById("tax").value = data.billDTO.tax;
                    document.getElementById("discount").value = data.billDTO.discount;
                    document.getElementById("totalBillAmount").value = data.billDTO.totalAmount;
                    document.getElementById("billStatus").value = data.billDTO.status;
                }
                else {
                    document.getElementById("billId").value = 'Bill is not generated yet.';
                    document.getElementById("billStayCost").value = 'N/A';
                    document.getElementById("tax").value = 'N/A';
                    document.getElementById("discount").value = 'N/A';
                    document.getElementById("totalBillAmount").value = 'N/A';
                    document.getElementById("billStatus").value = 'N/A';
                }

                // same goes for payment
                if (data.paymentDTO) {
                    document.getElementById("paymentMethod").value = data.paymentDTO.paymentMethod;
                    document.getElementById("paymentDate").value = data.paymentDTO.paymentDate;
                    document.getElementById("totalPaymentAmount").value = data.paymentDTO.amount;
                    document.getElementById("makePayment").hidden = true;
                }
                else
                {
                    document.getElementById("paymentMethod").value = 'N/A';
                    document.getElementById("paymentDate").value = 'N/A';
                    document.getElementById("totalPaymentAmount").value = 'Payment has not been made yet';
                    document.getElementById("makePayment").hidden = false;
                }

                document.getElementById("reservationModal").style.display = "block";
            })
            .catch(err => console.error(err));
    }

    // closing all modals
    function closeModal() {
        document.getElementById("reservationModal").style.display = "none";
    }

    // closes modal if user clicks outside it
    window.onclick = function(event) {
        const modal = document.getElementById("reservationModal");
        if (event.target === modal) closeModal();
    }

    // redirecting to payment modal with the necessary attributes
    function goToMakePaymentPage() {
        const reservationNum = document.getElementById("reservationNumber").value;
        const totalCost = document.getElementById("stayCost").value;
        const guestId = document.getElementById("guestId").value;

        window.location.href = "${pageContext.request.contextPath}/payment/form?reservationNum=" + reservationNum + "&totalCost=" + totalCost + "&guestId=" + guestId;

    }

    // updating reservation status buttons
    function changeResStatus(id, status)
    {
        const params = new URLSearchParams();
        params.append("id", id);
        params.append("status", status);

        fetch('<c:url value="/reservation/status" />',
            {
                method: 'post',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: params.toString()
            }
        ).then(response => response.json())
            .then(data => {
                console.log(data);
                if (data.success)
                {
                    location.reload();
                }
            })
            .catch(err => console.error(err));
    }

</script>

</body>
</html>
