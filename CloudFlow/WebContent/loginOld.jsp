<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
  <head>
  	<title>Login</title>
  </head>
  <body>
  	<form action="${pageContext.request.contextPath}/j_spring_security_check" method="post">
		<input type="hidden" name="requestedModule" value="${param.requestedModule}">
  		<table>
  			<tr>
  				<td>
  					Username
  				</td>
  				<td>
  					<input type="text" name="username">
  				</td>
  			</tr>
  			<tr>
  				<td>
  					Password
  				</td>
  				<td>
  					<input type="password" name="password">
  				</td>
  			</tr>
  			<tr>
  				<td>
  				</td>
  				<td>
  					<input type="submit" value="LOGIN">
  				</td>
  			</tr>
  		</table>
  		<c:if test="${not empty param.authFailed}">
  			<br>
            <div style="color: red">
                Eroare la autentificare: <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}" />.
            </div>
        </c:if>
  	</form>
  </body>
</html>