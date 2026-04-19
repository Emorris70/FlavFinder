<%@ include file= "taglib.jsp" %>
<header>
    <jsp:include page="flav-logo.jsp"/>
    <div class="search-c">
        <img src="${pageContext.request.contextPath}/images/search-icon.png"
             class="inner-icon search-icon" alt="search icon">
        <form method="GET" action="${pageContext.request.contextPath}/search" class="search-form">
            <input type="text" id="search" name="search-term" placeholder="Search for cuisines...">
        </form>
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
                    <c:choose>
                        <c:when test="${not empty sessionScope.userLocation}">
                            ${sessionScope.userLocation.results[0].address.municipality}, ${sessionScope.userLocation.results[0].address.countrySubdivisionCode}
                        </c:when>
                        <c:when test="${not empty sessionScope.userLat}">
                            Current Location
                        </c:when>
                        <c:when test="${not empty sessionScope.savedLocation}">
                            ${sessionScope.savedLocation.cityName}
                        </c:when>
                        <c:otherwise>Set Location</c:otherwise>
                    </c:choose>
                </button>
            </div>
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
                <button class="user-pf dropdown" id="user-pf">
                    ${fn:substring(sessionScope.user.firstName, 0, 2)}
                </button>
            </div>
            <div class="user-dropdown-content drop-r">
                <div class="to-go user-d-c">
                    <img src="${pageContext.request.contextPath}/images/favorite.png" alt="to-go icon" class="to-go-icon h-icon">
                    <a href="#">To-go</a>
                </div>
                <div class="settings user-d-c">
                    <img src="${pageContext.request.contextPath}/images/settings.png" alt="settings icon" class="settings-icon h-icon">
                    <a href="#">Settings</a>
                </div>
                <div class="log-out user-d-c">
                    <img src="${pageContext.request.contextPath}/images/logout.png" alt="logout icon" class="logout-icon h-icon">
                    <a href="${pageContext.request.contextPath}/logout">Logout</a>
                </div>
            </div>
        </div>
    </div>
</header>
