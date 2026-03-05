<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <title>Dashboard</title>
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
            margin-bottom: 30px;
        }

        .header-row {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 25px;
        }

        .welcome-row {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 40px;
        }

        .btn-primary {
            background: #4682A9 !important;
            margin-top: 20px;
            border-radius: 20px;
            padding: 6px 25px;
            font-size: 17px;
            color: white;
        }

        .stat-card {
            padding: 20px;
            border-radius: 12px;
            background: #4682A9;
            color: white;
            text-align: center;
            box-shadow: 0 4px 16px rgba(0,0,0,0.1);
        }

        .stat-card h3 {
            margin: 10px 0 0 0;
            font-size: 28px;
        }

        .stat-card p {
            margin: 0;
            font-weight: 600;
        }

        .quick-links {
            display: flex;
            gap: 20px;
            flex-wrap: wrap;
        }

        .quick-link-card {
            flex: 1;
            min-width: 180px;
            background: #fff;
            padding: 20px;
            border-radius: 12px;
            text-align: center;
            box-shadow: 0 4px 12px rgba(0,0,0,0.05);
            transition: transform 0.2s;
        }

        .quick-link-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 16px rgba(0,0,0,0.1);
        }

        .quick-link-card i {
            font-size: 32px;
            margin-bottom: 10px;
            color: #4682A9;
        }

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

        .btn-changepw
        {
            background-color: #7f94af !important;
        }
        .converter-box {
            width: 800px;
            padding: 20px;
            border: 1px solid #ccc;
            border-radius: 6px;
            background: #fff;
        }
        .converter-box input, .converter-box select {
            width: 100%;
            padding: 8px;
            margin: 6px 0;
        }
    </style>
</head>
<body>
<div class="dashboard-layout">
    <jsp:include page="sidebar.jsp"/>

    <main class="dashboard-content">
        <!-- header welcome row -->
        <div class="welcome-row">
            <c:choose>
                <c:when test="${not empty sessionScope.username}">
                    <h2>Welcome, <c:out value="${sessionScope.username}" />!</h2>
                </c:when>
                <c:otherwise>
                    <h2>Welcome to Ocean View Resort Dashboard</h2>
                </c:otherwise>
            </c:choose>
            <button class="btn btn-primary" onclick="openChangePwModal()">
                Change Password
            </button>
        </div>

        <!-- count of reservations -->
        <div class="content-card">
            <div class="header-row">
                <h3>Reservations Overview</h3>
            </div>
            <div class="row g-4">
                <div class="col-md-3">
                    <div class="stat-card">
                        <p>Confirmed</p>
                        <h3><c:out value="${reservationCount.Confirmed}" /></h3>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="stat-card ">
                        <p>Checked-In</p>
                        <h3><c:out value="${reservationCount.CheckedIn}" /></h3>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="stat-card ">
                        <p>Checked-Out</p>
                        <h3><c:out value="${reservationCount.CheckedOut}" /></h3>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="stat-card ">
                        <p>Cancelled</p>
                        <h3><c:out value="${reservationCount.Cancelled}" /></h3>
                    </div>
                </div>
            </div>
        </div>

        <!-- links for navigations -->
        <div class="content-card">
            <div class="header-row">
                <h3>Actions</h3>
            </div>
            <div class="quick-links">
                <div class="quick-link-card">
                    <i class="bi bi-person-plus"></i>
                    <p><a href="<c:url value='/reservation/create' />">New Reservation</a></p>
                </div>
                <div class="quick-link-card">
                    <i class="bi bi-people"></i>
                    <p><a href="<c:url value='/guest/register' />">Register Guest</a></p>
                </div>
                <div class="quick-link-card">
                    <i class="bi bi-house-door"></i>
                    <p><a href="<c:url value='/room/all' />">Room Management</a></p>
                </div>
            </div>
        </div>


        <!-- currency converter -->
        <div class="content-card">
        <div class="converter-box">
            <h3>Currency Converter</h3>
            <br>
            <label>Amount</label>
            <input type="number" id="amount" placeholder="Enter amount..." />

            <label>From</label>
            <select id="from">
                <option value="USD">&dollar; USD</option>
                <option value="EUR">&euro; EUR</option>
                <option value="GBP">&pound; GBP</option>
                <option value="AUD">A&dollar; AUD</option>
                <option value="LKR">Rs LKR</option>
            </select>

            <label>To</label>
            <select id="to">
                <option value="LKR">Rs LKR</option>
                <option value="USD">&dollar; USD</option>
                <option value="EUR">&euro; EUR</option>
                <option value="GBP">&pound; GBP</option>
                <option value="AUD">A&dollar; AUD</option>
            </select>

            <label>Converted Amount</label>
            <input type="text" id="result" readonly />
        </div>
        </div>

    </main>


    <div id="changePwModal" class="custom-modal">
        <div class="custom-modal-content">
            <span class="close" onclick="closeModal('changePwModal')">&times;</span>
            <form method="post" action="<c:url value='/user/change-password' />">
                <label>Username</label>
                <input class="form-control" name="username" id="username">
                <label>New Password</label>
                <input class="form-control" name="newPw" id="newPw">
                <br>
                    <button type="submit" class="btn-changepw">Change Password</button>
            </form>
        </div>
    </div>

    <script>
        function openChangePwModal() {
            document.getElementById('changePwModal').style.display = 'block';
        }

        // closes modal if user clicks outside it
        function closeModal(modalId) {
            document.getElementById(modalId).style.display = "none";
        }

        window.onclick = function(event) {
            ["changePwModal"].forEach(id => {
                let modal = document.getElementById(id);
                if(event.target === modal) modal.style.display = "none";
            });
        }

        // currency converter
        async function convertCurrency(){
            const amount = document.getElementById("amount").value;
            const from = document.getElementById("from").value;
            const to = document.getElementById("to").value;

            const params = new URLSearchParams();
            params.append("amount", amount);
            params.append("from", from);
            params.append("to", to);

            if (!amount || amount <= 0) return;

            fetch('<c:url value="/currency/convert" />',
                {
                    method: "post",
                    headers: { "Content-Type": "application/x-www-form-urlencoded" },
                    body: params.toString(),
                }
            )
                .then(response => response.json())
                .then(data => {
                    console.log(data);
                    document.getElementById("result").value = data.conversion_result.toFixed(2);
                })
        }

        document.getElementById("amount").addEventListener("input", convertCurrency);
        document.getElementById("from").addEventListener("change", convertCurrency);
        document.getElementById("to").addEventListener("change", convertCurrency);

        const params = new URLSearchParams(window.location.search);
        const error = params.get('error');
        const success = params.get('success');

        if (success === "true") {
            alert("Password was changed successfully!");
        }
        if (error === "empty_fields") {
            alert("Username or password cannot be empty!");
        }
        if (error === "system_error") {
            alert("Something went wrong.")
        }
        if (error === "weak_password") {
            alert("Password has to be 8 characters long and contain numbers and letters.")
        }

    </script>
</div>
</body>
</html>