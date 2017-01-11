<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ page session="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>Account Page</title>
	<style type="text/css">
		.tg  {border-collapse:collapse;border-spacing:0;border-color:#ccc;}
		.tg td{font-family:Arial, sans-serif;font-size:14px;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;border-color:#ccc;color:#333;background-color:#fff;}
		.tg th{font-family:Arial, sans-serif;font-size:14px;font-weight:normal;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;border-color:#ccc;color:#333;background-color:#f0f0f0;}
		.tg .tg-4eph{background-color:#f9f9f9}
	</style>
</head>
<body>



<!-- @Column(name="ACC_ID")
	private int account_no;
	@Column(name="EXT_USER_ID")
	private int uid;
	@Column(name="ACC_TYPE_ID")
	private String account_type_id;
	@Column(name="BALANCE")
	private double balance;
	@Column(name="DEBIT_CARD_NUM")
	private int dc_no;
	@Column(name="DEBIT_CARD_EXP_DATE")
	private String debit_exp_date;
	@Column(name="DEBIT_CARD_CVV")
	private int cvv;
	@Column(name="DEBIT_CARD_HOLDER_NAME")
	private String card_holder_name;
	@Column(name="ACC_STATUS")
	private String status; -->

<c:url var="addAction" value="/account/add" ></c:url>
<%-- 
<form:form action="${addAction}" commandName="account">
<table>
	<c:if test="${!empty account.account_no}">
	<tr>
		<td>
			<form:label path="account_no">
				<spring:message text="Account ID"/>
			</form:label>
		</td>
		<td>
			<form:input path="account_no" readonly="true" size="8"  disabled="true" />
			<form:hidden path="account_no" />
		</td> 
	</tr>
	</c:if>
	<tr>
		<td>
			<form:label path="card_holder_name">
				<spring:message text="Name of card holder"/>
			</form:label>
		</td>
		<td>
			<form:input path="card_holder_name" />
		</td> 
	</tr>
	
	<tr>
		<td>
			<form:label path="uid">
				<spring:message text="User id"/>
			</form:label>
		</td>
		<td>
			<form:input path="uid" />
		</td> 
	</tr>
	<tr>
		<td>
			<form:label path="balance">
				<spring:message text="balance"/>
			</form:label>
		</td>
		<td>
			<form:input path="balance" />
		</td> 
	</tr>
	<tr>
		<td>
			<form:label path="dc_no">
				<spring:message text="Debit card number"/>
			</form:label>
		</td>
		<td>
			<form:input path="dc_no" />
		</td> 
	</tr>
	<tr>
		<td>
			<form:label path="debit_exp_date">
				<spring:message text="Exp date"/>
			</form:label>
		</td>
		<td>
			<form:input path="debit_exp_date" />
		</td> 
	</tr>
	<tr>
		<td>
			<form:label path="status">
				<spring:message text="status"/>
			</form:label>
		</td>
		<td>
			<form:input path="status" />
		</td>
	</tr>
	<tr>
		<td>
			<form:label path="status">
				<spring:message text="status"/>
			</form:label>
		</td>
		<td>
			<form:input path="status" />
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<c:if test="${!empty account.account_no}">
				<input type="submit"
					value="<spring:message text="Edit Account"/>" />
			</c:if>
			<c:if test="${empty account.account_no}">
				<input type="submit"
					value="<spring:message text="Add Account"/>" />
			</c:if>
		</td>
	</tr>
</table>	
</form:form>
<br>

<h2>Tranfer Fund</h2>
<form:form action="transferFund"  method="post">
To Account: <input type="text" name="toAcc"/>
Amount: <input type="text" name="amount"/>
<input type="submit" name="transfer"
			value="<spring:message text="Transfer Money"/>" />
</form:form>



<a href="<c:url value='/transferFund/2 ' />" >Download Statement</a>
 --%>
<h2>${transactions}</h2>
<c:if test="${!empty transactions}">
	
	<table class="tg">
	<tr>
		<th width="80">Transaction ID</th>
		<th width="120">Transaction Number</th>
		<th width="120">Transaction Amount</th>
	</tr>
	<sec:authorize access="hasRole('MANAGER')">
	<h1> I DO KNOW THAT YOU ARE A MANAGER! </h1>
	<c:forEach items="${transactions}" var="account">
		<c:if test="${account.trxAmount > 500 }"> 
		<tr>
			<td>${account.transactionId}</td>
			<td>${account.trxAccNum}</td>
			<td>${account.trxAmount}</td>
			<td><a href="<c:url value='/approve-${account.transactionId}' />" class="btn btn-danger custom-width">Approve</a></td>
			<%-- <td><a href="<c:url value='/edit/${account.tranasctionId}' />" >Edit</a></td>
			<td><a href="<c:url value='/remove/${account.trxAccNum}' />" >Delete</a></td> --%>
		</tr>
	</c:if>
	</c:forEach>
	</sec:authorize>
	
	<sec:authorize access="hasRole('ADMIN') or hasRole('REGEMPLOYEE')">
	<c:forEach items="${transactions}" var="account">
		<tr>
			<td>${account.transactionId}</td>
			<td>${account.trxAccNum}</td>
			<td>${account.trxAmount}</td>
			<td><a href="<c:url value='/approve-${account.transactionId}' />" class="btn btn-danger custom-width">Approve</a></td>
			<%-- <td><a href="<c:url value='/edit/${account.tranasctionId}' />" >Edit</a></td>
			<td><a href="<c:url value='/remove/${account.trxAccNum}' />" >Delete</a></td> --%>
		</tr>
	</c:forEach>
	</sec:authorize>
	</table>
</c:if> 

</body>
</html>