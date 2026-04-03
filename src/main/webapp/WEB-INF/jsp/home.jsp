<jsp:include page="../../taglib.jsp"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Home - FlavFinder</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/homePage.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
    <script src="${pageContext.request.contextPath}/js/script.js"></script>
</head>
<body>
<jsp:include page="header-home.jsp"/>
<main>
    <jsp:include page="locationPopUp.jsp"/>
    <a href="${pageContext.request.contextPath}/logout">Logout</a>
</main>
<footer></footer>
</body>
</html>