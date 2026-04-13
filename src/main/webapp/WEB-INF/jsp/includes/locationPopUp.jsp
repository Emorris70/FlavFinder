<%@ include file= "taglib.jsp" %>
<%-- TODO add error handling and stying --%>
<div class="location-popUp-content pop-up-bg" id="pop-up"> <!--  parent  -->
    <div class="content-card-deco"> <!--  card  -->
        <div class="drop-c">
            <p class="p-deco-1 p-flex">
                Set Discovery Location
                <svg class="close" xmlns="http://www.w3.org/2000/svg"
                     viewBox="0 -960 960 960"
                     fill="#e3e3e3">
                    <path d="m256-200-56-56 224-224-224-224 56-56 224 224 224-224 56 56-224 224 224 224-56 56-224-224-224 224Z"/>
                </svg>
            </p>
            <p class="p-deco-2 muted-con">Choose a location to discover nearby restaurants</p>
            <c:if test="${not empty sessionScope.error}">
                <div class="errorMsg">
                    <p class="error-msg">${sessionScope.error}</p>
                </div>
                <c:remove var="error" scope="session"/>
            </c:if>
        </div>
        <div class="c-items">
            <label class="current-location-c location-c-deco">
                <div class="svg-deco">
                    <svg class="location-icon" xmlns="http://www.w3.org/2000/svg"
                         viewBox="0 -960 960 960"
                         fill="#e3e3e3">
                        <path d="M516-120 402-402 120-516v-56l720-268-268 720h-56Zm26-148 162-436-436 162 196 78 78 196Zm-78-196Z"/>
                    </svg>
                </div>
                <div class="content-info">
                    <p class="p-h1">Use Current Location</p>
                    <p class="p-deco-2 muted-con">Auto-detect location</p>
                </div>
                <input class="radio-end radio-deco" type="radio" name="location" value="current">
            </label>
            <div class="custom-location-c location-c-deco flex-override">
                <div class="custom-lc">
                    <div class="svg-deco">
                        <svg class="location-icon" xmlns="http://www.w3.org/2000/svg"
                             viewBox="0 -960 960 960"
                             fill="#e3e3e3">
                            <path d="M536.5-503.5Q560-527 560-560t-23.5-56.5Q513-640 480-640t-56.5 23.5Q400-593 400-560t23.5 56.5Q447-480 480-480t56.5-23.5ZM480-186q122-112 181-203.5T720-552q0-109-69.5-178.5T480-800q-101 0-170.5 69.5T240-552q0 71 59 162.5T480-186Zm0 106Q319-217 239.5-334.5T160-552q0-150 96.5-239T480-880q127 0 223.5 89T800-552q0 100-79.5 217.5T480-80Zm0-480Z"/></svg>
                    </div>
                    <div class="inner-lc">
                        <p class="p-h1">Set Custom Location</p>
                        <p class="p-deco-2 muted-con">Enter the city/code to explore</p>
                        <label for="cust-l"></label>
                    </div>
                </div>
                <form method="POST" action="${pageContext.request.contextPath}/location" class="custom-form">
                    <input class="custom-input" type="text" name="cust-location" id="cust-l" placeholder="City & ZIP or full address">
<%--                TODO loop through the response to relay location info    --%>
                    <button type="submit" value="" class="custom-submit">Set</button>
                </form>
            </div>
        </div>
    </div>
</div>