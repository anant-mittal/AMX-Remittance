<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%><%@ taglib prefix="c"
    uri="http://java.sun.com/jsp/jstl/core"%><div class="top-menu">
    <span class="top-menu-item <c:if test="${param.page eq 'configuration'}">top-menu-item--selected</c:if>"><a
        href="${pageContext.request.contextPath}/configuration">Configuration</a></span><span
        class="top-menu-item <c:if test="${param.page eq 'authentication'}">top-menu-item--selected</c:if>"><a
        href="${pageContext.request.contextPath}/single-sign-on">Oman Khatm Authentication</a></span>
    <script src="${pageContext.request.contextPath}/static/js/menu.js" type="text/javascript"></script>
</div>
