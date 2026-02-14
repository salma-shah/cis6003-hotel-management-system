<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page isELIgnored="false"%>
<title>User Guidelines</title>
<head>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css"/>
</head>
<h1>Contact Ocean View Hotel Support Team</h1>

<form method="post" action="<c:url value='/help/contact-form' />">

    <label for="message">Subject</label>
    <textarea id="message" name="message" placeholder="Write your message here..." required></textarea>

    Our team will get back to you soon!

    <button type="submit">Send Message</button>

</form>