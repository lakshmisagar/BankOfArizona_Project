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

<title>Pending Approvals By User</title>
	<style type="text/css">
		.tg  {border-collapse:collapse;border-spacing:0;border-color:#ccc;}
		.tg td{font-family:Arial, sans-serif;font-size:14px;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;border-color:#ccc;color:#333;background-color:#fff;}
		.tg th{font-family:Arial, sans-serif;font-size:14px;font-weight:normal;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;border-color:#ccc;color:#333;background-color:#f0f0f0;}
		.tg .tg-4eph{background-color:#f9f9f9}
	</style>
</head>
<body>


<form:form action="approvals" ModelAttribute="transaction"  method="post">
	<input type="submit" name="display" value="<spring:message text="click here to view pending  requests for approvals"/>" />
</form:form>

<c:if test="${!empty usermerchantrequest}"> User:  Merchant requests<br>
<table border=2>
	<tr>
		<th>Transaction Id</th>
		<th>Account number </th>
		<th>Amount</th>
		<th>Transaction_Merchant</th>
		<th>status</th>
	</tr>
	<c:forEach items="${usermerchantrequest}" var="usermerchantrequest">
	<tr>
		<td>${usermerchantrequest.transc_id} </td>
		<td>${usermerchantrequest.transc_acc_num} </td>
		<td>${usermerchantrequest.transc_amt} </td>
		<td>${usermerchantrequest.transc_merchant}</td>
		<c:if test="${empty  approved}">
		<td><a href="<c:url value='/approve-${usermerchantrequest.transc_id}'/>">approve</a></td></c:if></td>
		<c:if test="${!empty  approved}">
		<td>approved</td></c:if>
	</tr>
</c:forEach>
</table>
</c:if>


</body>
</html>