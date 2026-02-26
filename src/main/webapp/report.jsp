<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Generate Reports</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
</head>
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
        width: 75%;
    }

    .header-row {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 25px;
    }

    .enter-btn {
        background: #4682A9 !important;
        margin-top: 20px;
        border-radius: 20px;
        padding: 6px 25px;
        font-size: 14px;
        color: white;
    }

    .enter-btn:hover {
        background: #0f3c57 !important;
        color: white;
    }

    .header-row h2 {
        margin: 0 0 20px;
        font-weight: 600;
        color: #2c3e50;
    }

    .report-filter select {
        padding: 10px;
        border-radius: 8px;
        border: 1px solid #ddd;
    }

</style>
<body>
<div class="dashboard-layout">
    <jsp:include page="sidebar.jsp"/>

    <main class="dashboard-content">
        <div class="content-card">
            <div class="header-row">
                <h2>Generate Reports</h2>
            </div>

            <div class="content-card">
                <h5>Select Report</h5>

                <br>

                <form method="post" action="<c:url value='/report/generate' />">
                <div class="report-filter">
                <label for="reportType">Report Type:</label>
                <select id="reportType" name="reportType">
                    <option value="weekly">Weekly</option>
                    <option value="monthly">Monthly</option>
                </select>
                </div>
                <br>
                <div>
            <label for="startDate">Start Date:</label>
            <input type="date" name="startDate">
                </div>
                <br>
                <div>
            <label for="endDate">End Date:</label>
            <input type="date" name="endDate">
                </div>
                <br>
                <div>
            <button class="enter-btn" type="submit">Generate Report</button>
                </div>
                </form>
            </div>

</div>

    </main>
</div>
</body>
</html>
