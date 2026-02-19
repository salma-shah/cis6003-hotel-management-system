<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Register Guest</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
<style>
    .dashboard-layout {
    display: flex;
    min-height: 100vh;
    background-color: #f4f6f9;
    }

    .dashboard-content {
    flex: 1;
    padding: 40px 60px;
    margin-left: 240px;
    }

    .content-card {
    background: #fff;
    border-radius: 12px;
    padding: 40px;
    box-shadow: 0 8px 25px rgba(0,0,0,0.06);
    max-width: 1200px;
    margin: auto;
    }

    .form-title {
    font-size: 28px;
    font-weight: 600;
    margin-bottom: 35px;
    color: #2E6F95;
    }

    .form-grid {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 28px 40px;
    }

    .form-group {
    display: flex;
    flex-direction: column;
    }

    .form-group label {
    margin-bottom: 8px;
    font-weight: 500;
    font-size: 14px;
    color: #444;
    }

    .form-group input,
    .form-group select {
    height: 44px;
    padding: 0 12px;
    border-radius: 8px;
    border: 1px solid #dcdcdc;
    font-size: 14px;
    transition: 0.2s ease;
    }

    .form-group input:focus,
    .form-group select:focus {
    outline: none;
    border-color: #2E6F95;
    box-shadow: 0 0 0 2px rgba(46,111,149,0.1);
    }

    /* Reservation number inline */
    .inline-field {
    display: flex;
    gap: 12px;
    }

    .inline-field input {
    flex: 1;
    }

    .generate-btn {
    height: 44px;
    padding: 0 20px;
    border-radius: 8px;
    border: none;
    background: #2E6F95;
    color: white;
    font-weight: 500;
    cursor: pointer;
    transition: 0.2s ease;
    }

    .generate-btn:hover {
    background: #245a77;
    }

    /* Amenities clean layout */
    .amenities-group {
    display: flex;
    gap: 20px;
    align-items: center;
    margin-top: 5px;
    }

    .amenities-group label {
    display: flex;
    align-items: center;
    gap: 6px;
    font-weight: 400;
    }

    /* Buttons */
    .button-row {
    margin-top: 35px;
    display: flex;
    gap: 20px;
    }

    .enter-btn {
    background: #2E6F95;
    border-radius: 30px;
    padding: 10px 28px;
    font-size: 14px;
    color: white;
    border: none;
    cursor: pointer;
    transition: 0.2s ease;
    }

    .enter-btn:hover {
    background: #245a77;
    }
</style>
</head>

<body>
<div class="dashboard-layout">
    <jsp:include page="sidebar.jsp"/>
    <div class="dashboard-content">
        <div class="content-card">
            <h3 class="form-title">
                <i class="bi bi-plus-lg"></i>
                Make Reservation</h3>

            <form id="reservationForm" action="<c:url value='/reservation/create' />" method="post">

                <div class="form-grid">

                    <div class="form-group">
                        <label>Reservation Number</label>
                        <div class="inline-field">
                            <input type="text" name="reservationNumber" id="reservationNumber" readonly>
                            <button type="button" class="generate-btn" onclick="generateReservationNumber()">
                                Generate
                            </button>
                        </div>
                    </div>

                    <div class="form-group">
                        <label>Total Cost</label>
                        <input type="number" step="0.01" name="totalCost" id="totalCost" readonly>
                        <button type="button" class="generate-btn" onclick="calculateTotalCost()">
                            Calculate Total Cost
                        </button>
                    </div>

                    <div class="form-group">
                        <label>Check-In Date</label>
                        <input type="date" name="checkInDate" id="checkInDate" required>
                        <span class="error-message" id="checkInError"></span>
                    </div>

                    <div class="form-group">
                        <label>Check-Out Date</label>
                        <input type="date" name="checkOutDate" id="checkOutDate" required>
                        <span class="error-message" id="checkOutError"></span>
                    </div>

                    <div class="form-group">
                        <label>Room Type</label>
                        <select name="roomType" id="roomType" required>
                            <option value="">Select Room Type</option>
                            <option value="STANDARD">Standard</option>
                            <option value="DELUXE">Deluxe</option>
                            <option value="SUITE">Suite</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label>Amenities</label>
                        <div class="amenities-group">
                            <label><input type="checkbox" name="amenities" value="AC"> AC</label>
                            <label><input type="checkbox" name="amenities" value="WIFI"> WiFi</label>
                            <label><input type="checkbox" name="amenities" value="TV"> TV</label>
                            <label><input type="checkbox" name="amenities" value="MINIBAR"> Mini Bar</label>
                        </div>
                    </div>

                    <div class="form-group">
                        <label>Guest Registration Number</label>
                        <input type="text" name="guestRegNumber" id="guestRegNumber" required>
                    </div>

                    <div class="form-group">
                        <label>First Name</label>
                        <input type="text" name="firstName" id="firstName" required>
                    </div>

                    <div class="form-group">
                        <label>Last Name</label>
                        <input type="text" name="lastName" id="lastName" required>
                    </div>

                    <div class="form-group">
                        <label>NIC / Passport</label>
                        <input type="text" name="nicOrPassport" id="nicOrPassport" required>
                    </div>

                    <div class="form-group">
                        <label>Email</label>
                        <input type="email" name="email" id="email" required>
                    </div>

                    <div class="form-group">
                        <label>Adults</label>
                        <input type="number" name="adults" id="adults" min="1" required>
                    </div>

                    <div class="form-group">
                        <label>Children</label>
                        <input type="number" name="children" id="children" min="0" value="0">
                    </div>
                </div>

                <div class="button-row">
                    <button type="button" class="enter-btn" onclick="checkAvailability()">
                        Check Availability
                    </button>
                    <button type="submit" class="enter-btn">
                        Make Reservation
                    </button>
                </div>

            </form>
        </div>
    </div>
</div>
</body>

<script>
    document.getElementById("reservationForm").addEventListener("submit", function (e) {

        let valid = true;

        const checkIn = new Date(document.getElementById("checkInDate").value);
        const checkOut = new Date(document.getElementById("checkOutDate").value);
        const today = new Date();

        document.getElementById("checkInError").innerText = "";
        document.getElementById("checkOutError").innerText = "";

        if (checkIn < today) {
            document.getElementById("checkInError").innerText = "Check-in cannot be in the past.";
            valid = false;
        }

        if (checkOut <= checkIn) {
            document.getElementById("checkOutError").innerText = "Check-out must be after check-in.";
            valid = false;
        }

        const adults = parseInt(document.getElementById("adults").value);
        const children = parseInt(document.getElementById("children").value);

        if (adults + children <= 0) {
            alert("At least one guest required.");
            valid = false;
        }

        if (!valid) {
            e.preventDefault();
            return;
        }

        // After successful validation
        const confirmPayment = confirm("Reservation successful. Do you want to make the payment now?");
        if (confirmPayment) {
            window.location.href = "${pageContext.request.contextPath}/payment";
        }
    });

        function checkAvailability() {

        const checkIn = document.getElementById("checkInDate").value;
        const checkOut = document.getElementById("checkOutDate").value;
        const roomType = document.getElementById("roomType").value;

        if (!checkIn || !checkOut || !roomType) {
        alert("Please select check-in, check-out and room type first.");
        return;
    }

        fetch("${pageContext.request.contextPath}/reservation/checkAvailability", {
        method: "POST",
        headers: {
        "Content-Type": "application/x-www-form-urlencoded"
    },
        body: `checkIn=${checkIn}&checkOut=${checkOut}&roomType=${roomType}`
    })
        .then(response => response.text())
        .then(data => {
        alert(data);
    });
    }
</script>
