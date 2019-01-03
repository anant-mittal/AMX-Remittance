<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%><%@ taglib prefix="c"
    uri="http://java.sun.com/jsp/jstl/core"%><%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"
    session="false"%><!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Khatm SAML SDK Example</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/styles/jquery-ui.min.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/styles/jquery-ui.theme.min.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/styles/jquery.dataTables.min.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/styles/main.css" />
<script src="${pageContext.request.contextPath}/static/js/lib/jquery.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/static/js/lib/jquery-ui.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/static/js/lib/jquery.dataTables.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/static/js/single-sign-on.js" type="text/javascript"></script>
</head>
<body>
    <jsp:include page="menu.jsp">
        <jsp:param name="page" value="authentication" />
    </jsp:include>
    <h1>Oman Khatm Authentication</h1>
    <c:if test="${not empty errorMessage}">
        <div class="error-message">
            <span><c:out value="${errorMessage}" /></span>
        </div>
    </c:if>
    <c:if test="${not empty infoMessage}">
        <div class="info-message">
            <span><c:out value="${infoMessage}" /></span>
        </div>
    </c:if>
    <c:if test="${empty logged}">
        <div id="loginSection" class="section form-container">
            <form:form id="loginForm" method="post" modelAttribute="loginRequest">
                <p>
                    <form:label path="binding">Binding</form:label>
                    <form:select path="binding" items="${bindings}" cssClass="formField" />
                </p>
                <p>
                    <form:label path="authenticationMethod">Authentication method</form:label>
                    <form:select path="authenticationMethod" items="${authMethods}" cssClass="formField" />
                </p>
                <p>
                    <form:label path="relayState">Relay state</form:label>
                    <form:input path="relayState" cssClass="formField" />
                </p>
				<p>
					<form:label path="username" cssStyle="color: blue;">Username*</form:label>
					<form:input path="username" cssClass="formField" />
				</p>
				<p>
					<form:label path="userCivilNumber" cssStyle="color: blue;">User Civil Number*</form:label>
					<form:input path="userCivilNumber" cssClass="formField" />
				</p>
				<p>
					<form:label path="machineName" cssStyle="color: blue;">Machine name*</form:label>
					<form:input path="machineName" cssClass="formField" />
				</p>
				<p>
					<form:label path="location" cssStyle="color: blue;">Location*</form:label>
					<form:input path="location" cssClass="formField" />
				</p>
				<p>
					<form:label path="transactionType" cssStyle="color: blue;">Transaction Type</form:label>
					<form:input path="transactionType" cssClass="formField" />
				</p>
				<p>
					<form:label path="transactionDetails" cssStyle="color: blue;">Transaction Details</form:label>
					<form:input path="transactionDetails" cssClass="formField" />
				</p>
				<p>
					<form:label path="applicantMobile" cssStyle="color: blue;">Applicant Mobile Number</form:label>
					<form:input path="applicantMobile" cssClass="formField" />
				</p>
				<p>
                    <input type="submit" class="button" value="Authenticate">
                </p>
            </form:form>
        </div>
    </c:if>
    <c:if test="${not empty logged}">
        <div id="logoutSection" class="section form-container">
            <form:form id="logoutForm" method="post" action="${pageContext.request.contextPath}/single-logout"
                modelAttribute="logoutRequest">
                <div id="loggedUser">
                    <p class="horiz-text-padding">
                        <span>Username:</span> <span class="bold"><c:out value="${username}" /></span>
                    </p>
                    <p class="top-padding">
                        <span class="bold">User attributes</span>
                    </p>
                    <table id="attrTable" class="display">
                        <thead>
                            <th>Attribute name</th>
                            <th>Attribute value</th>
                        </thead>
                        <tbody>
                            <c:forEach items="${attributes}" var="attribute" varStatus="status">
                                <tr class="${status.index % 2 == 0 ? 'even' : 'odd'}">
                                    <td><c:out value="${attribute.name}" /></td>
                                    <td><c:out value="${attribute.value}" /></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </form:form>
        </div>
    </c:if>
</body>
</html>
