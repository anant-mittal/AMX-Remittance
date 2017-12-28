<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
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
        
      if (result!=null && result.equalsIgnoreCase("CAPTURED"))  {
            result   = request.getParameter("result");
            postdate = request.getParameter("postdate");
            tranid   = request.getParameter("tranid");
            auth     = request.getParameter("auth");
            trackid  = request.getParameter("trackid");
            ref      = request.getParameter("ref");
            out.println("REDIRECT=https://applications2.almullagroup.com/payment-service/jsp/knet_success.jsp?PaymentID="+paymentId+"&result="+result+"&auth="+auth+"&ref="+ref+"&postdate="+postdate+"&trackid="+trackid+"&tranid="+tranid+"&udf1="+udf1+"&udf2="+udf2+"&udf3="+udf3+"&udf4="+udf4+"&udf5="+udf5);
           
        }else if(result!=null && result.equalsIgnoreCase("CANCELED")) {
        	 ErrorText=request.getParameter("ErrorText");
        	 result   = request.getParameter("result");
             postdate = request.getParameter("postdate");
             tranid   = request.getParameter("tranid");
             auth     = request.getParameter("auth");
             trackid  = request.getParameter("trackid");
             ref      = request.getParameter("ref");
             out.println("REDIRECT=https://applications2.almullagroup.com/payment-service/jsp/knet_cancelled.jsp?PaymentID="+paymentId+"&result="+result+"&auth="+auth+"&ref="+ref+"&postdate="+postdate+"&trackid="+trackid+"&tranid="+tranid+"&udf1="+udf1+"&udf2="+udf2+"&udf3="+udf3+"&udf4="+udf4+"&udf5="+udf5);

        } else {
            ErrorText=request.getParameter("ErrorText");
            
            result   = request.getParameter("result");
            postdate = request.getParameter("postdate");
            tranid   = request.getParameter("tranid");
            auth     = request.getParameter("auth");
            trackid  = request.getParameter("trackid");
            ref      = request.getParameter("ref");
            if(paymentId==null){
            	paymentId = request.getParameter("PaymentID"); //PaymentID ,paymentid
            }
            out.println("REDIRECT=https://applications2.almullagroup.com/payment-service/jsp/error.jsp?PaymentID="+paymentId+"&result="+result+"&auth="+auth+"&ref="+ref+"&postdate="+postdate+"&trackid="+trackid+"&tranid="+tranid+"&udf1="+udf1+"&udf2="+udf2+"&udf3="+udf3+"&udf4="+udf4+"&udf5="+udf5);
        }
        
%>