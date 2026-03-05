<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <title>Make Reservation</title>
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

    .button-row {
    margin-top: 35px;
    display: flex;
    gap: 20px;
    }

    .section-title {
        font-size: 26px;
        font-weight: 600;
        margin-bottom: 20px;
        color: #2E6F95;
    }

    .available-rooms-box {
        background: #ffffff;
        padding: 25px;
        border-radius: 10px;
        border: 1px solid #e6e6e6;
        min-height: 400px;
    }

    .room-item {
        padding: 10px;
        border-bottom: 1px solid #eee;
        font-size: 14px;
    }

    .enter-btn {
    background: #2E6F95;
    border-radius: 30px;
    padding: 10px 28px;
    font-size: 14px;
    color: white;
    border: none;
    cursor: pointer;
    transition: background-color 0.2s ease;
    }

    .enter-btn.clicked
    {
        background: #619198;
        color: white;
    }

    .enter-btn:hover {
    background: #245a77;
    }

     /*for the avaialble rooms that display*/
     .room-item {
         background: #ffffff;
         border: 1px solid #e6e6e6;
         border-radius: 12px;
         padding: 20px;
         margin-bottom: 18px;
         box-shadow: 0 6px 18px rgba(0,0,0,0.05);
         transition: 0.2s ease;
     }

    .room-item:hover {
        transform: translateY(-2px);
        box-shadow: 0 10px 22px rgba(0,0,0,0.08);
    }

    .room-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 15px;
    }

    .room-number {
        font-size: 18px;
        font-weight: 600;
        color: #2E6F95;
    }

    .room-details {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 8px 20px;
        font-size: 14px;
        color: #444;
        margin-bottom: 12px;
    }

    .room-amenities {
        margin-top: 10px;
    }

    .amenities-list {
        display: flex;
        flex-wrap: wrap;
        gap: 8px;
    }

    .amenity-badge {
        background: #e8f3f9;
        color: #2E6F95;
        padding: 5px 10px;
        border-radius: 30px;
        font-size: 15px;
        font-weight: 550;
    }

    .no-amenities {
        font-size: 13px;
        color: #999;
    }

    .error-message {
        color: red;
        font-size: 13px;
        margin-top: 4px;
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
                            <button type="button" id="generateResNum" class="generate-btn">
                                Generate
                            </button>
                        </div>
                        <span class="error-message" id="resNumError"></span>
                    </div>

                    <div class="form-group">
                        <label>Total Cost</label>
                        <div class="inline-field">
                        <input type="number" step="0.01" name="totalCost" id="totalCost" readonly>
                        <button type="button" class="generate-btn" onclick="calculateTotalCost()">
                            Calculate Total Cost
                        </button>
                    </div>
                        <span class="error-message" id="totalCostError"></span>
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
                            <option value="1">Standard</option>
                            <option value="2">Deluxe</option>
                            <option value="3">Suite</option>
                        </select>
                        <span class="error-message" id="roomTypeError"></span>
                    </div>

                    <div class="form-group">
                        <label>Amenities & Services</label>
                        <div class="amenities-group">
                            <label><input type="checkbox" name="amenities" class="amenity" value="1"> WiFi</label>
                            <label><input type="checkbox" name="amenities" class="amenity" value="2"> Swimming Pool</label>
                            <label><input type="checkbox" name="amenities" class="amenity" value="7"> Ironing Board</label>
                            <label><input type="checkbox" name="amenities" class="amenity" value="9"> Minibar</label>
                            <label><input type="checkbox" name="amenities" class="amenity" value="5"> Bed & Breakfast</label>
                        </div>
                    <div class="amenities-group">
                        <label><input type="checkbox" name="amenities" class="amenity" value="6"> Spa</label>
                        <label><input type="checkbox" name="amenities" class="amenity" value="3"> Bathtub</label>
                        <label><input type="checkbox" name="amenities" class="amenity" value="4"> Balcony</label>
                        <label><input type="checkbox" name="amenities" class="amenity" value="8"> Hammock</label>
                        <label><input type="checkbox" name="amenities" class="amenity" value="10"> Personalized Galle Giftpack</label>
                    </div>
                        <span class="error-message" id="amenitiesError"></span>
                    </div>

                    <div class="form-group">
                        <label>Stay Room Cost</label>
                        <input type="text" name="stayCost" id="stayCost" required readonly>
                    </div>

                    <div class="form-group">
                        <label>Amenities Cost</label>
                        <input type="text" name="amenitiesCost" id="amenitiesCost" required readonly>
                    </div>


                    <div class="form-group">
                        <label>Guest Registration Number</label>
                        <input type="text" name="guestRegNumber" id="guestRegNumber" required>
                        <span class="error-message" id="guestRegNumError"></span>
                    </div>

                    <%--                // storing room id as a hidden value--%>
                    <input type="hidden" name="roomId" id="roomIdInput">
<%--                    <span class="error-message" id="roomIdError"></span>--%>

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
                        <input type="number" name="numAdults" id="adults" min="1">
                    </div>
                    <div class="form-group">
                        <label>Children</label>
                        <input type="number" name="numChildren" id="children" min="0" value="0">
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

<%--            this is a section where the avaialble rooms will be displayed--%>
            <br>
            <br>
            <div class="available-rooms-box">
                <div class="section-title">Available Rooms</div>

                <!-- dynamically filled from check room avail servlet-->
                <div id="availableRoomsList">
                    <input type="hidden" id="roomId" name="roomId">
                    <div class="room-item">No rooms checked yet.</div>
                </div>
            </div>

        </div>
    </div>
</div>


<script>
    document.getElementById("reservationForm").addEventListener("submit", function (e) {
        let valid = true;

        // dates and guests validation
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
            document.getElementById("checkOutError").innerText = "Check-out date must be after check-in date";
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
        }

        // const firstName = document.getElementById("firstName").value;
        // const lastName = document.getElementById("lastName").value;
        // const email = document.getElementById("email").value;
        // const nicOrPassport = document.getElementById("nicOrPassport").value.trim();
        // const regNum = document.getElementById("registrationNumber").value;
        //
        // if (!regNum) {
        //     document.getElementById("regNumberError").style.display = "block";
        // }
        //
        // if (!firstName) {
        //     document.getElementById("firstNameError").style.display = "block";
        // }
        //
        // if (!lastName) {
        //     document.getElementById("lastNameError").style.display = "block";
        // }
        //
        // if (!email) {
        //     document.getElementById("emailError").style.display = "block";
        // }
        //
        // if (!nicOrPassport)
        // {
        //     document.getElementById("nicOrPassportError").style.display = "block";
        // }
    });

    // generating the reservation number
    document.getElementById("generateResNum").addEventListener("click", function (e) {
            fetch("<c:url value='/generate?type=res-id' />")
                .then(response => response.text())
                .then(data => {
                    document.getElementById("reservationNumber").value = data;
                })
                .catch(error => {
                    console.error("Error generating reservation number:", error);
                });
        });

    function checkAvailability() {

        const today = new Date();
        const checkInDate = new Date(document.getElementById("checkInDate").value);
        const checkOutDate = new Date(document.getElementById("checkOutDate").value);

        const checkIn = document.getElementById("checkInDate").value;
        const checkOut = document.getElementById("checkOutDate").value;

        if (checkInDate < today || checkOut < today) {
            document.getElementById("checkInError").innerText = "Dates cannot be in the past.";
            document.getElementById("checkOutError").innerText = "Dates cannot be in the past.";
            return;
        }
        else {
            document.getElementById("checkInError").style.display = "none";
            document.getElementById("checkOutError").style.display = "none";
        }

        if (checkOutDate <= checkInDate) {
            document.getElementById("checkOutError").innerText = "Check-out date must be after check-in date";
            return;
        }
        else {
            document.getElementById("checkOutError").style.display = "none";
        }

            const roomTypeId = document.getElementById("roomType").value;
            const amenities = Array.from(document.querySelectorAll(".amenity:checked"))
                .map(cb => cb.value);

            const container = document.getElementById("availableRoomsList");
            container.innerHTML = "<div class='room-item'>Checking availability...</div>";

            if (!checkIn || !checkOut || !roomTypeId) {
                container.innerHTML = "<div class='room-item'>Select check-in, check-out, and room type.</div>";
                return;
            }

            const params = new URLSearchParams();
            params.append("checkIn", checkIn);
            params.append("checkOut", checkOut);
            params.append("roomTypeId", roomTypeId);
            amenities.forEach(a => params.append("amenities", a));

            fetch("<c:url value='/room-avail/check' />", {
                method: "POST",
                headers: { "Content-Type": "application/x-www-form-urlencoded" },
                body: params.toString()
            })
                .then(res => {
                    if (!res.ok) throw new Error(`Something went wrong`)
                    return res.json();
                })
                .then(rooms => {

                    if (rooms.checkInAfterCheckOut)
                    {
                        alert("Check-in date cannot be after check-out date.")
                        return;
                    }
                    if (rooms.datesBeforeToday)
                    {
                        alert("Dates cannot be in the past")
                        return;
                    }
                    if (rooms.invalidRoom)
                    {
                        alert("Please select a room type.")
                    }

                    container.innerHTML = ""; // clearing the box area

                    // error message
                    if (!rooms || rooms.length === 0) {
                        container.innerHTML = "<div class='room-item'>No rooms available.</div>";
                        return;
                    }

                    rooms.forEach(room => {
                        console.log(room.roomType.amenityList);
                        container.innerHTML +=
                '<div class="room-item">' +

                            '<div class="room-header">' +
                            '<span class="room-number">Room ' + room.roomNum + '</span>' +
                            '<button type="button" class="enter-btn" data-room-id="' + room.roomId + '" ' +
                            'data-room-type-price="' + room.roomType.basePricePerNight + '">' +
                            'Select</button>' +
                            '</div>' +

                            '<div class="room-details">' +
                            '<div><strong> Type: </strong>' + room.roomType.roomTypeName + '</div>' +
                            '<div><strong> Price Per Night: </strong>' + room.roomType.basePricePerNight + '</div>' +
                            '<div><strong> Bedding: </strong>' + room.roomType.bedding + '</div>' +
                            '<div><strong> Max Occupancy: </strong>' + room.roomType.maxOccupancy + '</div>' +
                            '<div><strong> Status: </strong>' + room.roomStatus + '</div>' +
                            '<div><strong> Floor: </strong>' + room.floorNum + '</div>' +
                            '</div>' +

                            '</div class="room-amenities">' + buildAmenities(room.roomType.amenityList) + '</div>';

                    });
                })
                .catch(error => {
                    console.log(error);
                    container.innerHTML = `<div class='room-item'>Error fetching rooms. Try again.</div>`;
                });
        }

    function buildAmenities(amenities) {

        if (!amenities || amenities.length === 0) {
            return '<span class="no-amenities">No amenities</span>';
        }

        let html = '<div class="amenities-list">';

        amenities.forEach(function(a) {
            html += '<span class="amenity-badge">' + a.name + '</span>';
        });

        html += '</div>' + '<br>';
        return html;
    }

    document.getElementById("availableRoomsList").addEventListener("click", function (e) {

        const button = e.target.closest(".enter-btn");

        const roomId = button.dataset.roomId;
        const roomTypeId = button.dataset.roomTypePrice;

        const hiddenInput = document.getElementById("roomIdInput");

        if (hiddenInput.value === roomId) {
            hiddenInput.value = "";
            button.classList.remove("clicked");
            document.getElementById("totalCost").value = "";
            return;
        }

        // select new room and store the id
        hiddenInput.value = roomId;

        // toggling the btn to change the color
        document.querySelectorAll(".enter-btn")
            .forEach(btn => btn.classList.remove("clicked"));

        button.classList.add("clicked");

        calculateTotalCost(roomTypeId);
    });

    // this dynamically calculates the total cost for the reservation
    function calculateTotalCost(roomTypeId) {
        const checkInValue = document.getElementById("checkInDate").value;
        const checkOutValue = document.getElementById("checkOutDate").value;
        //const amenitiesCost = document.getElementById("amenitiesCost").value;
        let totalCostField = document.getElementById("totalCost");
        let stayCost = document.getElementById("stayCost");

        if (!checkInValue || !checkOutValue || !roomTypeId) {
            alert("Select check-in, check-out and room type first.");
            return;
        }

        // converting the values to dates
        const checkInDate = new Date(checkInValue)
        const checkOutDate = new Date(checkOutValue);

        if (checkOutDate <= checkInDate) {
            alert("The check-out date has to be after check-in date.")
            return;
        }
        const millisecondsPerDay = 1000 * 60 * 60 * 24;
        const numOfNights = Math.floor((checkOutDate - checkInDate) / millisecondsPerDay);
        console.log(numOfNights);
        console.log(roomTypeId);
        // calculating the total stay cost by taking the base price per night passed

       stayCost.value = numOfNights * roomTypeId;
       // totalCostField.value = stayCost + amenitiesCost;
    }

    document.querySelectorAll("input[name='amenities']").forEach(cb => {
        cb.addEventListener("change", updateAmenitiesCost);
    });

    function updateAmenitiesCost() {
        const selected = Array.from(
            document.querySelectorAll("input[name='amenities']:checked")
        ).map(cb => cb.value);

        if (selected.length === 0) {
            alert("Please select the amenities of the room");
            return;
        }
        const params = new URLSearchParams();
        selected.forEach(a => params.append("amenities", a));

        const roomId = document.getElementById("roomIdInput").value;
        const stayCost = document.getElementById("stayCost").value;
        const totalCostField = document.getElementById("totalCost");
        if (!roomId) {
            alert("Please select a room.");
            return;
        }

        params.append("roomId", document.getElementById("roomIdInput").value);
        params.append("checkInDate", document.getElementById("checkInDate").value);
        params.append("checkOutDate", document.getElementById("checkOutDate").value);

        fetch('<c:url value="/reservation/calculate" />', {
            method: "post",
            body: params
        })
            .then(res => res.json())
            .then(data => {
                const amenitiesCost = parseFloat(
                    document.getElementById("amenitiesCost").value = data.amenitiesCost.toFixed(2)
                );

                const stayCostValue = parseFloat(stayCost);
                totalCostField.value = (stayCostValue + amenitiesCost).toFixed(2);
            });
    }

    // servlet params
    const servletParams = new URLSearchParams(window.location.search);
    const error = servletParams.get("error");

    if (error === "system_error")
    {
        alert("Something went wrong! We apologize!")
    }
    if (error === "guest_not_found")
    {
        alert("Guest registration number was not found.")
    }
    if (error === "adult_guest_required")
    {
        alert("At least one adult guest is required.")
    }
    if (error === "empty_fields")
    {
    alert("Please fill in all fields");
    }

    if (error === "checkindate_after_checkout")
    {
        alert("Check-in date cannot be after checkout date");
    }
    if (error === "dates_in_past")
    {
        alert("Dates cannot be in past")
    }

    if (error === "empty_room_check_fields")
    {
        alert("Please select check-in, check-out and room type.")
    }

    if (error === "invalid_room")
    {
        alert("Please select a room for reservation.")
    }
    if (error === "empty_cost")
    {
        alert("Total Stay cost is required.")
    }
    if (error === "duplicate_res_num")
    {
        alert("Reservation number already exists. Please generate another one.");
    }

</script>

<%--%--    // after successful validation and reservation is made, you can choose to make the payment now ;--%>
<c:if test="${not empty param.success}">
    <script>
        const reservationNum = "${param.reservationNum}";
        const totalCost = "${param.totalCost}";
        let guestId = "${param.guestId}";

        if (reservationNum) {
            const confirmPayment = confirm(
                "The reservation was made successfully! Email was sent to the guest!\nDo you want to make the payment now?"
            );

            if (confirmPayment) {
                window.location.href =
                    "${pageContext.request.contextPath}/payment/form?reservationNum=" + reservationNum + "&totalCost=" + totalCost + "&guestId=" + guestId;
            }
            else
            {
                window.location.href = "${pageContext.request.contextPath}/reservation/all";
            }
        }
    </script>
</c:if>

</body>
</html>
