<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
    <title>Create User Account</title>

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

        .form-success {
            margin-top: 12px;
            padding: 10px 14px;
            border-radius: 6px;
            background-color: #dadfce;
            color: #22a10f;
            border: 1px solid #6c8e50;
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
            <i class="bi bi-person-plus me-2"></i>Create New User Account
        </h4>

        <form id="registerForm" action="<c:url value='/user/register' />" method="post">
            <div class="form-error d-none" id="errorMsg"></div>
            <div class="form-success d-none" id="successMsg"></div>
            <div class="form-error d-none" id="systemError"></div>
            <br>
            <div class="mb-3">
                <label class="form-label">Username</label>
                <input type="text" name="username" id="username"class="form-control" onblur="checkAvailability()" required>
                <div class="form-error d-none" id="usernameError">Username already taken.</div>
            </div>

            <div class="mb-3">
                <label class="form-label">Password</label>
                <input type="password" name="password" id="password" class="form-control" required>
                <div class="form-error d-none" id="passwordError">Password has to be at least 8 characters</div>
            </div>

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label class="form-label">First Name</label>
                    <input type="text" name="firstName" id="firstName" class="form-control" required>
                    <div class="form-error d-none" id="firstNameError">Please enter first name</div>
                </div>

                <div class="col-md-6 mb-3">
                    <label class="form-label">Last Name</label>
                    <input type="text" name="lastName" id="lastName" class="form-control" required>
                    <div class="form-error d-none" id="lastNameError">Please enter the last name</div>
                </div>
            </div>

            <div class="mb-3">
                <label class="form-label">Email</label>
                <input type="email" id="email" name="email" class="form-control" onblur="checkAvailability()" required>
                <div class="form-error d-none" id="emailError">Email already taken.</div>
            </div>

            <div class="mb-3">
                <label class="form-label">Contact Number</label>
                <input type="text" id="contactNumber" name="contactNumber" class="form-control" required>
                <div class="form-error d-none" id="contactNumberError">Please enter contact number</div>
            </div>

            <div class="mb-3">
                <label class="form-label">Address</label>
                <input type="text" id="address" name="address" class="form-control" required>
                <div class="form-error d-none" id="addressError">Please enter address</div>
            </div>

            <div class="mb-4">
                <label class="form-label">Role</label>
                <select name="role" id="role" class="form-select" required>
                    <option value="">Select role</option>
                    <option value="Manager">Manager</option>
                    <option value="User">Receptionist</option>
                </select>
                <div class="form-error d-none" id="roleError">Please select a role</div>
            </div>

            <div class="d-flex justify-content-end">
                <button type="submit" class="btn enter-btn">
                    Create Account
                </button>
            </div>
        </form>
    </div>
</div>

<script>
    let usernameValid = false;
    let emailValid = false;

    document.addEventListener('DOMContentLoaded', function () {
        const passwordValue = document.getElementById('password');
        passwordValue.addEventListener("input", function() {
            const value = passwordValue.value.trim();
            const passwordError = document.getElementById("passwordError");
            const regex = /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$/;

            if (!value){
                passwordError.innerHTML = "Password is required.";
                passwordError.classList.remove("d-none");
            } else {
                passwordError.classList.add("d-none");
            }


            if (!regex.test(value)) {
                passwordError.innerHTML = "Password must be at least 8 characters and include letters and numbers.";
                passwordError.classList.remove("d-none");
            } else {
                passwordError.classList.add("d-none");
            }
        });
    })

    // this is frontend validation
    document.getElementById("registerForm").addEventListener("submit", function(e) {
        let isValid = true;
        const firstName = document.getElementById('firstName').value.trim();
        const lastName = document.getElementById('lastName').value.trim();
        const username = document.getElementById('username').value.trim();
        const email = document.getElementById('email').value.trim();
        const password = document.getElementById('password').value.trim();
        const address = document.getElementById('address').value.trim();
        const contactNumber = document.getElementById('contactNumber').value.trim();

        if (!firstName || !lastName || !username || !email || !password || !address || !contactNumber) {
        isValid = false;
        }

        if (!isValid) {
        e.preventDefault();
        const errorBox = document.getElementById("errorMsg");
        errorBox.innerHTML = "Please fill all fields.";
        errorBox.classList.remove("d-none");
        }

        if (!usernameValid || !emailValid) {
            e.preventDefault();
            alert("Please fix the errors before submitting");
        }

    });

    document.getElementById("username").addEventListener("blur", checkAvailability);
    document.getElementById("email").addEventListener("blur", checkAvailability);
    async function checkAvailability() {
        const params = new URLSearchParams();
        params.append("username", document.getElementById("username").value.trim());
        params.append("email", document.getElementById("email").value.trim());

        const url = '<c:url value="/user/check-duplicate" />?' + params.toString();
        const response = await fetch(url);
        const data = await response.json();

        usernameValid = !data.usernameExists;
        const usernameErrorBox = document.getElementById("usernameError");
        if (usernameValid) {usernameErrorBox.classList.add("d-none");}
        else {
            usernameErrorBox.innerText = "Username already taken";
            usernameErrorBox.classList.remove("d-none");
            }

            emailValid = !data.emailExists;
            const emailErrorBox = document.getElementById("emailError");
            if (emailValid) {
                emailErrorBox.classList.add("d-none");
            } else {
                emailErrorBox.innerText = "Email is already taken";
                emailErrorBox.classList.remove("d-none");
            }
    }


    // this checks based on the servlet response
    const params = new URLSearchParams(window.location.search);
    const error = params.get('error');
    const success = params.get('success');

    if (error  === "invalid_input") {
    document.getElementById("errorMsg").innerHTML = "Please fill all the fields.";
    document.getElementById("errorMsg").classList.remove('d-none');
    }

    if (success  === "true") {
    document.getElementById("successMsg").innerHTML = "The user account was successfully created and email has been sent!";
    document.getElementById("successMsg").classList.remove('d-none');
    }

    if (error  === "system_error") {
    document.getElementById("systemError").innerHTML = "Something went wrong. The user account could not be created.";
    document.getElementById("systemError").classList.remove('d-none');
    }

    if (error  === "weak_password") {
        document.getElementById("passportError").innerHTML = "Password must be at least 8 characters and include letters and numbers.";
        document.getElementById("passwordError").classList.remove('d-none');
    }

    if (error  === "invalid_email") {
        alert("Please enter a valid email address");
    }

</script>

</body>
</html>
