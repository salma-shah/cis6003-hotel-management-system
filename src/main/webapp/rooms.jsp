<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <title>Manage Rooms</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css"/>
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

    .number-filter {
        display: flex;
        gap: 15px;
        margin-bottom: 20px;
    }

    .number-filter input,
    .number-filter select {
        width: 185px;
        padding: 10px;
        border-radius: 6px;
        border: 1px solid #ccc;
    }

    /* room cards */
    .room-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
        gap: 20px;
    }

    .room-card {
        background: #ffffff;
        border-radius: 10px;
        box-shadow: 0 4px 10px rgba(0,0,0,0.05);
        overflow: hidden;
        display: flex;
        flex-direction: column;
    }

    .room-card img {
        width: 100%;
        height: 160px;
        object-fit: cover;
    }

    .room-body {
        padding: 15px;
        flex: 1;
    }

    .room-body h5 {
        margin-bottom: 8px;
        font-weight: 600;
    }

    .room-meta {
        font-size: 14px;
        color: #555;
        margin-bottom: 6px;
    }

    .room-status {
        font-weight: 600;
    }

    .status-available { color: green; }
    .status-occupied { color: red; }
    .status-maintenance { color: orange; }

    .room-footer {
        padding: 12px 15px;
        background: #f8f9fa;
        text-align: right;
    }

    .amenities-filter label {
        margin-right: 20px;
    }

    .enter-btn {
        background: #4682A9 !important;
        margin-top: 20px;
        border-radius: 20px;
        padding: 6px 25px;
        font-size: 14px;
        color: white;
    }
</style>

<body>


<div class="dashboard-layout">
    <jsp:include page="sidebar.jsp"/>

    <main class="dashboard-content">
        <section class="content-card">

<%--        this is a section only manager can view--%>
<c:if test="${sessionScope.userRole == 'Manager'}">
            <div class="header-row">
                <h2>Manage Rooms</h2>
                <a href="<c:url value='/room/create' />" class="btn enter-btn">
                    + Create New Room
                </a>
            </div>
</c:if>

            <div class="number-filter">
                <input type="number" id="searchInput" placeholder="Search by floor number...">

<%--                // these are the filters: --%>
<%--                // status filter --%>
                <select id="statusFilter" name="status">
                    <option value="">All Status</option>
                    <option value="Available">Available</option>
                    <option value="Unavailable">Unavailable</option>
                    <option value="Maintenance">Maintenance</option>
                </select>

<%--                room type filter--%>
                <select id="typeFilter" name="type">
                    <option value="">All Room Types</option>
                    <option value="Standard">Standard</option>
                    <option value="Deluxe">Deluxe</option>
                    <option value="Suite">Suite</option>
                </select>

<%--                // bedding type filter--%>
                <select id="beddingInput" name="bedding">
                    <option value="">All Bedding Types</option>
                    <option value="Single">Single</option>
                    <option value="Twin">Twin</option>
                    <option value="Double">Double</option>
                    <option value="King">King</option>
                </select>

<%--                filter for max occupancy--%>
                <input type="number" placeholder="Number of Adults" name="guestsAdults" id="guestsAdults" step="1" min="11" required>
                <input type="number" placeholder="Number of Children" name="guestsChildren" id="guestsChildren" step="1" min="0">

            </div>
<%--    // amenity filters--%>
    <div class="amenities-filter" name="amenities">
        <label><input type="checkbox"  name="amenities" class="amenity-checked" value="1"> WiFi</label>
        <label><input type="checkbox"  name="amenities" class="amenity-checked" value="2">  Swimming Pool</label>
        <label><input type="checkbox"  name="amenities" class="amenity-checked" value="7"> Ironing Board</label>
        <label><input type="checkbox"  name="amenities" class="amenity-checked" value="9"> Minibar</label>
        <label><input type="checkbox"  name="amenities" class="amenity-checked" value="5"> Bed & Breakfast</label>
    </div>

<%--    you also need a section to filter by dates--%>

    <br>
    <br>

<%--    no rooms avaialbale message as a fallback--%>
            <c:if test="${empty rooms}">
                <p>No rooms available.</p>
            </c:if>

