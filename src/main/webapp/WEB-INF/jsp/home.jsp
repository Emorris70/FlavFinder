<%@ include file= "includes/taglib.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<jsp:include page="includes/app-head.jsp"/>
<body>
<jsp:include page="includes/header-home.jsp"/>
<main>
    <jsp:include page="includes/locationPopUp.jsp"/>
    <div class="home-content">

        <%-- Category Filter Pills --%>
        <div class="category-section">
            <h2 class="section-heading">What's on the menu tonight?</h2>
            <div class="category-pills">
                <button class="pill active">All</button>
                <button class="pill">Italian</button>
                <button class="pill">Japanese</button>
                <button class="pill">Mexican</button>
                <button class="pill">American</button>
                <button class="pill">Thai</button>
                <button class="pill">Chinese</button>
                <button class="pill">Mediterranean</button>
                <button class="pill">Indian</button>
            </div>
        </div>
            <%-- Recently Viewed Section --%>
            <c:if test="${not empty sessionScope.recentlyViewed and empty requestScope.searchTerm}">
            <div class="restaurant-section">
                <h3 class="section-title">
                    <img src="${pageContext.request.contextPath}/images/clock.png" alt="Recently Viewed icon" class="section-icon">
                    Recently Viewed
                </h3>
                <div class="cards-row">
                    <c:forEach var="restaurant" items="${sessionScope.recentlyViewed}">
                        <a href="${pageContext.request.contextPath}/restaurant?placeId=${restaurant.placeId}"
                           class="restaurant-card card-wide">
                            <div class="card-img-wrap">
                                <c:choose>
                                    <c:when test="${not empty restaurant.photosSample}">
                                        <img src="${restaurant.photosSample[0].photoUrl}"
                                             alt="${restaurant.name}" class="card-img"
                                             referrerpolicy="no-referrer">
                                    </c:when>
                                    <c:otherwise>
                                        <img src="${pageContext.request.contextPath}/images/near-me.png"
                                             alt="${restaurant.name}" class="card-img">
                                    </c:otherwise>
                                </c:choose>
                                <button class="fav-btn ${sessionScope.savedPlaceIds.contains(restaurant.placeId) ? 'saved' : ''}"
                                        data-place-id="${restaurant.placeId}">
                                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 -960 960 960">
                                        <path d="m480-120-58-52q-101-91-167-157T150-447.5Q111-500 95.5-544T80-634q0-94 63-157t157-63q52 0 99 22t81 62q34-40 81-62t99-22q94 0 157 63t63 157q0 46-15.5 90T810-447.5Q771-395 705-329T538-172l-58 52Z"/>
                                    </svg>
                                </button>
                            </div>
                            <div class="card-body">
                                <div class="card-title-row">
                                    <span class="card-name">${restaurant.name}</span>
                                    <span class="card-price">${not empty restaurant.priceLevel ? restaurant.priceLevel : ''}</span>
                                </div>
                                <c:if test="${restaurant.rating > 0}">
                                    <div class="card-rating">
                                        <svg class="star-icon" viewBox="0 0 24 24">
                                            <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
                                        </svg>
                                        <span class="rating-val">${restaurant.rating}</span>
                                        <span class="review-count">(${restaurant.reviewCount})</span>
                                        <span class="card-separator">·</span>
                                        <span class="card-type">${fn:escapeXml(restaurant.type)}</span>
                                    </div>
                                </c:if>
                                <div class="card-footer-row">
                                    <svg class="loc-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 -960 960 960">
                                        <path d="M480-301q99-80 149.5-154T680-594q0-90-56-148t-144-58q-88 0-144 58t-56 148q0 65 50.5 139T480-301Zm0 101Q339-304 269.5-402T200-594q0-125 78-205.5T480-880q124 0 202 80.5T760-594q0 94-69.5 192T480-200Zm0-320q33 0 56.5-23.5T560-600q0-33-23.5-56.5T480-680q-33 0-56.5 23.5T400-600q0 33 23.5 56.5T480-520Zm0-80Z"/>
                                    </svg>
                                    <span class="card-distance" data-lat="${restaurant.latitude}" data-lon="${restaurant.longitude}">--</span>
                                    <span class="open-status ${not empty restaurant.openingStatus and restaurant.openingStatus.toLowerCase().contains('open') ? 'status-open' : 'status-closed'}">
                                        <c:choose>
                                            <c:when test="${not empty restaurant.openingStatus and restaurant.openingStatus.toLowerCase().contains('open')}">Open</c:when>
                                            <c:when test="${not empty restaurant.openingStatus and restaurant.openingStatus.toLowerCase().contains('close')}">Closed</c:when>
                                            <c:otherwise>--</c:otherwise>
                                        </c:choose>
                                    </span>
                                </div>
                            </div>
                        </a>
                    </c:forEach>
                </div>
            </div>
            </c:if>

            <%-- Nearby Restaurants Section --%>
            <div class="restaurant-section" id="nearby-section">
                <h3 class="section-title">
                    <img src="${pageContext.request.contextPath}/images/h-location-i.png" alt="Nearby icon" class="section-icon">
                    <c:choose>
                        <c:when test="${not empty requestScope.searchTerm}">Results for "${requestScope.searchTerm}"</c:when>
                        <c:otherwise>Nearby Restaurants</c:otherwise>
                    </c:choose>
                </h3>
                <c:choose>
                    <c:when test="${not empty requestScope.nearbyRestaurants.data}">
                        <div class="cards-grid">
                            <c:forEach var="restaurant" items="${requestScope.nearbyRestaurants.data}">
                                <a href="${pageContext.request.contextPath}/restaurant?placeId=${restaurant.placeId}"
                                   class="restaurant-card card-grid">
                                    <div class="card-img-wrap">
                                        <c:choose>
                                            <c:when test="${not empty restaurant.photosSample}">
                                                <img src="${restaurant.photosSample[0].photoUrl}"
                                                     alt="${restaurant.name}" class="card-img"
                                                     referrerpolicy="no-referrer">
                                            </c:when>
                                            <c:otherwise>
                                                <img src="${pageContext.request.contextPath}/images/near-me.png"
                                                     alt="${restaurant.name}" class="card-img">
                                            </c:otherwise>
                                        </c:choose>
                                        <button class="fav-btn ${sessionScope.savedPlaceIds.contains(restaurant.placeId) ? 'saved' : ''}"
                                                data-place-id="${restaurant.placeId}">
                                            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 -960 960 960">
                                                <path d="m480-120-58-52q-101-91-167-157T150-447.5Q111-500 95.5-544T80-634q0-94 63-157t157-63q52 0 99 22t81 62q34-40 81-62t99-22q94 0 157 63t63 157q0 46-15.5 90T810-447.5Q771-395 705-329T538-172l-58 52Z"/>
                                            </svg>
                                        </button>
                                    </div>
                                    <div class="card-body">
                                        <div class="card-title-row">
                                            <span class="card-name">${restaurant.name}</span>
                                            <span class="card-price">${not empty restaurant.priceLevel ? fn:escapeXml(restaurant.priceLevel) : ''}</span>
                                        </div>
                                        <c:if test="${restaurant.rating > 0}">
                                            <div class="card-rating">
                                                <svg class="star-icon" viewBox="0 0 24 24">
                                                    <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
                                                </svg>
                                                <span class="rating-val">${restaurant.rating}</span>
                                                <span class="review-count">(${restaurant.reviewCount})</span>
                                                <span class="card-separator">·</span>
                                                <span class="card-type">${fn:escapeXml(restaurant.type)}</span>
                                            </div>
                                        </c:if>
                                        <div class="card-footer-row">
                                            <svg class="loc-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 -960 960 960">
                                                <path d="M480-301q99-80 149.5-154T680-594q0-90-56-148t-144-58q-88 0-144 58t-56 148q0 65 50.5 139T480-301Zm0 101Q339-304 269.5-402T200-594q0-125 78-205.5T480-880q124 0 202 80.5T760-594q0 94-69.5 192T480-200Zm0-320q33 0 56.5-23.5T560-600q0-33-23.5-56.5T480-680q-33 0-56.5 23.5T400-600q0 33 23.5 56.5T480-520Zm0-80Z"/>
                                            </svg>
                                            <span class="card-distance" data-lat="${restaurant.latitude}" data-lon="${restaurant.longitude}">--</span>
                                            <span class="open-status ${not empty restaurant.openingStatus and restaurant.openingStatus.toLowerCase().contains('open') ? 'status-open' : 'status-closed'}">
                                                <c:choose>
                                                        <c:when test="${not empty restaurant.openingStatus and restaurant.openingStatus.toLowerCase().contains('open')}">Open</c:when>
                                                        <c:when test="${not empty restaurant.openingStatus and restaurant.openingStatus.toLowerCase().contains('close')}">Closed</c:when>
                                                        <c:otherwise>--</c:otherwise>
                                                </c:choose>
                                            </span>
                                        </div>
                                    </div>
                                </a>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <c:choose>
                            <c:when test="${not empty requestScope.searchTerm and empty requestScope.nearbyRestaurants}">
                                <p class="no-results">Set a location to search for restaurants.</p>
                            </c:when>
                            <c:when test="${not empty requestScope.searchTerm}">
                                <p class="no-results">No results found for "${requestScope.searchTerm}".</p>
                            </c:when>
                            <c:otherwise>
                                <p class="no-results">Set a location to discover nearby restaurants.</p>
                            </c:otherwise>
                        </c:choose>
                    </c:otherwise>
                </c:choose>
            </div>
    </div>
</main>
<footer></footer>
<script>
    const contextPath = "${pageContext.request.contextPath}";
    <c:choose>
        <c:when test="${not empty sessionScope.userLat}">
            const userLat = ${sessionScope.userLat};
            const userLon = ${sessionScope.userLon};
        </c:when>
        <c:when test="${not empty sessionScope.userLocation}">
            const userLat = ${sessionScope.userLocation.results[0].position.lat};
            const userLon = ${sessionScope.userLocation.results[0].position.lon};
        </c:when>
        <c:when test="${not empty sessionScope.savedLocation}">
            const userLat = ${sessionScope.savedLocation.latitude};
            const userLon = ${sessionScope.savedLocation.longitude};
        </c:when>
        <c:otherwise>
            const userLat = null;
            const userLon = null;
        </c:otherwise>
    </c:choose>
    const savedPlaceIds = new Set([<c:forEach var="pid" items="${sessionScope.savedPlaceIds}" varStatus="s">'${pid}'<c:if test="${!s.last}">,</c:if></c:forEach>]);
</script>
<script src="${pageContext.request.contextPath}/js/home-mini.js"></script>
</body>
</html>