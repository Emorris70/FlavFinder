<jsp:include page="../../taglib.jsp"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
<%--  unitilize includes for this head portion for home.jsp  --%>
    <title>Home - FlavFinder</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/homePage.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body>
<jsp:include page="header-home.jsp"/>
<main>
    <jsp:include page="locationPopUp.jsp"/>
</main>
<footer></footer>
<script>
    const contextPath = "${pageContext.request.contextPath}";
</script>
<script src="${pageContext.request.contextPath}/js/script.js"></script>
</body>
</html>