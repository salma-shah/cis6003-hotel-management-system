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
            height: 100vh;
            background-color: #f5f7fa;
        }

        .dashboard-content {
            flex: 0.5;
            padding: 30px;
            margin-left: 240px;
        }

        .content-card {
            background: #fff;
            border-radius: 10px;
            padding: 30px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.05);
            max-width: 800px;
            margin: auto;
        }

        .form-title {
            font-weight: 600;
            margin-bottom: 25px;
            color: #4682A9;
        }

        .form-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
        }

        .form-group {
            display: flex;
            flex-direction: column;
        }

        .form-group label {
            margin-bottom: 13px;
            font-weight: 500;
        }

        .form-group input {
            margin-bottom: 15px;
            padding: 10px;
            border-radius: 6px;
            border: 1px solid #ccc;
            font-size: 14px;
        }

        .enter-btn {
            background: #4682A9 !important;
            margin-top: 20px;
            border-radius: 20px;
            padding: 6px 25px;
            font-size: 14px;
            color: white;
            border: none;
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

    <main class="dashboard-content">
        <section class="content-card">
            <h4 class="form-title">
                <i class="bi bi-person-vcard-fill"></i> Register Guest
            </h4>

            <form id="registerGuestForm" action="<c:url value='/guest/register' />" method="post">
                <div class="form-grid">

                    <div class="form-group">
                        <label for="registrationNumber">Registration Number</label>
                        <div class="d-flex gap-2">
                            <input type="text" id="registrationNumber" name="registrationNumber" class="form-control" placeholder="Enter registration number" required>

                            <button type="button" id="generateRegBtn" class="btn btn-outline-secondary">
                                Generate
                            </button>
                        </div>

                        <div class="error-message" id="regNumberError"></div>
                    </div>
                </div>

                    <div class="form-group">
                        <label for="firstName">First Name</label>
                        <input type="text" id="firstName" name="firstName" placeholder="Enter first name" required>
                        <div class="error-message" id="firstNameError"></div>
                    </div>

                    <div class="form-group">
                        <label for="lastName">Last Name</label>
                        <input type="text" id="lastName" name="lastName" placeholder="Enter last name" required>
                        <div class="error-message" id="lastNameError"></div>
                    </div>

                    <div class="form-group">
                        <label for="contactNumber">Contact Number</label>
                        <input type="tel" id="contactNumber" name="contactNumber" placeholder="Enter contact number" required>
                        <div class="error-message" id="contactError"></div>
                    </div>

                    <div class="form-group">
                        <label for="address">Address</label>
                        <input type="text" id="address" name="address" placeholder="Enter address" required>
                    </div>

                    <div class="form-group">
                        <label for="email">Email</label>
                        <input type="email" id="email" name="email" placeholder="Enter email" required>
                        <div class="error-message" id="emailError"></div>
                    </div>

                    <div class="form-group">
                        <label for="nic">NIC</label>
                        <input type="text" id="nic" name="nic" placeholder="Enter NIC" required>
                        <div class="error-message" id="nicError"></div>
                    </div>

                    <div class="form-group">
                        <label for="passportNumber">Passport Number</label>
                        <input type="text" id="passportNumber" name="passportNumber" placeholder="Enter passport number">
                        <div class="error-message" id="passportError"></div>
                    </div>

                    <div class="form-group">
                        <label for="dob">Date of Birth</label>
                        <input type="date" id="dob" name="dob" required>
                        <div class="error-message" id="dobError"></div>
                    </div>

                <div class="d-flex justify-content-end">
                    <button type="submit" class="btn enter-btn">Register Guest</button>
                </div>
            </form>
        </section>
    </main>
</div>

<script>
    document.addEventListener("DOMContentLoaded", () => {
        const form = document.getElementById("registerGuestForm");

        form.addEventListener("submit", (e) => {
            let valid = true;
            document.querySelectorAll(".error-message").forEach(el => el.textContent = "");

            const nic = document.getElementById("nic").value.trim();
            const passport = document.getElementById("passportNumber").value.trim();
            const dob = document.getElementById("dob").value;

            // nic validation
            if (!/^\d{9,12}[vV]?$/.test(nic)) {
                document.getElementById("nicError").textContent = "Invalid NIC number";
                valid = false;
            }

            // passport validation
            if (passport && !/^[A-Za-z0-9]{6,9}$/.test(passport)) {
                document.getElementById("passportError").textContent = "Invalid passport number";
                valid = false;
            }

            // dob valdiation
            if (dob) {
                const birthDate = new Date(dob);
                const today = new Date();
                let age = today.getFullYear() - birthDate.getFullYear();
                const monthDiff = today.getMonth() - birthDate.getMonth();
                if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
                    age--;
                }
                if (age < 18) {
                    document.getElementById("dobError").textContent = "Guest needs to be at least 18 years of age.";
                    valid = false;
                }
            }

            if (!valid) e.preventDefault();
        });

        document.getElementById("generateRegBtn")
            .addEventListener("click", function () {

                fetch("<c:url value='/generate?type=reg-num' />")
                    .then(response => response.text())
                    .then(data => {
                        document.getElementById("registrationNumber").value = data;
                    })
                    .catch(error => {
                        console.error("Error generating registration number:", error);
                    });
            });

    });
</script>
</body>
</html>
