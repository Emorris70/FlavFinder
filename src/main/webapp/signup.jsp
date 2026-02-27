<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Sign up - FlavFinder</title>
    <link rel="stylesheet" href="css/main.css" />
    <link rel="stylesheet" href="css/auth.css" />
</head>
<body>
<header>
    <div class="a-lc" role="img" aria-label="FlavFinder Logo">
        <h1 class="main-h1">
            FL
            <span class="img-c">
            <img
                    src="images/location.png"
                    alt="location pin icon"
                    class="img-t"
            /> </span
            >V
        </h1>
    </div>
</header>
<main class="container">
    <h1 class="m-h">Welcome to FlavFinder!</h1>
    <%--  Ensure the action point to a servlet to extract form data  --%>
    <form action="/auth" method="POST" id="auth-form">
        <div class="con-wrapper">
            <label for="first_name">first Name</label>
            <div class="input-wrapper">
                <img src="images/user.png" alt="avatar logo" />
                <input
                        type="text"
                        name="first_name"
                        id="first_name"
                        placeholder="First name"
                        required
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
                        placeholder="Email"
                        required
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
        </div>
        <p class="dir-deco">
            Already have an account?<a href="auth?action=login">Login</a>
        </p>
        <div class="btn-container">
            <button type="submit" class="btn-submit" id="btn-submit">
                Sign Up
            </button>
        </div>
    </form>
</main>
</body>
</html>
