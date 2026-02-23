<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <title>Staff Guidelines & Contact</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css"/>
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

        .header-row h2 {
            margin: 0 0 20px;
            font-weight: 600;
            color: #2c3e50;
        }

        .guidelines ul {
            list-style-type: disc;
            padding-left: 20px;
        }

        .contact-form label {
            font-weight: 600;
            margin-top: 10px;
        }

        .contact-form input,
        .contact-form textarea {
            border-radius: 8px;
            border: 1px solid #ddd;
            padding: 10px;
            width: 100%;
            margin-top: 5px;
        }

        .btn-submit {
            background: #4682A9;
            color: white;
            border-radius: 20px;
            padding: 8px 30px;
            margin-top: 15px;
        }

        .btn-submit:hover {
            background: #35658e;
        }

        .form-success {
            color: green;
            margin-top: 10px;
        }

        .form-error {
            color: red;
            margin-top: 10px;
        }

    </style>
</head>
<body>
<div class="dashboard-layout">
    <jsp:include page="sidebar.jsp"/>

    <main class="dashboard-content">
        <div class="content-card guidelines">
            <div class="header-row">
                <h2><i class="bi bi-question-circle"></i> Staff Guidelines</h2>
            </div>
            <div class="accordion">
                <!-- reservations -->
                <div class="accordion-step">
                    <h4><i class="bi bi-bookmark-plus"></i> Reservations</h4>
                    <div class="accordion-step-content">
                        <ul>
                            <li>Ensure the reservation's number is valid and unique.</li>
                            <li>View all reservations from the <b>Reservations</b> menu. Use search filters for reservation number, status, or date ranges.</li>
                            <li>Create new reservations using the <i>+ Make Reservation</i> button. Ensure guest details, room selection, and check-in/check-out dates are accurate.</li>
                            <li>Updating a reservation: Click <b>Update</b> in the Actions column. Only change fields that are allowed (check-out dates, or payment status).</li>
                            <li>Cancelling a reservation: Only do this if a guest cancels or fails to pay. Make sure to update the payment status accordingly.</li>
                        </ul>
                    </div>
                </div>

                <!-- guests -->
                <div class="accordion-step">
                    <h4><i class="bi bi-person-raised-hand"></i> Guests</h4>
                    <div class="accordion-step-content">
                        <ul>
                            <li>Ensure the guest's registration number is valid and unique.</li>
                            <li>Register a new guest using the <i>+ Register Guest</i> button. All guest information must be accurate: full name, ID/passport number, contact information, and address.</li>
                            <li>Verify guest details before check-in to prevent booking errors.</li>
                            <li>Use the "View" action to see a guest's profile or history of reservations.</li>
                            <li>Ensure guest privacy: Do not share sensitive data without proper authorization.</li>
                        </ul>
                    </div>
                </div>

                <!-- payment section -->
                <div class="accordion-step">
                    <h4><i class="bi bi-cash-coin"></i> Payments</h4>
                    <div class="accordion-step-content">
                        <ul>
                            <li>Always check the payment status before confirming check-in.</li>
                            <li>Payment statuses include: Pending, Paid.</li>
                            <li>Always double-check that the generated bill matches the payment details.</li>
                            <li>Share the bill with the guest without fail.</li>
                            <li>Keep track of partial payments and ensure they are reflected in the reservation system.</li>
                            <li>For issues with payments, contact the finance department.</li>
                        </ul>
                    </div>
                </div>

                <!-- room -->
                <div class="accordion-step">
                    <h4><i class="bi bi-segmented-nav"></i> Room Management</h4>
                    <div class="accordion-step-content">
                        <ul>
                            <li>Each room has a type (Single, Deluxe, Suite, etc.) and a unique ID. Always confirm the room type matches the guest’s booking.</li>
                            <li>Check room availability before creating or updating a reservation.</li>
                            <li>Maintain room status: Available, Unavailable, or Under Maintenance. Update after check-in/check-out or maintenance work.</li>
                            <li>Report any discrepancies, damages, or issues to the maintenance or admin team immediately.</li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>

        <!--this is a contact form -->
        <div class="content-card contact-form">
            <div class="header-row">
                <h2>Contact HR</h2>
            </div>
            <h4>If you have any questions or inquiries, please write them here <i class="bi bi-tsunami"></i></h4>
            <form method="post" action="<c:url value='/help/contact-form' />">
                <label for="message">Message</label>
                <textarea id="message" name="message" rows="5" placeholder="Write your message here..." required></textarea>
                <br>
                <br>
                <h5>Our team will get back to you soon!</h5>
                <br>
                <br>
                <button type="submit" class="btn-submit">Send Message</button>
                <div id="formFeedback"></div>
            </form>
        </div>
    </main>
</div>
</body>
</html>
