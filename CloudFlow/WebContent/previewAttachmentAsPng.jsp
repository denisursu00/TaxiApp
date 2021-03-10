<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
	<head>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<title>CloudDoc ${initParam['CloudDocVersion']}</title>
		
		<link rel="stylesheet" type="text/css" href="<c:url value="/css/previewAttachmentAsPng.css" />">
		
		<script type="text/javascript" src="<c:url value="/javascript/jquery-1.7.2.min.js" />"></script>
		<script type="text/javascript" src="<c:url value="/javascript/StyleUtils.js" />"></script>
		<script type="text/javascript" src="<c:url value="/javascript/ScrollUtils.js" />"></script>
		<script type="text/javascript" src="<c:url value="/javascript/previewAttachmentAsPng.js" />"></script>
	</head>
	<body>
	
		<div id="zoomButtons">
			<button id="zoomFitButton">Fit</button>
			<button id="zoomInButton">+</button>
			<button id="zoomOutButton">-</button>
		</div>
		
		<c:forEach var="pngFileName" items="${pngFileNames}">
			<img class="pagePreviewImage" src="<c:url value="/actions/temporaryFileDisplay.png?tempFileName=${pngFileName}" />">
		</c:forEach>
	</body>
</html>