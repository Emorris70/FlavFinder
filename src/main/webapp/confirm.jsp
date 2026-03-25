<jsp:include page="taglib.jsp"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<jsp:include page="head.jsp"/>
<%-- TODO ensure to set the title for this page--%>
<body>
<header>
    <jsp:include page="flav-logo.jsp"/>
</header>
<main class="container override-animation">
    <h1 class="m-h">Check your email</h1>
    <p class="p-deco s-p">Enter the code that we sent to the email address. The code expires in 15 minutes.</p>
    <!--  might need to change this  -->
<%--  TODO add error display message  --%>
    <form method="POST" action="auth" id="auth-exists">
        <div class="con-wrapper">
            <label for="v-c">Verification code</label>
            <div class="input-wrapper">
                <input type="text" name="v-c" id="v-c" placeholder="Enter code" />
            </div>
        </div>
        <div class="btn-container sw-dir">
            <button type="submit"
                    name="action"
                    value="confirm"
                    class="btn-submit">Continue</button>
            <!--    Ensure this redirect back to the sign-up page.        -->
            <button type="button" class="btn-submit back-btn">Back</button>
        </div>
    </form>
</main>
</body>
</html>
