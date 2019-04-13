
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<html>
<head>
    <title>Database users</title>
</head>
<body>
<h1>Cars:</h1>
<c:if test="${not empty cars}">
    <c:forEach items="${cars}" var="line">
        <li>${line} + <a href="del?id=${line.id}">Delete</a></li>
    </c:forEach>
</c:if>
</body>
</html>
