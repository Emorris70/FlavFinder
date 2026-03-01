<%@include file="taglib.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Home - FlavFinder</title>
    <link rel="stylesheet" href="css/homePage.css">
    <link rel="stylesheet" href="css/main.css">
</head>
<body>
<%-- Reminder: Ensure the header is a includes--%>
<header>
    <%@include file="flav-logo.jsp"%>
    <div class="search-c">
        <input type="search" id="search" name="search-term" placeholder="Search for cuisines...">
    </div>
    <div class="user-content">
        <div class="location-drop-container">
            <div class="location-p">
                <button class="location-toggle-btn" id="location-toggle-btn">
                    <img src="images/near-me.png" class="inner-icon" alt="Near me Icon">

                </button>
            </div>
            <div class="location-dropdown-content"></div>
        </div>
        <div class="to-go-container">
            <a href="#" id="to-go-deco">
                <img src="images/favorite-heart.png" class="inner-icon" alt="To-Go icon">
                To-Go
            </a>
        </div>
        <div class="user-drop-container">
            <div class="user-toggle-btn">
                <button class="user-pf" id="user-pf">User</button>
            </div>
            <!--                <div class="user-dropdown-content"></div>-->
        </div>
    </div>
</header>
<main></main>
<footer></footer>
</body>
</html>
