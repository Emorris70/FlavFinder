<jsp:include page="taglib.jsp"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<jsp:include page="head.jsp"/>
<body>
<header>
    <jsp:include page="flav-logo.jsp"/>
</header>
<main class="container override-animation">
    <h1 class="m-h">Check your email</h1>
    <p class="p-deco s-p">Enter the code that we sent to the email address. The code expires in 15 minutes.</p>
    <div class="errorMsg">
        <c:if test="${not empty sessionScope.error}">
            <p class="error-msg">${sessionScope.error}</p>
            <c:remove var="error" scope="session"/>
        </c:if>
    </div>
    <form method="POST" action="auth" id="auth-exists">
        <div class="con-wrapper">
            <label for="v-code">Verification code</label>
            <div class="input-wrapper">
                <input type="text" name="v-code" id="v-code" placeholder="Enter code" />
            </div>
        </div>
        <div class="btn-container sw-dir">
            <button type="submit"
                    name="action"
                    value="confirm"
                    class="btn-submit">Continue</button>
            <!--    Ensure this redirect back to the sign-up page.        -->
            <button type="button"
                    name="action"
                    value="back"
                    class="btn-submit back-btn">Back</button>
        </div>
    </form>
</main>
</body>
</html>
