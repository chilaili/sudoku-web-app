<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sudoku</title>
<link rel="stylesheet" type="text/css" href="css/sudoku.css" />
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script src="js/scripts.js"></script>
</head>
<body>
	<form>
		<table>
			<caption>SUDOKU</caption>

			<c:forEach items="${sudoku.puzzle}" var="row" varStatus="rowStatus">
				<tr class="row">
<c:forEach items="${row}" var="cell" varStatus="columnStatus">
						<td class="cell"><input type="text"
							id="sudoku.puzzle[${rowStatus.index}][${columnStatus.index}]"
							name="sudoku.puzzle[${rowStatus.index}][${columnStatus.index}]" maxlength="1"
							value="${cell}"></td>
					</c:forEach>
				</tr>
			</c:forEach>

		</table>
		<input type="button" id="test"value="submit" />
	</form>
	<%--<c:choose>
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