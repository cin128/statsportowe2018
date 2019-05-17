<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Status</title>
</head>
<body>
	<center>
		<h2>Projects update status</h2>
	</center>

	<c:if test="${not empty rows}">
		<table class="bordered">
			<thead>
				<tr>
					<th colspan="9">${project_name}</th>
				</tr>
				<tr>
					<th><abbr title="Pozycja">l.p.</abbr></th>
					<th>Job ID</th>
					<th><abbr title="Progress">Progress</abbr></th>
					<th><abbr title="Processing time">Processing time</abbr></th>
					<th><abbr title="Srart">Start</abbr></th>
					<th><abbr title="End">End</abbr></th>
					<th><abbr title="Status">Status</abbr></th>
					<th><abbr title="End status">End status</abbr></th>
					<th><abbr title="Job name">Job name</abbr></th>
                    <th><abbr title="Step name">Step name</abbr></th>
					<th><abbr title="Count">Count</abbr></th>
				</tr>
			</thead>
			<tbody>
				<c:set var="count" value="0" scope="page" />
				<c:forEach var="row" items="${rows}">
					<c:set var="count" value="${count + 1}" scope="page"/>
					<tr>
						<th>${count}</th>
						<td><b>${row.jobExecution.id}</b></td>
						<td>${row.progress}</td>
						<td>${row.processingTime}</td>
						<td><b>${row.jobExecution.status}</b></td>
						<td><b>${row.jobExecution.exitStatus.exitCode}</b></td>
						<td><b>${row.jobExecution.jobInstance.jobName}</b></td>
						<td colspan="4">
						<table>
							<c:forEach var="step" items="${row.jobExecution.stepExecutions}">
								<tr>
									<td><b>${step.startTime}</b></td>
									<td><b>${step.endTime}</b></td>
									<td><b>${step.stepName}</b></td>
									<td><b>${step.readCount}</b></td>
								</tr>
							</c:forEach>
						</table>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:if>
</body>
</html>
