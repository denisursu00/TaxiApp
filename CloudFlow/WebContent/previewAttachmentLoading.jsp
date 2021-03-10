<%@ page contentType="text/html" pageEncoding="UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
	<head>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<title>CloudDoc ${initParam['CloudDocVersion']}</title>
		<link rel="stylesheet" type="text/css" href="<c:url value="/css/previewAttachmentLoading.css" />">
	</head>
	<body>
		<div id="pleaseWaitArea">
			<spring:message code="[messages]PLEASE_WAIT" />
		</div>
	</body>
</html>