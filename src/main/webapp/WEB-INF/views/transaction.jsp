<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>
<html>
<head>
	<title>User Page</title>
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
	<c:if test="${!empty  status}">${status}</c:if>
</form:form>

<h2>Choose Account To Display Transactions</h2>
<form:form action="transaction" ModelAttribute="transaction"  method="post">
	<select  name="select" >
		<option value=""></option>	
		<c:forEach items="${transaction}" var="transaction">
			<option value="${transaction}">${transaction}</option>
		</c:forEach>
	</select>
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


<br>${userprof}</br>
<c:if test="${!empty  userprof}"> Merchant :Submit payment on behalf of user 



	<form:form action="merchantpayment" ModelAttribute="transaction"  method="post">


		
			
		
		
		user_account_no:<input type ="text" name="accountno" ><br></br>
		 amount:<input type ="text" name="amount" ><br><br>
		
		<input type="submit" name="submitonbehalfofuser"
			value="<spring:message text="submit payment"/>"  />
			
</form:form>

</c:if>



<c:if test="${!empty  usermerchantrequest}"> User:  Merchant requests<br>
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
<td><a href="<c:url value='/approve/${usermerchantrequest.transc_id}'/>">approve</a></td></c:if></td>
<c:if test="${!empty  approved}">
<td>approved</td></c:if>
</tr>
</c:forEach>
</table>
</c:if>


</body>
</html>
