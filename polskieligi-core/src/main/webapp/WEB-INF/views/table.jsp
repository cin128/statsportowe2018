<%@ page language="java" contentType="text/html; charset=utf-8"
		 pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

	<!-- jquery -->
	<script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
	<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
	<link rel="stylesheet"
		  href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">

	<link href="<c:url value="/css/index.css" />" rel="stylesheet">

	<script>
        $(document).ready(function() {
            $("#address").autocomplete({
                source : function(request, response) {
                    $.ajax({
                        url : "${pageContext.request.contextPath }/teams",
                        dataType : "json",
                        data : {
                            team : request.term,
                            maxRows: 2
                        },
                        success : function(data) {
                            //alert(data);
                            //console.log(data);
                            response(data);
                        }
                    });
                },
                select: function(event, ui){
                    $("#address").val(ui.item.label);
                    $("#addressId").val(ui.item.value);
                    return false;
                },
                minLength : 2
            });
        });

	</script>

	<script>
        $(function() {
            $(document).tooltip();
        });

        $( function() {
            $( "#tabs" ).tabs();
        } );
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

<input type="text"  id="address" value=""/>
<input type="hidden"  id="addressId" value=""/>
<span>
  <button id="button-id" type="button">Search</button>
</span>

<c:if test="${not empty rows}">

	<div>${project_name}</div>
	<div id="tabs">
		<div id="menu">
			<ul class="buttons">
				<li><a class="active" href="#tabs-all">Ogółem</a></li>
				<li><a href="#tabs-home">Dom</a></li>
				<li><a href="#tabs-away">Wyjazd</a></li>
			</ul>
		</div>
		<div id="tabs-all">

			<table>
				<thead>
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
						<td class="team_name"><b>${row.teamName}</b></td>
						<td>${row.games}</td>
						<td><b>${row.points}</b></td>
						<td>${row.wins}</td>
						<td>${row.draws}</td>
						<td>${row.defeats}</td>
						<td>${row.goalsScored}-${row.goalsAgainst}</td>

						<td class="last-ten-matches"><c:forEach var="match"
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
						</c:forEach></td>
					</tr>
				</c:forEach>
				</tbody>
			</table>

		</div>

		<div id="tabs-home">
			<table>
				<thead>
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
				<c:forEach var="row" items="${rowsHome}">
					<c:set var="count" value="${count + 1}" scope="page" />
					<tr>
						<th>${count}</th>
						<td class="team_name"><b>${row.teamName}</b></td>
						<td>${row.gamesHome}</td>
						<td><b>${row.pointsHome}</b></td>
						<td>${row.winsHome}</td>
						<td>${row.drawsHome}</td>
						<td>${row.defeatsHome}</td>
						<td>${row.goalsScoredHome}-${row.goalsAgainstHome}</td>

						<td class="last-ten-matches"><c:forEach var="match"
																items="${row.lastMatchesHome}">
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
						</c:forEach></td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
		</div>
		<div id="tabs-away">
			<table>
				<thead>
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
				<c:forEach var="row" items="${rowsAway}">
					<c:set var="count" value="${count + 1}" scope="page" />
					<tr>
						<th>${count}</th>
						<td class="team_name"><b>${row.teamName}</b></td>
						<td>${row.gamesAway}</td>
						<td><b>${row.pointsAway}</b></td>
						<td>${row.winsAway}</td>
						<td>${row.drawsAway}</td>
						<td>${row.defeatsAway}</td>
						<td>${row.goalsScoredAway}-${row.goalsAgainstAway}</td>

						<td class="last-ten-matches"><c:forEach var="match"
																items="${row.lastMatchesAway}">
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
						</c:forEach></td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
		</div>
	</div>


</c:if>
</body>
</html>