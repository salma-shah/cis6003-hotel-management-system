<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Create User Account</title>

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
            <i class="bi bi-person-plus me-2"></i>Create New User Account
        </h4>

        <c:if test="${not empty param.error}">
            <div class="alert alert-danger">${param.error}</div>
        </c:if>

        <form action="<c:url value='/user/register' />" method="post">
            <div class="mb-3">
                <label class="form-label">Username</label>
                <input type="text" name="username" class="form-control" required>
            </div>

            <div class="mb-3">
                <label class="form-label">Password</label>
                <input type="password" name="password" class="form-control" required>
            </div>

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label class="form-label">First Name</label>
                    <input type="text" name="firstName" class="form-control" required>
                </div>

                <div class="col-md-6 mb-3">
                    <label class="form-label">Last Name</label>
                    <input type="text" name="lastName" class="form-control" required>
                </div>
            </div>

            <div class="mb-3">
                <label class="form-label">Email</label>
                <input type="email" name="email" class="form-control" required>
            </div>

            <div class="mb-3">
                <label class="form-label">Contact Number</label>
                <input type="text" name="contactNumber" class="form-control" required>
            </div>

            <div class="mb-3">
                <label class="form-label">Address</label>
                <input type="text" name="address" class="form-control" required>
            </div>


            <div class="mb-4">
                <label class="form-label">Role</label>
                <select name="role" class="form-select" required>
                    <option value="">Select role</option>
                    <option value="Manager">Manager</option>
                    <option value="User">Receptionist</option>
<%--                    <option value="STAFF">Staff</option>--%>
                </select>
            </div>

            <div class="d-flex justify-content-end">
                <button type="submit" class="btn enter-btn">
                    Create Account
                </button>
            </div>
        </form>

        <%-- ensuring input is valid  --%>

        <c:if test="${not empty param.error}">
            <c:choose>
                <c:when test="${param.error == 'empty_fields'}">
                    <p class="form-error"> Please fill in all the fields.</p>
                </c:when>
            </c:choose>
            <c:choose>
                <c:when test="${param.error == 'username_used'}">
                    <p class="form-error"> This username is already in use.</p>
                </c:when>
            </c:choose>
            <c:choose>
                <c:when test="${param.error == 'email_used'}">
                    <p class="form-error"> This email is already in use.</p>
                </c:when>
            </c:choose>
            <%--    <c:choose>--%>
            <%--        <c:when test="${param.error == 'user_not_found'}">--%>
            <%--            <p class="form-error"> User could not be found.</p>--%>
            <%--        </c:when>--%>
            <%--    </c:choose>--%>
            <c:choose>
                <c:when test="${param.error == 'system_error'}">
                    <p class="form-error"> Something went wrong. We apologize.</p>
                </c:when>
            </c:choose>
            <c:otherwise>
                <p class="form-error">The new user account could not be created. Please try again.</p>
            </c:otherwise>
        </c:if>
        <c:if test="${not empty param.success}">
        <p class="form-error">The account was registered successfully! User may now log in into their account.</p>
        </c:if>
    </div>
</div>

</body>
</html>
