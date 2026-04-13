<%@ include file= "includes/taglib.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<jsp:include page="includes/app-head.jsp"/>
<body>
<jsp:include page="includes/header-home.jsp"/>
<main>
    <jsp:include page="includes/locationPopUp.jsp"/>
</main>
<footer></footer>
<script>
    const contextPath = "${pageContext.request.contextPath}";
</script>
<script src="${pageContext.request.contextPath}/js/script.js"></script>
</body>
</html>