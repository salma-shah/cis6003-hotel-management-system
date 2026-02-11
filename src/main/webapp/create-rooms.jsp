<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Create Room</title>

    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">

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
            font-size: 14px;
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

        <h4 class="form-title">
            <i class="bi bi-house-add"></i>  Create New Room
        </h4>
        <form id="registerForm" action="<c:url value='/room/create' />" method="post">
        <div class="mb-3">
            <label class="form-label">Room Type</label>
            <select name="type" id="type" class="form-select" required>
                <option value="">Select room type</option>
                <option value="Standard">Standard</option>
                <option value="Deluxe">Deluxe</option>
                <option value="Suite">Suite</option>
            </select>
            <div class="form-error d-none" id="typeError">Please enter the room type.</div>
        </div>

            <div class="mb-3">
                <label class="form-label">Description</label>
                <input type="text" name="desc" id="desc" class="form-control" required>
                <div class="form-error d-none" id="descError">Please enter a descrption for the room.</div>
            </div>

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label class="form-label">Price Per Night</label>
                    <input type="text" name="pricePerNight" id="pricePerNight" class="form-control" required>
                    <div class="form-error d-none" id="priceError">Please enter the price per night</div>
                </div>

                <div class="mb-3">
                    <label class="form-label">Bedding</label>
                    <select name="bedding" id="bedding" class="form-select" required>
                        <option value="">Select bedding type</option>
                        <option value="Single">Single</option>
                        <option value="Twin">Twin</option>
                        <option value="Double">Double</option>
                        <option value="King">King</option>
                    </select>
                </div>

                <div class="mb-3">
                    <label class="form-label">Status</label>
                    <select name="status" id="status" class="form-select" required>
                        <option value="">Select status</option>
                        <option value="Available">Available</option>
                        <option value="Unavailable">Unavailable</option>
                    </select>
                    <div class="form-error d-none" id="statusError">Please select the room status.</div>
                </div>

                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label class="form-label">Max Occupancy</label>
                        <input type="number" name="maxOccupancy" id="maxOccupancy" class="form-control" required>
                        <div class="form-error d-none" id="maxOccupancyError">Please enter maximum occupancy.</div>
                    </div>

                    <div class="col-md-6 mb-3">
                        <label class="form-label">Floor Number</label>
                        <input type="number" name="floorNum" id="floorNum" class="form-control" required>
                        <div class="form-error d-none" id="floorNumError">Please enter the floor number.</div>
                    </div>
                </div>

            <div class="d-flex justify-content-end">
                <button type="submit" class="btn enter-btn">
                    Create Room
                </button>
            </div>
            </div>
        </form>
    </div>
</div>

</body>
</html>
