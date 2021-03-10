<%@ page contentType="text/html" pageEncoding="UTF-8" %>

<%@ taglib uri="/WEB-INF/TLD/app" prefix="app" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
	<head>
		<%--
	 	Am comentat aceasta secventa de EL si am inlocuit cu scripleti pentru ca
	 	se pare ca IBM Rational Application Developer inca nu a auzit de EL.
	 	
	 	<c:if test="${!empty app:getCurrentLocale(pageContext.session)}">
			<meta name="gwt:property" content="locale=${app:getCurrentLocale(pageContext.session)}">
		</c:if>
		--%>
		<%@ include file="/WEB-INF/include/gwtLocalePropertyMeta.jspf" %>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<title>CloudDoc ${initParam['CloudDocVersion']}</title>
		<script type="text/javascript" language="javascript" src="cloudDocClientModule/cloudDocClientModule.nocache.js"></script>
		<script type="text/javascript" src="javascript/loadingManager.js"></script>
		<link rel="stylesheet" type="text/css" href="css/loadingManager.css">
	</head>
	<body>
		<div id="x-desktop">
			<dl id="x-shortcuts"></dl>
		</div>
	</body>
</html>