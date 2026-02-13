<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
            <div class="mb-3">
                <label class="form-label">Username</label>
                <input type="text" name="username" id="username"class="form-control" required>
                <div class="form-error d-none" id="usernameError">Please enter a username</div>
            </div>

            <div class="mb-3">
                <label class="form-label">Password</label>
                <input type="password" name="password" id="password" class="form-control" required>
                <div class="form-error d-none" id="passwordError">Please enter a password</div>
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
                    <div class="form-error d-none" id="lastName">Please enter the last name</div>
                </div>
            </div>

            <div class="mb-3">
                <label class="form-label">Email</label>
                <input type="email" id="email" name="email" class="form-control" required>
                <div class="form-error d-none" id="emailError">Please enter a valid email</div>
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
<%--                    <option value="STAFF">Staff</option>--%>
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

</body>
</html>
