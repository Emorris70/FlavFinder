<jsp:include page="../../taglib.jsp"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Home - FlavFinder</title>
    <link rel="stylesheet" href="../../css/homePage.css">
    <link rel="stylesheet" href="../../css/main.css">
</head>
<body>
<%-- Reminder: Ensure the header is a includes--%>
<header>
    <jsp:include page="../../flav-logo.jsp"/>
    <div class="search-c">
        <img src="../../images/search-icon.png" class="inner-icon search-icon" alt="search icon">
        <input type="text" id="search" name="search-term" placeholder="Search for cuisines...">
        <span id="filter-trigger" class="filter-btn">
                <img src="../../images/tune.png" class="inner-icon filter-icon" alt="filter icon">
            </span>
        <!--    add drop down container/content        -->
    </div>
    <div class="user-content">
        <div class="location-drop-container">
            <div class="location-p">
                <button class="location-toggle-btn" id="location-toggle-btn">
                    <img src="../../images/near-me.png" class="inner-icon" alt="Near me Icon">

                </button>
            </div>
            <!--    add drop down content        -->
            <div class="location-dropdown-content"></div>
        </div>
        <div class="to-go-container">
            <!--     Ensure to add a link to the to-go page when completed           -->
            <a href="#" id="to-go-deco">
                <img src="../../images/favorite-heart.png" class="inner-icon" id="to-go-icon" alt="To-Go icon">
                To-Go
            </a>
        </div>
        <div class="user-drop-container">
            <div class="user-toggle-btn">
                <button class="user-pf" id="user-pf"></button>
            </div>
            <!--    add drop down container/content        -->
            <div class="user-dropdown-content"></div>
        </div>
    </div>
</header>
<main></main>
<footer></footer>
</body>
</html>