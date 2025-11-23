<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Test Page</title>
</head>
<body>
    <h2>Test Page</h2>
    <p>This is a test page to check if JSP is working.</p>
    
    <h3>Session Information:</h3>
    <p>User in session: ${sessionScope.user}</p>
    <c:if test="${sessionScope.user != null}">
        <p>Username: ${sessionScope.user.username}</p>
        <p>Role: ${sessionScope.user.role}</p>
    </c:if>
    
    <h3>Request Information:</h3>
    <p>Context Path: <%=request.getContextPath()%></p>
    <p>Request URI: <%=request.getRequestURI()%></p>
</body>
</html>