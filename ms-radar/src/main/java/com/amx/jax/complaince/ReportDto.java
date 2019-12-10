package com.amx.jax.complaince;

import java.sql.Date;

public class ReportDto {
	
	 public ReportDto() 
	    {
		 String rentity_id,submission_code,report_code,currency_code_local;
			String submission_date;
			
	    }
	 
	 
	 
	    }
//sub class 
class ReportPerson extends ReportDto 
{
	
	 public ReportPerson() 
	    {
		 String rep_first_name, rep_last_name,rep_nationality1,
			
			rep_tph_contact_type,rep_tph_communication_type,rep_tph_number,
			
			email,occupation,
			
			rep_address_type,rep_address,rep_city,rep_country_code,
			
			reason,action;
	    }
}

//sub class 
class Transactiondetails extends ReportDto 
{
	
	 public Transactiondetails() 
	    {
		 String transactionnumber,transaction_location,date_transaction,teller,authorized,transmode_code,amount_local,
			
			from_funds_code,foreign_currency_code,foreign_amount,foreign_exchange_rate;
	    }
}

//sub class 
class CustomerDetails extends ReportDto 
{
	 public CustomerDetails() 
	    {
		 String cust_gender,cust_title,cust_first_name,cust_last_name,cust_ssn,cust_nationality1,
			
			cust_tph_contact_type,cust_tph_communication_type,cust_tph_country_prefix,cust_tph_number,
			
			cust_address_type,cust_address,cust_city,cust_country_code,
			
			cust_identity_type,cust_identity_number,cust_issue_country;
	    }
	
}

//sub class 
class BeneficiaryDetails extends ReportDto 
{
	 public BeneficiaryDetails() 
	    {
		 String from_country,
			
			to_funds_code,
			
			bene_gender,bene_title,bene_first_name,bene_last_name,bene_ssn,bene_nationality1,
			
			bene_tph_contact_type,bene_tph_communication_type,bene_tph_country_prefix,bene_tph_number,
			
			bene_address_type,bene_address,bene_city,bene_country_code,
			
			bene_type,bene_number,bene_issue_country,
			
			bene_to_country,
			
			indicator,indicator1;
			
	    }
	
}

