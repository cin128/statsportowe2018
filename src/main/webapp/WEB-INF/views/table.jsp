<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%--  --%>
<link rel="stylesheet"
	href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css">
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<jsp:include page="css/index.css" />
<script>
	$(function() {
		$(document).tooltip();
	});
</script>
<style type="text/css">
.ui-tooltip {
	border-radius: 6px;
	font: 14px "Helvetica Neue", Sans-Serif;
	box-shadow: 0 0 7px black;
}
</style>

<title>Tabela</title>
</head>
<body>
	<c:if test="${not empty rows}">
		<table class="bordered">
			<thead>
				<tr>
					<th colspan="9">${project_name}</th>
				</tr>
				<tr>
					<th><abbr title="Pozycja">Poz</abbr></th>
					<th>Drużyna</th>
					<th><abbr title="Mecze">M</abbr></th>
					<th><abbr title="Punkty">Pkt</abbr></th>
					<th><abbr title="Zwycięstwa">Z</abbr></th>
					<th><abbr title="Remisy">R</abbr></th>
					<th><abbr title="Porażki">P</abbr></th>
					<th>Bramki</th>
					<th class="last-ten-matches">10 ostatnich meczów</th>
				</tr>
			</thead>
			<tbody>
				<c:set var="count" value="0" scope="page" />
				<c:forEach var="row" items="${rows}">
					<c:set var="count" value="${count + 1}" scope="page" />
					<tr>
						<th>${count}</th>
						<td><b>${row.teamName}</b></td>
						<td>${row.games}</td>
						<td><b>${row.points}</b></td>
						<td>${row.wins}</td>
						<td>${row.draws}</td>
						<td>${row.defeats}</td>
						<td>${row.goalsScored}-${row.goalsAgainst}</td>
						
						<!-- <td class="last-ten-matches"><c:forEach var="match"
								items="${row.lastMatches}">
								<c:if test="${match!=null}">
									<c:if test="${match.result==0}">
										<div title="${match.description} ${match.date}"
											class="match_draw">R</div>
									</c:if>
									<c:if test="${match.result>0}">
										<div title="${match.description} ${match.date}"
											class="match_win">Z</div>
									</c:if>
									<c:if test="${match.result<0}">
										<div title="${match.description} ${match.date}"
											class="match_lose">P</div>
									</c:if>
								</c:if>
							</c:forEach></td> -->
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:if>
</body>
</html>