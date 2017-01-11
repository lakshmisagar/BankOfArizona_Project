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

<title>Credit Card Page</title>
	<style type="text/css">
		.tg  {border-collapse:collapse;border-spacing:0;border-color:#ccc;}
		.tg td{font-family:Arial, sans-serif;font-size:14px;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;border-color:#ccc;color:#333;background-color:#fff;}
		.tg th{font-family:Arial, sans-serif;font-size:14px;font-weight:normal;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;border-color:#ccc;color:#333;background-color:#f0f0f0;}
		.tg .tg-4eph{background-color:#f9f9f9}
	</style>
</head>
<body>

<h3>Credit Card Account Details</h3>
<c:if test="${!empty listCards}">
	<table class="tg">
	<tr>
		<th width="80">Card ID</th>
		<th width="120">Cardholder Name</th>
		<th width="120">Expiry Date</th>
		<th width="120">CVV</th>
		<th width="120">Credit Limit</th>
		<th width="120">Amount Spent</th>
		<th width="120">Due Date</th>
	</tr>
	<c:forEach items="${listCards}" var="card">
		<tr>
			<td>${card.cardId}</td>
			<td>${card.chName}</td>
			<td>${card.expDate}</td>
			<td>${card.cvv}</td>
			<td>${card.creditLimit}</td>
			<td>${card.amountSpent}</td>
			<td>${card.dueDate}</td>
		</tr>
	</c:forEach>
	</table>
</c:if>

<h2>Make Payment To Merchant</h2>
<form:form action="makePayment"  method="post">
To Account: <input type="text" name="toAcc"/>
Amount: <input type="text" name="amount"/>
<input type="submit" name="payment"
			value="<spring:message text="Make Payment"/>" />
</form:form>

<h2>Pay Credit Card Bill</h2>
<form:form action="payCreditCard"  method="post">
Amount: <input type="text" name="amount"/>
<input type="submit" name="Pay Bill"
			value="<spring:message text="Credit Card Payment"/>" />
</form:form>

<h2>Display Credit Card Transactions</h2>
<form:form action="cctransaction" ModelAttribute="cctransaction"  method="post">
	<input type="submit" name="display" value="<spring:message text="Display transaction"/>" />
</form:form>
<c:if test="${!empty listcctransactions}">
<table border=2>
	<tr>
		<th>Transaction Id</th>
		<th>Amount</th>
		<th>Merchant</th>
		<th>Date</th>
		<th>Description</th>
	</tr>
<c:forEach items="${listcctransactions}" var="listtransactions">
	<tr>
		<td>${listtransactions.ccTranscId} </td>
		<td>${listtransactions.amount} </td>
		<td>${listtransactions.merchant} </td>
		<td>${listtransactions.date}</td>
		<td>${listtransactions.trxDesc}</td>
	</tr>
</c:forEach>
</table>
</c:if>

</body>
</html>