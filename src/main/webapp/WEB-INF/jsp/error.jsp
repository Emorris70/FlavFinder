<%@ include file="includes/taglib.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>
        <c:choose>
            <c:when test="${requestScope['javax.servlet.error.status_code'] == 404}">Page Not Found</c:when>
            <c:when test="${requestScope['javax.servlet.error.status_code'] == 400}">Bad Request</c:when>
            <c:otherwise>Something Went Wrong</c:otherwise>
        </c:choose>
        — FlavFinder
    </title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
    <style>
        .error-page {
            min-height: 100vh;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            text-align: center;
            padding: 2rem;
            background: var(--bg-primary, #f9f9f9);
        }
        .error-code {
            font-size: 6rem;
            font-weight: 800;
            color: var(--flav-red, #e63946);
            line-height: 1;
            margin-bottom: 0.5rem;
        }
        .error-title {
            font-size: 1.5rem;
            font-weight: 600;
            color: var(--text-primary, #1a1a1a);
            margin-bottom: 0.75rem;
        }
        .error-msg {
            color: var(--text-secondary, #666);
            margin-bottom: 2rem;
            max-width: 400px;
        }
        .error-btn {
            display: inline-block;
            background: var(--flav-red, #e63946);
            color: #fff;
            padding: 0.75rem 1.75rem;
            border-radius: 50px;
            font-weight: 600;
            text-decoration: none;
            transition: opacity 0.2s;
        }
        .error-btn:hover { opacity: 0.85; }
    </style>
</head>
<body>
<div class="error-page">
    <div class="error-code">${requestScope['javax.servlet.error.status_code']}</div>

    <c:choose>
        <c:when test="${requestScope['javax.servlet.error.status_code'] == 404}">
            <h1 class="error-title">Page Not Found</h1>
            <p class="error-msg">The page you're looking for doesn't exist or may have been moved.</p>
        </c:when>
        <c:when test="${requestScope['javax.servlet.error.status_code'] == 400}">
            <h1 class="error-title">Bad Request</h1>
            <p class="error-msg">The request couldn't be understood. Please check your input and try again.</p>
        </c:when>
        <c:otherwise>
            <h1 class="error-title">Something Went Wrong</h1>
            <p class="error-msg">An unexpected error occurred on our end. Please try again in a moment.</p>
        </c:otherwise>
    </c:choose>

    <a href="${pageContext.request.contextPath}/home" class="error-btn">Back to Home</a>
</div>
</body>
</html>
