<jsp:include page="../../taglib.jsp"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Home - FlavFinder</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/homePage.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
    <script src="${pageContext.request.contextPath}/js/script.js"></script>
</head>
<body>
<%-- Reminder: Ensure the header is a includes--%>
<header>
    <jsp:include page="../../flav-logo.jsp"/>
    <div class="search-c">
        <img src="${pageContext.request.contextPath}/images/search-icon.png"
             class="inner-icon search-icon" alt="search icon">
        <input type="text" id="search" name="search-term" placeholder="Search for cuisines...">
        <span id="filter-trigger" class="filter-btn">
                <img src="${pageContext.request.contextPath}/images/tune.png"
                     class="inner-icon filter-icon" alt="filter icon">
            </span>
        <!--    add drop down container/content        -->
    </div>
    <div class="user-content">
        <div class="location-drop-container">
            <div class="location-p">
                <button class="location-toggle-btn" id="location-toggle-btn">
                    <img src="${pageContext.request.contextPath}/images/near-me.png"
                         class="inner-icon" alt="Near me Icon">
                </button>
            </div>
            <!--    add drop down content        -->
            <div class="location-dropdown-content"></div>
        </div>
        <div class="to-go-container">
            <!--     Ensure to add a link to the to-go page when completed           -->
            <a href="#" id="to-go-deco">
                <img src="${pageContext.request.contextPath}/images/favorite-heart.png"
                     class="inner-icon" id="to-go-icon" alt="To-Go icon">
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
<main>
    <jsp:include page="locationPopUp.jsp"/>
    <p>First Name: ${sessionScope.firstName}</p>
    <br>
    <p>Email: ${sessionScope.email}</p>
    <a href="${pageContext.request.contextPath}/logout">Logout</a>
</main>
<footer></footer>
</body>
</html>