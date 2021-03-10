<%@ page import="org.apache.commons.lang.StringUtils" %>

<%
String url = request.getContextPath() + "/redirectToDefaultModule.do";

String queryString = request.getQueryString();
if (StringUtils.isNotEmpty(queryString)) {
	url += ("?" + queryString);
}

response.sendRedirect(url);
%>