package com.amx.jax.model.response.serviceprovider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ServiceProviderResponse
   {
      String action_ind, response_code, response_description, technical_details, request_XML, response_XML;

      public String getAction_ind()
	 {
	    return action_ind;
	 }

      public void setAction_ind(String action_ind)
	 {
	    this.action_ind = action_ind;
	 }

      public String getResponse_code()
	 {
	    return response_code;
	 }

      public void setResponse_code(String response_code)
	 {
	    this.response_code = response_code;
	 }

      public String getResponse_description()
	 {
	    return response_description;
	 }

      public void setResponse_description(String response_description)
	 {
	    this.response_description = response_description;
	 }

      public String getTechnical_details()
	 {
	    return technical_details;
	 }

      public void setTechnical_details(String technical_details)
	 {
	    this.technical_details = technical_details;
	 }

      public String getRequest_XML()
	 {
	    return request_XML;
	 }

      public void setRequest_XML(String request_XML)
	 {
	    this.request_XML = request_XML;
	 }

      public String getResponse_XML()
	 {
	    return response_XML;
	 }

      public void setResponse_XML(String response_XML)
	 {
	    this.response_XML = response_XML;
	 }

      @Override
      public String toString()
	 {
	    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss zzz");
	    sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
	    Date date = new Date();
	    String formated_date = sdf.format(date);

	    return "\n--------------------------------------------------------------------------------------------------------------------\n" + String.format("%-49s",
																			      " Response Code")
		   + ": "
		   + getResponse_code()
		   + "\n-------------------------------------------------------------------------------------------------------------------"
		   + String.format("%-50s",
				   "\n Action Indicator")
		   + ": "
		   + getAction_ind()
		   + "\n-------------------------------------------------------------------------------------------------------------------"
		   + String.format("%-50s",
				   "\n Response Description")
		   + ": "
		   + getResponse_description()
		   + "\n-------------------------------------------------------------------------------------------------------------------"
		   + String.format("%-50s",
				   "\n Time Now")
		   + ": "
		   + formated_date
		   + "\n-------------------------------------------------------------------------------------------------------------------"
		   + String.format("%-50s",
				   "\n Exception Stack: ")
		   + getTechnical_details()
		   + "\n-------------------------------------------------------------------------------------------------------------------"
		   + String.format("%-50s",
				   "\n XML Request")
		   + ": "
		   + getRequest_XML()
		   + "\n-------------------------------------------------------------------------------------------------------------------"
		   + String.format("%-50s",
				   "\n XML Response")
		   + ": "
		   + getResponse_XML()
		   + "\n--------------------------------------------------------------------------------------------------------------------\n";
	 }
   }