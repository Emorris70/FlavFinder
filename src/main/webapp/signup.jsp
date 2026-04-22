<%@include file="WEB-INF/jsp/includes/taglib.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<jsp:include page="head.jsp"/>
<body>
<jsp:include page="header.jsp"/>
<main class="container">
    <h1 class="m-h">Welcome to FlavFinder!</h1>
    <c:if test="${not empty error}">
        <div class="errorMsg">
            <p class="error-msg">${error}</p>
        </div>
        <c:remove var="error" scope="session"/>
    </c:if>
    <form method="POST" action="${pageContext.request.contextPath}/auth" id="auth-form">
        <div class="con-wrapper">
            <label for="first_name">first Name</label>
            <div class="input-wrapper">
                <img src="images/user.png" alt="avatar logo" />
                <input
                        type="text"
                        name="first_name"
                        id="first_name"
                        placeholder="First name"
                        value="${param.first_name}"
                />
            </div>
        </div>
        <div class="con-wrapper">
            <label for="email">email</label>
            <div class="input-wrapper">
                <input
                        type="email"
                        name="email"
                        id="email"
                        placeholder="name@host.com"
                        value="${param.email}"
                />
                <img src="images/mail.png" alt="mail icon" />
            </div>
        </div>
        <div class="con-wrapper">
            <label for="password">password</label>
            <div class="input-wrapper">
                <img src="images/lock-p.png" alt="lock icon" />
                <input
                        type="password"
                        name="password"
                        id="password"
                        placeholder="Password"
                        required
                />
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
        <p class="dir-deco">
            Already have an account?<a href="auth?action=login">Login</a>
        </p>
        <div class="btn-container">
            <button type="submit"
                    name="action"
                    value="signUp"
                    class="btn-submit" >
                Sign Up
            </button>
        </div>
    </form>
</main>
<script src="${pageContext.request.contextPath}/js/signup.js" defer></script>
</body>
</html>
