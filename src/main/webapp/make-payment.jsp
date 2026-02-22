<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<head>
    <title>Make Payment</title>

    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">

    <style>
        .content {
            margin-left: 240px;
            padding: 40px;
        }

        .form-card {
            max-width: 700px;
            background: #ffffff;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 8px 20px rgba(0,0,0,0.1);
        }

        .form-title {
            font-weight: 600;
            margin-bottom: 25px;
            color: #4682A9;
        }

        .enter-btn {
            background: #4682A9 !important;
            margin-top: 20px;
            border-radius: 20px;
            padding: 6px 25px;
            font-size: 18px;
            color: white;
        }

        .enter-btn:hover {
            background: #20465e !important;
            color: white;
        }

        .form-error {
            margin-top: 12px;
            padding: 10px 14px;
            border-radius: 6px;
            background-color: #fdecea;
            color: #b42318;
            border: 1px solid #f5c2c7;
            font-size: 0.95rem;
            font-weight: 500;
        }

    </style>
</head>

<body>

<jsp:include page="sidebar.jsp"/>

<div class="content">
    <div class="form-card">
        <h3 class="form-title">Make Payment</h3>

        <form action="<c:url value='/payment/generate' />" method="post">

            <div class="mb-3">
                <label class="form-label">Reservation Number</label>
                <input type="text" id="reservationNum" name="reservationNum" class="form-control" onblur="getReservationBillDetails()" required>
            </div>

            <div class="mb-3">
                <label class="form-label">Guest ID</label>
                <input type="text" name="guestId" id="guestId" class="form-control" required>
            </div>

            <div class="mb-3">
                <label class="form-label">Total Stay Cost</label>
                <input type="number" step="0.01" id="stayCost" name="stayCost" class="form-control" required>
            </div>

            <div class="mb-3">
                <label class="form-label">Discount</label>
                <input type="number" step="5" name="discount" id="discount"  class="form-control" required>
            </div>

            <div class="mb-3">
                <label class="form-label">Tax</label>
                <input type="number" step="5" name="tax"  id="tax" class="form-control" required>
            </div>

            <div class="mb-3">
                <label class="form-label">Total Amount</label>
                <input type="number" step="0.01" name="amount" id="amount" class="form-control" required>
            </div>

            <div class="mb-3">
                <label class="form-label">Payment Method</label>
                <select name="paymentMethod" id="paymentMethod" class="form-control" required>
                    <option value="">Select Payment Method </option>
                    <option value="Card">Credit / Debit Card</option>
                    <option value="Cash">Cash</option>
                    <option value="Bank Transfer">Bank Transfer</option>
                </select>
            </div>

            <div id="cardSection" style="display: none;">
<%--            card details only if card option is selected--%>
            <div class="mb-3">
                <label class="form-label">Card Number</label>
                <input type="text" name="cardNumber" class="form-control">
            </div>

            <div class="mb-3">
                <label class="form-label">Card Holder Name</label>
                <input type="text" name="cardHolderName" class="form-control">
            </div>

            <div class="mb-3">
                <label class="form-label">Expiry Date</label>
                <input type="month" name="expiryDate" class="form-control">
            </div>

            <div class="mb-3">
                <label class="form-label">CVV</label>
                <input type="password" name="cvv" class="form-control" maxlength="4">
            </div>
            </div>

            <button type="submit" formaction="<c:url value='/payment/generate' />"value= "billAndPayment"  class="btn enter-btn">Generate Bill & Confirm Payment</button>

            <c:if test="${not empty errorMessage}">
                <div class="form-error">
                        ${errorMessage}
                </div>
            </c:if>

        </form>
    </div>
</div>

</body>


<script>

<%--    function getReservationBillDetails() {--%>
<%--        const resNum = document.getElementById("reservationNum").value.trim();--%>
<%--        if (!resNum) return;--%>

<%--        const params = new URLSearchParams(window.location.search);--%>
<%--        const resId = params.get("resNum");--%>
<%--        if (reservationNum) {--%>
<%--            document.getElementById("reservationNum").value = resNum;--%>
<%--            getReservationBillDetails();--%>
<%--        }--%>
<%--        params.append("reservationNum", resNum);--%>

<%--        fetch('<c:url value="/payment/details?num=" />' + params.toString(),--%>
<%--            {--%>
<%--                headers: {--%>
<%--                    "X-Requested-With": "XMLHttpRequest"--%>
<%--                }--%>
<%--            })--%>
<%--            .then(res => {--%>
<%--                if (!res.ok) {--%>
<%--                    throw new Error("HTTP error " + res.status);--%>
<%--                }--%>
<%--                return res.json();--%>
<%--            })--%>
<%--            .then(data =>--%>
<%--            {--%>
<%--                document.getElementById("stayCost").value = parseFloat(data.totalCost).toFixed(2);--%>
<%--                document.getElementById("guestId").value = data.guestId;--%>

<%--                // re calculating the total cost based on new factors like tax and discount--%>
<%--                const discount = parseFloat(document.getElementById("discount").value  || 0);--%>
<%--                const tax = parseFloat(document.getElementById("tax").value || 0)--%>
<%--                document.getElementById("amount").value = ((data.totalCost - discount) + tax).toFixed(2);--%>
<%--            })--%>
<%--            .catch(err => {--%>
<%--                console.error(err);--%>
<%--                alert("Something went wrong loading the reservation details.")--%>
<%--            })--%>
<%--    }--%>

<%--    document.getElementById("discount").addEventListener("input", getReservationBillDetails);--%>
<%--    document.getElementById("tax").addEventListener("input", getReservationBillDetails);--%>

    const paymentSelect = document.getElementById("paymentMethod");
    const cardSection = document.getElementById("cardSection");

    paymentSelect.addEventListener("change", function () {
        if (this.value === "Card") {
            cardSection.style.display = "block";
        } else {
            cardSection.style.display = "none";
        }
    });
</script>
