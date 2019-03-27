<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%><%@ taglib prefix="c"
    uri="http://java.sun.com/jsp/jstl/core"%><%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%><%@ taglib
    prefix="x509" uri="http://gemalto.com/ics/rnd/sdk/saml/example/x509"%><%@ taglib prefix="fn"
    uri="http://java.sun.com/jsp/jstl/functions"%><%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"
    session="false"%><!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Khatm SAML SDK Example</title>
<link rel="stylesheet" href="<c:url value="/static/styles/jquery-ui.min.css" />" />
<link rel="stylesheet" href="<c:url value="/static/styles/jquery-ui.theme.min.css" />" />
<link rel="stylesheet" href="<c:url value="/static/styles/main.css" />" />
<script src="<c:url value="/static/js/lib/jquery.min.js"/>" type="text/javascript"></script>
<script src="<c:url value="/static/js/lib/jquery-ui.min.js"/>" type="text/javascript"></script>
<script src="<c:url value="/static/js/configuration.js"/>" type="text/javascript"></script>
</head>
<body>
    <jsp:include page="menu.jsp">
        <jsp:param name="page" value="configuration" />
    </jsp:include>
    <h1>Configuration</h1>
    <c:if test="${not empty infoMessage}">
        <div class="info-message">
            <span><c:out value="${infoMessage}" /></span>
        </div>
    </c:if>
    <c:if test="${not empty activeTabIndex}">
        <input type="hidden" id="activeTabIndex" value="${activeTabIndex}" />
    </c:if>
    <form:form action="${pageContext.request.contextPath}/configuration" method="post" modelAttribute="cfgModel"
        enctype="multipart/form-data" id="cfgForm">
        <c:set var="DATE_PATTERN" value="d/M/yyyy H:mm:ss" />
        <div id="tabs">
            <ul>
                <li id="spTab"><a href="#spMetadataTab">SP Metadata</a></li>
                <li><a href="#spCredentialsTab">SP Credentials</a></li>
                <li id="idpTab"><a href="#idpMetadataTab">IDP Metadata</a></li>
            </ul>
            <div id="spMetadataTab">
                <div class="container">
                    <p class="left-padding font-size--smaller">
                        <span>Import configuration from the SP metadata XML file</span> <input type="file"
                            id="spMetadataFile" name="spMetadataFile">
                        <form:errors path="spMetadataFile" cssClass="error" />
                    </p>
                    <div class="form-container float-left">
                        <p>
                            <form:label path="configuration.spMetadata.entityId">Entity ID</form:label>
                            <form:input path="configuration.spMetadata.entityId" cssClass="formField" />
                        </p>
                        <p>
                            <form:label path="configuration.spMetadata.acsLocation">Assertion consumer service location</form:label>
                            <form:input path="configuration.spMetadata.acsLocation" cssClass="formField" />
                        </p>
                        <p>
                            <form:label path="configuration.spMetadata.authnRequestsSigned"
                                for="configuration.spMetadata.authnRequestsSigned1">Signed authentication requests</form:label>
                            <form:checkbox path="configuration.spMetadata.authnRequestsSigned" />
                        </p>
                    </div>
                </div>
            </div>
            <div id="spCredentialsTab">
                <div class="container">
                    <div class="form-container">
                        <p>
                            <form:label path="configuration.spSigningCredential.signatureAlgorithm">Signature algorithm</form:label>
                            <form:select path="configuration.spSigningCredential.signatureAlgorithm"
                                items="${signatureAlgos}" cssClass="formField" />
                        </p>
                        <p>
                            <form:label path="configuration.spSigningCredential.digestMethod">Digest method</form:label>
                            <form:select path="configuration.spSigningCredential.digestMethod" items="${digestMethods}"
                                cssClass="formField" />
                        </p>
                    </div>

                    <div class="form-container">
                        <fieldset>
                            <legend>SP signing certificate</legend>
                            <c:set var="spSigningCertificate"
                                value="${x509:certificate(cfgModel.configuration.spSigningCredential.certificate)}" />
                            <p>
                                <label>Subject</label><input value="${fn:escapeXml(spSigningCertificate.subject)}"
                                    disabled="true" class="formField--long" />
                            </p>
                            <p>
                                <label>Issuer</label><input value="${fn:escapeXml(spSigningCertificate.issuer)}"
                                    disabled="true" class="formField--long" />
                            </p>
                            <p>
                                <label>Valid from</label>
                                <fmt:formatDate value="${spSigningCertificate.notBefore}" pattern="${DATE_PATTERN}"
                                    var="date" />
                                <input value="${fn:escapeXml(date)}" disabled="true" class="formField" />
                            </p>
                            <p>
                                <label>Valid to</label>
                                <fmt:formatDate value="${spSigningCertificate.notAfter}" pattern="${DATE_PATTERN}"
                                    var="date" />
                                <input value="${fn:escapeXml(date)}" disabled="true" class="formField" />
                            </p>
                            <p class="font-size--smaller">
                                <span>Import SP key and certificate from p12 file</span> <input type="file"
                                    id="spPrivateKeyFile" name="spPrivateKeyFile">
                                <form:errors path="spPrivateKeyFile" cssClass="error" />
                            </p>
                            <p id="spPrivateKeyPasswordBlock">
                                <form:label path="spPrivateKeyPassword">P12 password</form:label>
                                <form:password path="spPrivateKeyPassword" cssClass="formField" />
                            </p>
                        </fieldset>
                    </div>
                    <div class="form-container">
                        <fieldset>
                            <legend>SP encryption certificate</legend>
                            <c:set var="spEncryptionCertificate"
                                value="${x509:certificate(cfgModel.configuration.spEncryptingCredential.certificate)}" />
                            <p>
                                <label>Subject</label><input value="${fn:escapeXml(spEncryptionCertificate.subject)}"
                                    disabled="true" class="formField--long" />
                            </p>
                            <p>
                                <label>Issuer</label><input value="${fn:escapeXml(spEncryptionCertificate.issuer)}"
                                    disabled="true" class="formField--long" />
                            </p>
                            <p>
                                <label>Valid from</label>
                                <fmt:formatDate value="${spEncryptionCertificate.notBefore}" pattern="${DATE_PATTERN}"
                                    var="date" />
                                <input value="${fn:escapeXml(date)}" disabled="true" class="formField" />
                            </p>
                            <p>
                                <label>Valid to</label>
                                <fmt:formatDate value="${spEncryptionCertificate.notAfter}" pattern="${DATE_PATTERN}"
                                    var="date" />
                                <input value="${fn:escapeXml(date)}" disabled="true" class="formField" />
                            </p>
                            <p class="font-size--smaller">
                                <span>Import SP key and certificate from p12 file</span> <input type="file"
                                    id="spEncryptionPrivateKeyFile" name="spEncryptionPrivateKeyFile">
                                <form:errors path="spEncryptionPrivateKeyFile" cssClass="error" />
                            </p>
                            <p id="spEncryptionPrivateKeyPasswordBlock">
                                <form:label path="spEncryptionPrivateKeyPassword">P12 password</form:label>
                                <form:password path="spEncryptionPrivateKeyPassword" cssClass="formField" />
                            </p>
                        </fieldset>

                    </div>
                </div>
            </div>
            <div id="idpMetadataTab">
                <div class="container">
                    <p class="left-padding font-size--smaller">
                        <span>Import configuration from the IDP metadata XML file</span> <input type="file"
                            id="idpMetadataFile" name="idpMetadataFile">
                        <form:errors path="idpMetadataFile" cssClass="error" />
                    </p>
                    <div class="form-container float-left">
                        <p>
                            <form:label path="configuration.idpMetadata.entityId">Entity ID</form:label>
                            <form:input path="configuration.idpMetadata.entityId" cssClass="formField" />
                        </p>
                        <p>
                            <form:label path="configuration.idpMetadata.ssoLocation">Single sign-on service location</form:label>
                            <form:input path="configuration.idpMetadata.ssoLocation" cssClass="formField" />
                        </p>
                    </div>
                    <div class="form-container display-inline-block">
                        <p>IDP certificate</p>
                        <c:set var="idpCertificate"
                            value="${x509:certificate(cfgModel.configuration.idpMetadata.signingCertificates)}" />
                        <div>
                            <p>
                                <label>Subject</label><input value="${fn:escapeXml(idpCertificate.subject)}"
                                    disabled="true" class="formField--long" />
                            </p>
                            <p>
                                <label>Issuer</label><input value="${fn:escapeXml(idpCertificate.issuer)}"
                                    disabled="true" class="formField--long" />
                            </p>
                            <p>
                                <label>Valid from</label>
                                <fmt:formatDate value="${idpCertificate.notBefore}" pattern="${DATE_PATTERN}" var="date" />
                                <input value="${fn:escapeXml(date)}" disabled="true" class="formField" />
                            </p>
                            <p>
                                <fmt:formatDate value="${idpCertificate.notAfter}" pattern="${DATE_PATTERN}" var="date" />
                                <label>Valid to</label><input value="${fn:escapeXml(date)}" disabled="true"
                                    class="formField" />
                            </p>
                            <p class="font-size--smaller">
                                <span>Import IDP certificate</span> <input type="file" id="idpCertificateFile"
                                    name="idpCertificateFile">
                                <form:errors path="idpCertificateFile" cssClass="error" />
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <p class="form-container">
            <input type="submit" class="button" value="Save changes">
        </p>
    </form:form>
</body>
</html>
