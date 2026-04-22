<%@ include file="includes/taglib.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>${sessionScope.page}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/homePage.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cardView.css">
</head>
<body>
<jsp:include page="includes/header-home.jsp"/>
<jsp:include page="includes/locationPopUp.jsp"/>

<main class="cv-main">

    <%-- ── Hero ── --%>
    <div class="cv-hero">
        <c:choose>
            <c:when test="${not empty restaurant.photosSample}">
                <img src="${restaurant.photosSample[0].photoUrlLarge}"
                     alt="${fn:escapeXml(restaurant.name)}"
                     class="cv-hero-img"
                     referrerpolicy="no-referrer">
            </c:when>
            <c:otherwise>
                <img src="${pageContext.request.contextPath}/images/near-me.png"
                     alt="${fn:escapeXml(restaurant.name)}"
                     class="cv-hero-img cv-hero-placeholder">
            </c:otherwise>
        </c:choose>

        <div class="cv-hero-overlay">

            <%-- Top row: back + save --%>
            <div class="cv-hero-top">
                <a href="${pageContext.request.contextPath}/home" class="cv-back-btn" aria-label="Back to home">
                    <svg viewBox="0 0 24 24" class="cv-back-icon">
                        <path d="M20 11H7.83l5.59-5.59L12 4l-8 8 8 8 1.41-1.41L7.83 13H20v-2z"/>
                    </svg>
                </a>
                <button class="cv-save-btn fav-btn" aria-label="Save restaurant">
                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 -960 960 960">
                        <path d="m480-120-58-52q-101-91-167-157T150-447.5Q111-500 95.5-544T80-634q0-94 63-157t157-63q52 0 99 22t81 62q34-40 81-62t99-22q94 0 157 63t63 157q0 46-15.5 90T810-447.5Q771-395 705-329T538-172l-58 52Z"/>
                    </svg>
                </button>
            </div>

            <%-- Bottom meta: name, rating, type, price, open status --%>
            <div class="cv-hero-meta">
                <h1 class="cv-name">${fn:escapeXml(restaurant.name)}</h1>
                <div class="cv-meta-row">
                    <c:if test="${restaurant.rating > 0}">
                        <svg class="cv-star" viewBox="0 0 24 24">
                            <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
                        </svg>
                        <span class="cv-rating">${restaurant.rating}</span>
                        <span class="cv-reviews">(${restaurant.reviewCount})</span>
                        <span class="cv-dot">·</span>
                    </c:if>
                    <c:if test="${not empty restaurant.type}">
                        <span class="cv-type">${fn:escapeXml(restaurant.type)}</span>
                    </c:if>
                    <c:if test="${not empty restaurant.priceLevel}">
                        <span class="cv-dot">·</span>
                        <span class="cv-price">${fn:escapeXml(restaurant.priceLevel)}</span>
                    </c:if>
                </div>
                <c:if test="${not empty restaurant.openingStatus}">
                    <c:choose>
                        <c:when test="${restaurant.openingStatus.toLowerCase().contains('open')}">
                            <span class="cv-status-pill cv-open">Open</span>
                        </c:when>
                        <c:when test="${restaurant.openingStatus.toLowerCase().contains('close')}">
                            <span class="cv-status-pill cv-closed">Closed</span>
                        </c:when>
                    </c:choose>
                </c:if>
            </div>

        </div>
    </div>

    <div class="cv-content">

    <%-- ── Action Buttons ── --%>
    <div class="cv-actions">
        <c:if test="${not empty restaurant.placeLink}">
            <a href="${fn:escapeXml(restaurant.placeLink)}"
               class="cv-btn cv-btn-primary"
               target="_blank" rel="noopener noreferrer">
                <svg viewBox="0 0 24 24" class="cv-btn-icon">
                    <path d="M12 2C8.13 2 5 5.13 5 9c0 5.25 7 13 7 13s7-7.75 7-13c0-3.87-3.13-7-7-7zm0 9.5c-1.38 0-2.5-1.12-2.5-2.5s1.12-2.5 2.5-2.5 2.5 1.12 2.5 2.5-1.12 2.5-2.5 2.5z"/>
                </svg>
                Directions
            </a>
        </c:if>
        <c:if test="${not empty restaurant.phoneNumber}">
            <a href="tel:${fn:escapeXml(restaurant.phoneNumber)}" class="cv-btn cv-btn-outline">
                <svg viewBox="0 0 24 24" class="cv-btn-icon">
                    <path d="M6.62 10.79c1.44 2.83 3.76 5.14 6.59 6.59l2.2-2.2c.27-.27.67-.36 1.02-.24 1.12.37 2.33.57 3.57.57.55 0 1 .45 1 1V20c0 .55-.45 1-1 1-9.39 0-17-7.61-17-17 0-.55.45-1 1-1h3.5c.55 0 1 .45 1 1 0 1.25.2 2.45.57 3.57.11.35.03.74-.25 1.02l-2.2 2.2z"/>
                </svg>
                Call
            </a>
        </c:if>
        <c:if test="${not empty restaurant.website}">
            <a href="${fn:escapeXml(restaurant.website)}"
               class="cv-btn cv-btn-outline"
               target="_blank" rel="noopener noreferrer">
                <svg viewBox="0 0 24 24" class="cv-btn-icon">
                    <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-1 17.93c-3.95-.49-7-3.85-7-7.93 0-.62.08-1.21.21-1.79L9 15v1c0 1.1.9 2 2 2v1.93zm6.9-2.54c-.26-.81-1-1.39-1.9-1.39h-1v-3c0-.55-.45-1-1-1H8v-2h2c.55 0 1-.45 1-1V7h2c1.1 0 2-.9 2-2v-.41c2.93 1.19 5 4.06 5 7.41 0 2.08-.8 3.97-2.1 5.39z"/>
                </svg>
                Website
            </a>
        </c:if>
        <c:if test="${not empty restaurant.bookingLink}">
            <a href="${fn:escapeXml(restaurant.bookingLink)}"
               class="cv-btn cv-btn-outline"
               target="_blank" rel="noopener noreferrer">
                <svg viewBox="0 0 24 24" class="cv-btn-icon">
                    <path d="M17 12h-5v5h5v-5zM16 1v2H8V1H6v2H5c-1.11 0-1.99.9-1.99 2L3 19c0 1.1.89 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2h-1V1h-2zm3 18H5V8h14v11z"/>
                </svg>
                Reserve
            </a>
        </c:if>
    </div>

    <%-- ── Photo Strip (only when more than 1 photo available) ── --%>
    <c:if test="${fn:length(restaurant.photosSample) > 1}">
        <div class="cv-section">
            <h2 class="cv-section-label">Photos</h2>
            <div class="cv-photos">
                <c:forEach var="photo" items="${restaurant.photosSample}">
                    <a href="${photo.photoUrlLarge}"
                       target="_blank" rel="noopener noreferrer"
                       class="cv-photo-link">
                        <img src="${photo.photoUrl}"
                             alt="${fn:escapeXml(restaurant.name)}"
                             class="cv-photo-thumb"
                             referrerpolicy="no-referrer">
                    </a>
                </c:forEach>
            </div>
        </div>
    </c:if>

    <%-- ── Details Section ── --%>
    <div class="cv-section cv-details">
        <h2 class="cv-section-label">Details</h2>

        <c:if test="${not empty restaurant.fullAddress}">
            <div class="cv-detail-row">
                <svg class="cv-detail-icon" viewBox="0 -960 960 960">
                    <path d="M480-301q99-80 149.5-154T680-594q0-90-56-148t-144-58q-88 0-144 58t-56 148q0 65 50.5 139T480-301Zm0 101Q339-304 269.5-402T200-594q0-125 78-205.5T480-880q124 0 202 80.5T760-594q0 94-69.5 192T480-200Zm0-320q33 0 56.5-23.5T560-600q0-33-23.5-56.5T480-680q-33 0-56.5 23.5T400-600q0 33 23.5 56.5T480-520Zm0-80Z"/>
                </svg>
                <span class="cv-detail-text">${fn:escapeXml(restaurant.fullAddress)}</span>
            </div>
        </c:if>

        <c:if test="${not empty restaurant.phoneNumber}">
            <div class="cv-detail-row">
                <svg class="cv-detail-icon" viewBox="0 0 24 24">
                    <path d="M6.62 10.79c1.44 2.83 3.76 5.14 6.59 6.59l2.2-2.2c.27-.27.67-.36 1.02-.24 1.12.37 2.33.57 3.57.57.55 0 1 .45 1 1V20c0 .55-.45 1-1 1-9.39 0-17-7.61-17-17 0-.55.45-1 1-1h3.5c.55 0 1 .45 1 1 0 1.25.2 2.45.57 3.57.11.35.03.74-.25 1.02l-2.2 2.2z"/>
                </svg>
                <a href="tel:${fn:escapeXml(restaurant.phoneNumber)}" class="cv-detail-link">
                    ${fn:escapeXml(restaurant.phoneNumber)}
                </a>
            </div>
        </c:if>

        <c:if test="${not empty restaurant.workingHours}">
            <div class="cv-detail-row">
                <svg class="cv-detail-icon" viewBox="0 0 24 24">
                    <path d="M11.99 2C6.47 2 2 6.48 2 12s4.47 10 9.99 10C17.52 22 22 17.52 22 12S17.52 2 11.99 2zM12 20c-4.42 0-8-3.58-8-8s3.58-8 8-8 8 3.58 8 8-3.58 8-8 8zm.5-13H11v6l5.25 3.15.75-1.23-4.5-2.67V7z"/>
                </svg>
                <div class="cv-hours-wrap">
                    <button class="cv-hours-toggle"
                            id="cv-hours-toggle"
                            aria-expanded="false"
                            aria-controls="cv-hours-body">
                        Hours
                        <svg class="cv-chevron" viewBox="0 0 24 24">
                            <path d="M7.41 8.59L12 13.17l4.59-4.58L18 10l-6 6-6-6 1.41-1.41z"/>
                        </svg>
                    </button>
                    <div class="cv-hours-body" id="cv-hours-body" hidden>
                        <c:forEach var="entry" items="${restaurant.workingHours}">
                            <div class="cv-hours-entry" data-day="${fn:escapeXml(entry.key)}">
                                <span class="cv-hours-day">${fn:escapeXml(entry.key)}</span>
                                <span class="cv-hours-times">
                                    <c:forEach var="t" items="${entry.value}" varStatus="s">
                                        ${fn:escapeXml(t)}<c:if test="${!s.last}">, </c:if>
                                    </c:forEach>
                                </span>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </c:if>

    </div><%-- cv-details --%>

    </div><%-- cv-content --%>

</main>

<script>const contextPath = '${pageContext.request.contextPath}';</script>
<script src="${pageContext.request.contextPath}/js/script.js"></script>
<script src="${pageContext.request.contextPath}/js/cardView.js"></script>
</body>
</html>
