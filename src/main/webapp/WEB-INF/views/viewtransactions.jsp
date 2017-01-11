<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>Transaction Details</title>
	<style type="text/css">
		.tg  {border-collapse:collapse;border-spacing:0;border-color:#ccc;}
		.tg td{font-family:Arial, sans-serif;font-size:14px;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;border-color:#ccc;color:#333;background-color:#fff;}
		.tg th{font-family:Arial, sans-serif;font-size:14px;font-weight:normal;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;border-color:#ccc;color:#333;background-color:#f0f0f0;}
		.tg .tg-4eph{background-color:#f9f9f9}
	</style>
</head>
<body>

<h2>Click Below To Download Account Statement</h2>
<a href="<c:url value='/downloadPDF' />" >Download</a>

<h2>Choose Account To Display Transactions</h2>
<form:form action="transaction" ModelAttribute="transaction"  method="post">
	<input type="submit" name="display" value="<spring:message text="Display transaction"/>" />
</form:form>

<c:if test="${!empty listtransactions}">
<table border=2>
	<tr>
		<th>Transaction Id</th>
		<th>Account number </th>
		<th>Amount</th>
		<th>Transction_type_id</th>
		<th>Transaction_Merchant</th>
		<th>Transaction_Fee</th>
		<th>Description</th>
		<th>Status</th>
	</tr>
<c:forEach items="${listtransactions}" var="listtransactions">
	<tr>
		<td>${listtransactions.transc_id} </td>
		<td>${listtransactions.transc_acc_num} </td>
		<td>${listtransactions.transc_amt} </td>
		<td>${listtransactions.transc_type_id}</td>
		<td>${listtransactions.transc_merchant}</td>
		<td>${listtransactions.transc_fee}</td>
		<td>${listtransactions.transc_desc}</td>
		<td>${listtransactions.transc_status_Id}</td>
	</tr>
</c:forEach>
</table>
</c:if>


</body>
</html>