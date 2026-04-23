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
                    <img src="${pageContext.request.contextPath}/images/phone.png" class="r-card-icon" alt="phone icon">
                    Call
                </a>
            </c:if>
            <c:if test="${not empty restaurant.website}">
                <a href="${fn:escapeXml(restaurant.website)}"
                   class="cv-btn cv-btn-outline"
                   target="_blank" rel="noopener noreferrer">
                    <img src="${pageContext.request.contextPath}/images/website.png" class="r-card-icon" alt="website icon">
                    Website
                </a>
            </c:if>
            <c:if test="${not empty restaurant.bookingLink}">
                <a href="${fn:escapeXml(restaurant.bookingLink)}"
                   class="cv-btn cv-btn-outline"
                   target="_blank" rel="noopener noreferrer">
                    <img src="${pageContext.request.contextPath}/images/reservation.png" class="r-card-icon" alt="reservation icon">
                    Reserve
                </a>
            </c:if>
        </div>

        <%-- ── Photo Strip ── --%>
        <div class="cv-section cv-section-photos">
            <h2 class="cv-section-label">Photos</h2>
            <c:choose>
                <c:when test="${fn:length(restaurant.photosSample) > 1}">
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
                </c:when>
                <c:otherwise>
                    <p class="cv-photos-empty">Could not load photos.</p>
                </c:otherwise>
            </c:choose>
        </div>

        <%-- ── Details Section ── --%>
        <div class="cv-section cv-details">
            <h2 class="cv-section-label">Details</h2>

            <c:if test="${not empty restaurant.fullAddress}">
                <div class="cv-detail-row">
                    <img src="${pageContext.request.contextPath}/images/g-location.png" class="cv-detail-icon" alt="address icon">
                    <span class="cv-detail-text">${fn:escapeXml(restaurant.fullAddress)}</span>
                </div>
            </c:if>

            <c:if test="${not empty restaurant.phoneNumber}">
                <div class="cv-detail-row">
                    <img src="${pageContext.request.contextPath}/images/g-phone.png" class="cv-detail-icon" alt="phone icon">
                    <a href="tel:${fn:escapeXml(restaurant.phoneNumber)}" class="cv-detail-link">
                            ${fn:escapeXml(restaurant.phoneNumber)}
                    </a>
                </div>
            </c:if>

            <c:if test="${not empty restaurant.workingHours}">
                <div class="cv-detail-row">
                    <img src="${pageContext.request.contextPath}/images/g-clock.png" class="cv-detail-icon" alt="hours icon">
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
<script src="${pageContext.request.contextPath}/js/home.js"></script>
<script src="${pageContext.request.contextPath}/js/cardView.js"></script>
</body>
</html>