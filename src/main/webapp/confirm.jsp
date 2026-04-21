<%@include file="WEB-INF/jsp/includes/taglib.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<jsp:include page="head.jsp"/>
<body>
<jsp:include page="header.jsp"/>
<main class="container override-animation">
    <h1 class="m-h">Check your email</h1>
    <p class="p-deco s-p">Enter the code that we sent to the email address. The code expires in 15 minutes.</p>
    <c:if test="${not empty error}">
        <div class="errorMsg">
            <p class="error-msg">${error}</p>
        </div>
        <c:remove var="error" scope="session"/>
    </c:if>
    <form method="POST" action="${pageContext.request.contextPath}/auth" id="auth-exists">
        <div class="con-wrapper">
            <label for="v-code">Verification code</label>
            <div class="input-wrapper">
                <input type="text" name="v-code" id="v-code" placeholder="Enter code" value="${param['v-code']}" />
            </div>
        </div>
        <div class="btn-container sw-dir">
            <button type="submit"
                    name="action"
                    value="confirm"
                    class="btn-submit">Continue</button>
            <a href="auth?action=sign-up" class="btn-submit back-btn">Back</a>
        </div>
    </form>
</main>
</body>
</html>
