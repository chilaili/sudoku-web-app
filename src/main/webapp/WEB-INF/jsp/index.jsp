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

	<table id="sudokutable">
		<caption>
			<h1>
				<b>SUDOKU</b>
			</h1>
		</caption>
		<tbody>
			<c:forEach items="${matrix}" var="row" varStatus="rowStatus">
				<tr class="row">
					<c:forEach items="${row}" var="cell" varStatus="columnStatus">
						<td class="cell">
							<input type="text" id="matrix${rowStatus.index}${columnStatus.index}"
								name="matrix${rowStatus.index}${columnStatus.index}" maxlength="1"
								<c:if test="${cell!=0}"> value="${cell}" readonly</c:if>
								onkeyup="checkCell(this);" onkeypress="return isNumberKey(event);" />
						</td>
					</c:forEach>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<div id="containerButtons">
		<input class='ph-button ph-btn-color' type="button" value="check answers"
			id="checkAnswers" name="checkAnswers" />
		<input class='ph-button ph-btn-color' type="button" id="solvePuzzle"
			name="solvePuzzle" value="solve puzzle" />
		<input class='ph-button ph-btn-color' type="button" id="resetter"
			name="resetter" value="reset table" />
		<form action="">
			<input class='ph-button ph-btn-color' type="submit" id="newGame"
				name="newGame" value="new game" />
		</form>
		<div>
			<input class='ph-button ph-btn-color' type="checkbox" id="helpme"
				name="helpme" />
			Help Me
		</div>
	</div>
	<div id="display"></div>
</body>
</html>