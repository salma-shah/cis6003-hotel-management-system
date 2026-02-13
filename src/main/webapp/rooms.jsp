<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <title>Manage Rooms</title>
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

    .search-filter {
        display: flex;
        gap: 15px;
        margin-bottom: 20px;
    }

    .search-filter input,
    .search-filter select {
        width: 250px;
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

            <div class="search-filter">
                <input type="number" id="searchInput" placeholder="Search by floor number...">

<%--                // these are the filters: --%>
<%--                // status filter --%>
                <select id="statusFilter" name="status">
                    <option value="">All Status</option>
                    <option value="Available">Available</option>
                    <option value="Unavailable">Unavailable</option>
                </select>
            </div>
<%--    // amenity filters--%>
            <div class="amenities-filter" name="amenities">
                <label><input type="checkbox" class="amenity" value="WiFi"> WiFi</label>
                <label><input type="checkbox" class="amenity" value="Pool">  Pool</label>
                <label><input type="checkbox" class="amenity" value="AC"> AC</label>
                <label><input type="checkbox" class="amenity" value="TV"> TV</label>
                <label><input type="checkbox" class="amenity" value="Balcony"> Balcony</label>
            </div>
    <br>
    <br>

            <c:if test="${empty rooms}">
                <p>No rooms available.</p>
            </c:if>

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
                            <div class="room-meta">Category: ${room.roomType}</div>
                            <div class="room-meta">Floor: ${room.floorNum}</div>
                            <div class="room-meta room-status
                               ${room.roomStatus == 'Available' ? 'status-available' :
                                 room.roomStatus == 'Unavailable' ? 'status-occupied' : 'status-maintenance'}">
                                Status: ${room.roomStatus}
                            </div>
                        </div>

                        <div class="room-footer">
                            <a href="<c:url value='/room/get?id=${room.roomId}' />"
                               class="btn enter-btn">
                                View Details
                            </a>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </section>
    </main>
</div>

<script>
    document.addEventListener("DOMContentLoaded", function() {
        const searchInput = document.getElementById("searchInput");
        const roomGrid = document.getElementById("roomGrid");
        const noRoomsMessage = document.getElementById("noRoomsMessage");
        let debounceTimer;

        searchInput.addEventListener("input", () => {
            clearTimeout(debounceTimer);
            debounceTimer = setTimeout(() => {
                const floorNum = searchInput.value.trim();
                const allCards = roomGrid.querySelectorAll(".room-card");

                // if no input, usual room grid will display
                if (!floorNum)
                {
                    allCards.forEach((card) => {card.style.display = "";});
                    noRoomsMessage.style.display = "none";
                    return;
                }

                fetch('<c:url value="/room/all" />?floorNum=' + encodeURIComponent(floorNum),
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
                        const allCards = roomGrid.querySelectorAll(".room-card");
                        allCards.forEach((card) => {card.style.display = "none";});
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
                    .catch(err => {
                        console.error(err);
                    });
            }, 300);
        });
    });


    // const statusFilter = document.getElementById("statusFilter");
    // const amenityCheckboxes = document.querySelectorAll(".amenity");
    // const roomCards = document.querySelectorAll(".room-card");
    //
    //
    // function getSelectedAmenities() {
    //     return Array.from(amenityCheckboxes)
    //         .filter(cb => cb.checked)
    //         .map(cb => cb.value.toLowerCase());
    // }


    // function filterRooms() {
    //     const searchText = searchInput.value.toLowerCase();
    //     const status = statusFilter.value;
    //     const selectedAmenities = getSelectedAmenities();
    //
    //
    //     roomCards.forEach(card => {
    //         // const name = card.dataset.name.toLowerCase();
    //        //  const category = card.dataset.category.toLowerCase();
    //         const roomStatus = card.dataset.status;
    //        // const amenities = card.dataset.amenities
    //             .toLowerCase()
    //             .split(",");
    //
    //
    //         const matchesSearch =
    //           //  name.includes(searchText) ||
    //             category.includes(searchText);
    //
    //
    //         const matchesStatus =
    //             !status || roomStatus === status;
    //
    //
    //         // AND logic: room must contain ALL selected amenities
    //         const matchesAmenities =
    //             selectedAmenities.length === 0 ||
    //             selectedAmenities.every(a => amenities.includes(a));
    //
    //
    //         card.style.display =
    //             (matchesSearch && matchesStatus && matchesAmenities)
    //                 ? "block"
    //                 : "none";
    //     });
    // }
    //
    //
    // searchInput.addEventListener("keyup", filterRooms);
    // statusFilter.addEventListener("change", filterRooms);
    // amenityCheckboxes.forEach(cb =>
    //     cb.addEventListener("change", filterRooms)
    // );

</script>




</body>
</html>
