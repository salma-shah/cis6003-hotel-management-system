<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>GeeksforGeeks</title>

</head>
<body>
<form action="<c:url value='/email' />" method="post">
    <div align="center"> <h1>Enter the Credentials: </h1>
        <input type="hidden" name="action" value="add">

        <label for="sID">Sender ID :</label> <br>
        <input id="sID" type="text" name="id"> <br>

        <label for="sMail">Sender Email :</label> <br>
        <input id="sMail" type="email" name="emailSender"> <br>

        <label for="pw">Password :</label> <br>
        <input id="pw" type="password" name="password"> <br>

        <label for="rMail">Receiver Email :</label> <br>
        <input id="rMail" type="email" name="emailReceiver"> <br>

        <label for="sub">Subject :</label> <br>
        <input id="sub" type="text" name="subject"> <br>

        <label for="message">Message :</label> <br>
        <input id="message" type="text" name="message"><br>
        <input type="submit" value="Send">

    </div>
</form>
</body>
</html>