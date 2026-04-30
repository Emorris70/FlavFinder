<%@ include file="includes/taglib.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>${sessionScope.page}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home-mini.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main-mini.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/settings-mini.css">
</head>
<body>
<jsp:include page="includes/header-home.jsp"/>
<jsp:include page="includes/locationPopUp.jsp"/>

<main class="settings-main">

    <%-- Page header --%>
    <div class="settings-page-header">
        <button id="settings-back-btn" class="settings-back-btn" aria-label="Go back">
            <svg class="settings-back-icon" viewBox="0 0 24 24">
                <path d="M20 11H7.83l5.59-5.59L12 4l-8 8 8 8 1.41-1.41L7.83 13H20v-2z"/>
            </svg>
        </button>
        <h1 class="settings-title">Settings</h1>
    </div>

    <%-- Error banner --%>
    <c:if test="${not empty sessionScope.settingsError}">
        <div class="settings-banner">
            <div class="error-msg">${sessionScope.settingsError}</div>
        </div>
        <c:remove var="settingsError" scope="session"/>
    </c:if>

    <%-- Account information --%>
    <div class="settings-card">
        <p class="settings-card-title">Account</p>

        <div class="settings-profile-row">
            <div class="settings-avatar">${fn:substring(sessionScope.user.firstName, 0, 2)}</div>
            <div>
                <div class="settings-profile-name">${fn:escapeXml(sessionScope.user.firstName)}</div>
                <div class="settings-profile-email">${fn:escapeXml(sessionScope.user.email)}</div>
            </div>
        </div>

        <div class="settings-info-row">
            <span class="settings-info-label">Name</span>
            <span class="settings-info-value">${fn:escapeXml(sessionScope.user.firstName)}</span>
        </div>
        <div class="settings-info-row">
            <span class="settings-info-label">Email</span>
            <span class="settings-info-value">${fn:escapeXml(sessionScope.user.email)}</span>
        </div>
    </div>

    <%-- Danger zone --%>
    <div class="settings-card danger">
        <p class="settings-card-title danger-title">Danger Zone</p>
        <p class="danger-description">
            Permanently delete your account and all associated data including your saved restaurants
            and locations. This action cannot be undone.
        </p>
        <button class="btn-delete" id="open-delete-modal">
            <svg viewBox="0 0 24 24" width="16" height="16" fill="currentColor">
                <path d="M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z"/>
            </svg>
            Delete Account
        </button>
    </div>

</main>

<%-- Confirmation modal --%>
<div class="settings-modal-overlay" id="delete-modal">
    <div class="settings-modal">
        <div class="settings-modal-icon">
            <svg viewBox="0 0 24 24">
                <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-2h2v2zm0-4h-2V7h2v6z"/>
            </svg>
        </div>
        <h2 class="settings-modal-title">Delete Account?</h2>
        <p class="settings-modal-body">
            This will permanently remove your account, saved restaurants, and locations.
            You will not be able to recover this data.
        </p>
        <div class="settings-modal-actions">
            <button class="btn-modal-cancel" id="cancel-delete">Cancel</button>
            <form method="POST" action="${pageContext.request.contextPath}/settings" style="flex:1">
                <input type="hidden" name="action" value="deleteAccount">
                <button type="submit" class="btn-modal-confirm" style="width:100%">Yes, Delete</button>
            </form>
        </div>
    </div>
</div>

<script>
    const contextPath = '${pageContext.request.contextPath}';
    const userLat = null;
    const userLon = null;
    const savedPlaceIds = new Set();
</script>
<script src="${pageContext.request.contextPath}/js/home-mini.js"></script>
<script src="${pageContext.request.contextPath}/js/settings-mini.js"></script>
</body>
</html>
