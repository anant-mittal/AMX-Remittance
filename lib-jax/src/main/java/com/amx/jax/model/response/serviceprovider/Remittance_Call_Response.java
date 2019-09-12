package com.amx.jax.model.response.serviceprovider;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Remittance_Call_Response extends ServiceProviderResponse
   {
      Calendar expected_completion_date;
      String bene_bank_remittance_reference;
      String out_going_transaction_reference; // transaction number generated locally

      public Calendar getExpected_completion_date()
	 {
	    return expected_completion_date;
	 }

      public void setExpected_completion_date(Calendar expected_completion_date)
	 {
	    this.expected_completion_date = expected_completion_date;
	 }

      public String getBene_bank_remittance_reference()
	 {
	    return bene_bank_remittance_reference;
	 }

      public void setBene_bank_remittance_reference(String bene_bank_remittance_reference)
	 {
	    this.bene_bank_remittance_reference = bene_bank_remittance_reference;
	 }

      public String getOut_going_transaction_reference() {
		return out_going_transaction_reference;
	}

	public void setOut_going_transaction_reference(String out_going_transaction_reference) {
		this.out_going_transaction_reference = out_going_transaction_reference;
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
		   + super.getResponse_code()
		   + "\n-------------------------------------------------------------------------------------------------------------------"
		   + String.format("%-50s",
				   "\n Action Indicator")
		   + ": "
		   + super.getAction_ind()
		   + "\n-------------------------------------------------------------------------------------------------------------------"
		   + String.format("%-50s",
				   "\n Response Description")
		   + ": "
		   + super.getResponse_description()
		   + "\n-------------------------------------------------------------------------------------------------------------------"
		   + String.format("%-50s",
				   "\n Time Now")
		   + ": "
		   + formated_date
		   + "\n-------------------------------------------------------------------------------------------------------------------"
		   + String.format("%-50s",
				   "\n Exception Stack: ")
		   + super.getTechnical_details()
		   + "\n-------------------------------------------------------------------------------------------------------------------"
		   + String.format("%-50s",
				   "\n Expected Completion Date")
		   + ": "
		   + (expected_completion_date != null ? expected_completion_date.getTime() : "")
		   + "\n-------------------------------------------------------------------------------------------------------------------"
		   + String.format("%-50s",
				   "\n Bene Bank Txn Ref")
		   + ": "
		   + bene_bank_remittance_reference
		   + "\n-------------------------------------------------------------------------------------------------------------------"
		   + String.format("%-50s",
				   "\n XML Request")
		   + ": "
		   + super.getRequest_XML()
		   + "\n-------------------------------------------------------------------------------------------------------------------"
		   + String.format("%-50s",
				   "\n XML Response")
		   + ": "
		   + super.getResponse_XML()
		   + "\n--------------------------------------------------------------------------------------------------------------------\n";
	 }
   }
