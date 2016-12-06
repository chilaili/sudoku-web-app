<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
HELLO MATRIX <p>${testo}</p>
    <table id="grid" border="1" style="border-collapse: collapse;">
                <tr class="row">
                    <td class="cell"><input type="text" maxlength="1"></td>
                    <td class="cell"><input type="text" maxlength="1"></td>
                    <td class="cell"><input type="text" maxlength="1"></td>
                    <td class="cell"><input type="text" maxlength="1"></td>
                    <td class="cell"><input type="text" maxlength="1"></td>
                    <td class="cell"><input type="text" maxlength="1"></td>
                    <td class="cell"><input type="text" maxlength="1"></td>
                    <td class="cell"><input type="text" maxlength="1"></td>
                    <td class="cell"><input type="text" maxlength="1"></td>

                    <!--row--></tr>
                     ---------------------------------------------------------
                     ---------------------------------------------------------

                <tr class="row">
                    <td class="cell"><input type="text" maxlength="1"></td>
                    <td class="cell"><input type="text" maxlength="1"></td>
                    <td class="cell"><input type="text" maxlength="1"></td>
                    <td class="cell"><input type="text" maxlength="1"></td>
                    <td class="cell"><input type="text" maxlength="1"></td>
                    <td class="cell"><input type="text" maxlength="1"></td>
                    <td class="cell"><input type="text" maxlength="1"></td>
                    <td class="cell"><input type="text" maxlength="1"></td>
                    <td class="cell"><input type="text" maxlength="1"></td>
                <!--row--></tr>
     </table>
<%-- <p>${matrix}</p>
<c:choose>
  <c:when test="${empty matrix}">
  <h1>error</h1>
   
  </c:when>
  <c:otherwise>
     <c:forEach items="${matrix}" var="number">
     <h2>${number}</h2>
                <c:out value="${number}"/>
                </c:forEach>
  </c:otherwise>
</c:choose> --%>
</body>
</html>