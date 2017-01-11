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
<title>Manage Funds</title>
	<style type="text/css">
		.tg  {border-collapse:collapse;border-spacing:0;border-color:#ccc;}
		.tg td{font-family:Arial, sans-serif;font-size:14px;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;border-color:#ccc;color:#333;background-color:#fff;}
		.tg th{font-family:Arial, sans-serif;font-size:14px;font-weight:normal;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;border-color:#ccc;color:#333;background-color:#f0f0f0;}
		.tg .tg-4eph{background-color:#f9f9f9}
	</style>
</head>
<body>

<h2>Choose Credit/Debit For Transaction</h2>
<form:form action="transaction" ModelAttribute="transaction"  method="post">
	Choose the type of transaction:
	<select  name="select" >
		<option value=""></option>	
		<c:forEach items="${transaction}" var="transaction">
			<option value="${transaction}">${transaction}</option>
		</c:forEach>
	</select>
	<input type="radio" name="debitandcredit" value="debit"> Debit
	<input type="radio" name="debitandcredit" value="credit"> Credit
	Amount:<input type ="text" name="amount" >
	<input type="submit" name="submit"value="<spring:message text="perform transaction"/>"  />
	<c:if test="${!empty  status}">Operation:${status}</c:if>
</form:form>

<h2>Transfer Funds</h2>
<form:form action="transferFund"  method="post">
To Account: <input type="text" name="toAcc"/><br>
To Email: <input type="text" name="toEmail"><br>
To Phone: <input type="text" name="toPhone">
Amount: <input type="text" name="amount"/>
<input type="submit" name="transfer"
			value="<spring:message text="Transfer Money"/>" />
</form:form>

<form:form action="validateOtp"  method="post">
OTP: <input type="text" name="otp"/>
<input type="submit" name="checkOtp"
			value="<spring:message text="Validate Otp"/>" />
</form:form>

</body>
</html>