<%@include file="WEB-INF/jsp/includes/taglib.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<jsp:include page="head.jsp"/>
<body>
<jsp:include page="header.jsp"/>
<main class="container override-animation">
    <h1 class="m-h">Reset your Password</h1>
    <p class="p-deco s-p-2">Enter the code sent to your email and choose a new password.</p>
    <c:if test="${not empty error}">
        <div class="errorMsg">
            <p class="error-msg">${error}</p>
            <c:remove var="error" scope="session"/>
        </div>
    </c:if>
    <form method="POST" action="${pageContext.request.contextPath}/auth" id="auth-exists">
        <div class="con-wrapper">
            <label for="v-code">Verification code</label>
            <div class="input-wrapper">
                <input type="text" name="v-code" id="v-code" placeholder="Enter code" value="${param['v-code']}"/>
            </div>
        </div>
        <div class="con-wrapper">
            <label for="password">New password</label>
            <div class="input-wrapper">
                <img src="${pageContext.request.contextPath}/images/lock-p.png" alt="lock icon" />
                <input type="password" name="password" id="password" placeholder="New password" required/>
            </div>
            <div class="pass-requirements">
                <ul class="req-list">
                    <li class="req-item" id="req-length">
                        <span class="req-dot"></span>
                        <span class="req-text">At least 8 characters</span>
                    </li>
                    <li class="req-item" id="req-uppercase">
                        <span class="req-dot"></span>
                        <span class="req-text">At least 1 uppercase letter</span>
                    </li>
                    <li class="req-item" id="req-lowercase">
                        <span class="req-dot"></span>
                        <span class="req-text">At least 1 lowercase letter</span>
                    </li>
                    <li class="req-item" id="req-number">
                        <span class="req-dot"></span>
                        <span class="req-text">At least 1 number</span>
                    </li>
                    <li class="req-item" id="req-special">
                        <span class="req-dot"></span>
                        <span class="req-text">At least 1 special character</span>
                    </li>
                </ul>
            </div>
        </div>
        <div class="btn-container sw-dir">
            <button type="submit" name="action" value="confirmForgotPassword" class="btn-submit">
                Confirm
            </button>
            <a href="auth?action=reset-pass" class="btn-submit back-btn">Back</a>
        </div>
    </form>
</main>
<script src="${pageContext.request.contextPath}/js/signup.js" defer></script>
</body>
</html>