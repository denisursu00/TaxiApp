<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
  <head>
  	<title>Select account</title>
  </head>
  <body>
  	<form action="${pageContext.request.contextPath}/j_spring_security_check" method="post">
		<input type="hidden" name="username" value="${username}">
		<input type="hidden" name="password" value="${password}">
		<table>
			<c:forEach items="${usersWithUsername}" var="user">
				<tr>
					<td>
						<input type="radio" name="id" value="${user.id}">
					</td>
					<td>
						${user.title}	
					</td>							
				</tr>			
			</c:forEach>
			<tr>
				<td></td>
				<td>
					<input type="submit" value="LOGIN">
				</td>
			</tr>
		</table>
  	</form>
  </body>
</html>