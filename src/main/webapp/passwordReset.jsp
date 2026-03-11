<jsp:include page="taglib.jsp"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<jsp:include page="head.jsp"/>
<body>
<header>
    <jsp:include page="flav-logo.jsp"/>
</header>
<main class="container">
    <h1 class="m-h">Forgot Password?</h1>
    <p class="p-deco">Enter your email to receive a rest link</p>
    <form method="POST" action="auth" id="auth-form">
        <div class="con-wrapper">
            <label for="email">email</label>
            <div class="input-wrapper">
                <input
                        type="email"
                        name="email"
                        id="email"
                        placeholder="name@host.com"
                />
                <img src="images/mail.png" alt="mail icon" />
            </div>
        </div>
        <div class="btn-container">
            <button type="submit" class="btn-submit" id="btn-submit">
                Reset Password
            </button>
        </div>
        <p class="dir-deco center-i">
            Back to<a href="auth?action=login">Login</a>
        </p>
    </form>
</main>
</body>
</html>
