<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Login</title>
    <link rel="stylesheet" href="css/main.css">
    <link rel="stylesheet" href="css/auth.css">
</head>
<body>
<main class="signup-container">
    <h1 class="m-h">Welcome Back!</h1>
    <div id="validation-sec">
        <!-- Provide feedback whether info is invalid -->
    </div>
<%--  ensure action points to a servlet  --%>
    <form action="#" id="auth-exists">
        <label for="email">email</label>
        <div class="input-wrapper">
            <img src="images/email.png" alt="mail icon" />
            <input type="email" name="email" id="email" placeholder="Email" />
        </div>

        <label for="password">password</label>
        <div class="input-wrapper">
            <img src="images/lock.png" alt="lock icon" />
            <input
                    type="password"
                    name="password"
                    id="password"
                    placeholder="Password"
            />
        </div>

        <div class="btn-container">
            <button type="submit" class="btn-submit">Sign in</button>
        </div>
        <p class="dir-deco">
            Don't have an account?<a href="sign-up.html">Sign up</a>
        </p>
    </form>
</main>
</body>
</html>
