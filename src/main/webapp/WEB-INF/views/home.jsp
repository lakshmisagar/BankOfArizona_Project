<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Home page</title>
</head>
<body>
	<div class="generic-container">
		<div class="authbar">
	 		<span>Dear <strong>${loggedinuser}</strong>,</span> <span class="floatRight">
		 		<a href="<c:url value='/list' />">List</a></span>
	 	</div>
	 	
	 	<sec:authorize access="hasRole('USER')">
		 	<div class="well">
		 		<a href="<c:url value='/accounts' />">View Account</a>
		 	</div>
		 	<div class="well">
		 		<a href="<c:url value='/managefunds' />">Manage Funds</a>
		 	</div>
		 	<div class="well">
		 		<a href="<c:url value='/viewtransactions' />">View Transactions</a>
		 	</div>
		 	<div class="well">
		 		<a href="<c:url value='/creditcard' />">Credit Card</a>
		 	</div>
		 	<div class="well">
		 		<a href="<c:url value='/pendingapprovalsbyuser' />">pending approvals</a>
		 	</div>
		</sec:authorize>
		<sec:authorize access="hasRole('MERCHANT')">
		 	<div class="well">
		 		<a href="<c:url value='/accounts' />">View Account</a>
		 	</div>
		 	<div class="well">
		 		<a href="<c:url value='/managefunds' />">Manage Funds</a>
		 	</div>
		 	<div class="well">
		 		<a href="<c:url value='/viewtransactions' />">View Transactions</a>
		 	</div>
		 	<div class="well">
		 		<a href="<c:url value='/creditcard' />">Credit Card</a>
		 	</div>
		 	<div class="well">
		 		<a href="<c:url value='/submitpaymentbymerchant' />">submit payment</a>
		 	</div>
		</sec:authorize>
	 	<sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
		 	<div class = "well">
		 		<a href="<c:url value='/pending_trans' />">Pending Transactions</a> 	
		 	</div>
	 	</sec:authorize>
	 	
	</div>
</body>
</html>