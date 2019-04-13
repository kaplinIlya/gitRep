<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Car</title>
</head>
<body>
<form:form action="addCar" method="POST"  modelAttribute="carJSP">
    Firm:   <form:input path="firm"/> <br/>
    Model:  <form:input path="model"/> <br/>
    Color:  <form:input path="color"/> <br/>
    Power:  <form:input path="power"/> <br/>
    <form:button>Submit</form:button>
</form:form>
</body>
</html>
