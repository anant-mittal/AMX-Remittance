<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="java.sql.*"%>
<%
String    result = null;
String    postdate = null;
String    tranid   = null;
String    auth     = null;
String    trackid  = null;
String    ref =null;
String    ErrorText = null;
String    url = null;
String    payid = null;

    String paymentId   = request.getParameter("paymentid"); //PaymentID ,paymentid
    String ErrorNo     = request.getParameter("Error");
    String udf1        = request.getParameter("udf1");
    String udf2        = request.getParameter("udf2");
    String udf3        = request.getParameter("udf3");
    String udf4        = request.getParameter("udf4");
    String udf5        = request.getParameter("udf5");
  		   result      = request.getParameter("result");
     String paymentId1  = request.getParameter("PaymentID");
     postdate = request.getParameter("postdate");
     tranid   = request.getParameter("tranid");
     auth     = request.getParameter("auth");
     trackid  = request.getParameter("trackid");
     ref      = request.getParameter("ref");
    
     System.out.println("Result :"+result);
%>

<html>
<head>
<title>First JSP</title>
</head>
<body>

	<h2>Thank you for using AlMulla Payment Service !!!</h2>
    <hr />
    <br/>
	<table style="width: 100%">
		<tr>
			<th>Parameter</th>
			<th>Value</th>
		</tr>
		<tr>
			<td>Result</td>
			<td><%=result%></td>

		</tr>
		<tr>
			<td>Payment Id</td>
			<td><%=paymentId1%></td>

		</tr>

		<tr>
			<td>Track Id</td>
			<td><%=trackid%></td>

		</tr>
		<tr>
			<td>Ref Id</td>
			<td><%=ref%></td>

		</tr>
	</table>


	
</body>
</html>