<%--    // this is if there are no search results--%>
    <p id="noRoomsMessage" style="display:none;">No rooms available.</p>

            <div class="room-grid" id="roomGrid">
                <c:forEach var="room" items="${rooms}">
                    <div class="room-card"
                         data-room-id="${room.roomId}"
                         data-category="${room.roomType}"
                         data-amenities="${room.beddingTypes}"
                         data-status="${room.roomStatus}"
                         data-floor-num="${room.floorNum}">

                        <c:if test="${not empty room.roomImgList}">
                            <img src="${pageContext.request.contextPath}/${room.roomImgList[0].imgPath}"
                                 alt = "${room.roomImgList[0].alt}">
                        </c:if>

                        <div class="room-body">
                            <div class="room-meta">Room Type: ${room.roomType}</div>
                            <div class="room-meta">Floor: ${room.floorNum}</div>
                            <div class="room-meta"><i class="bi bi-person-fill-check"></i>: ${room.maxOccupancy}</div>
                            <div class="room-meta room-status
                               ${room.roomStatus == 'Available' ? 'status-available' :
                                 room.roomStatus == 'Unavailable' ? 'status-occupied' : 'status-maintenance'}">
                                Status: ${room.roomStatus}
                            </div>
                        </div>

                        <div class="room-footer">
                        </div>
                    </div>
                </c:forEach>
            </div>
        </section>
    </main>
</div>

<script>
    document.addEventListener("DOMContentLoaded", function() {
        // filter fields
        const searchInput = document.getElementById("searchInput");
        const statusInput = document.getElementById("statusFilter");
        const beddingInput = document.getElementById("beddingInput");
        const guestsAdultInput = document.getElementById("guestsAdults");
        const guestsChildrenInput = document.getElementById("guestsChildren");
        const roomTypeInput = document.getElementById("typeFilter");
        const amenitiesCheckbox = document.querySelectorAll(".amenity-checked");

        // room grid variables
        const roomGrid = document.getElementById("roomGrid");
        const noRoomsMessage = document.getElementById("noRoomsMessage");
        let debounceTimer;

        function performSearch() {
            const floorNum = searchInput.value.trim();
            const statusFilter = statusInput.value.trim();
            const beddingFilter = beddingInput.value.trim();
            const roomTypeFilter = roomTypeInput.value.trim();
            const guestsAdultsFilter = guestsAdultInput.value.trim();
            const guestsChildrenFilter = guestsChildrenInput.value.trim();
            const checkedAmenities = Array.from(amenitiesCheckbox)
                .filter(cb => cb.checked).map(cb => cb.value);
            const allCards = roomGrid.querySelectorAll(".room-card");

            // if no input for filters, usual room grid will display
            if (!floorNum && !statusFilter && !roomTypeFilter && !beddingFilter && !guestsAdultsFilter && checkedAmenities.length === 0) {
                allCards.forEach((card) => {
                    card.style.display = "";
                });
                noRoomsMessage.style.display = "none";
                return;
            }

            const params = new URLSearchParams();
            if (floorNum) params.append("floorNum", floorNum);
            if (statusFilter) params.append("statusFilter", statusFilter);
            if (roomTypeFilter) params.append("roomTypeFilter", roomTypeFilter);
            if (beddingFilter) params.append("beddingFilter", beddingFilter);
            if (guestsAdultsFilter !== "") params.append("guestsAdultsFilter", guestsAdultsFilter);
            if (guestsChildrenFilter !== "") params.append("guestsChildrenFilter", guestsChildrenFilter);

            // passing only only the checked amentiies
            params.delete("amenitiesFilter");
            checkedAmenities.forEach(id => {
                params.append("amenitiesFilter", id);
            });

            fetch('<c:url value="/room/all" />?' + params.toString(),
                {
                    headers: {
                        "X-Requested-With": "XMLHttpRequest"
                    }
                })
                .then(res => {
                    if (!res.ok) throw new Error("Search failed");
                    return res.json();
                })
                .then(rooms => {
                    allCards.forEach((card) => {
                        card.style.display = "none";
                    });
                    noRoomsMessage.style.display = "none";

                    if (!rooms || rooms.length === 0) {
                        noRoomsMessage.style.display = "block";
                        return;  // if no rooms, no rooms avaialble msg displayed
                    }

                    rooms.forEach(room => {
                        console.log(room);
                        const card = roomGrid.querySelector(".room-card[data-room-id='" + room.roomId + "']");
                        if (card) card.style.display = "";
                    });
                })
                .catch(err => console.error(err));
    }

    // this debounce is for the search function
        // to ensure that the request is called only every 300 miliseconds
    const debounceSearchTimer = () => {
            clearTimeout(debounceTimer);
            debounceTimer = setTimeout(performSearch, 300);
    };

        // floor filter
        searchInput.addEventListener("input", debounceSearchTimer);

      // status filter
        statusInput.addEventListener("change", debounceSearchTimer);

        // bedding input filter
        beddingInput.addEventListener("change", debounceSearchTimer);

         // room type input filter
        roomTypeInput.addEventListener("change", debounceSearchTimer);

    // number of adults and guests
    guestsAdultInput.addEventListener("input", debounceSearchTimer);

        guestsChildrenInput.addEventListener("input", debounceSearchTimer);

        // amentiies filter input
        amenitiesCheckbox.forEach(cb => cb.addEventListener("change", debounceSearchTimer));


    })

</script>




</body>
</html>
