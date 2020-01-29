package com.amx.jax.model.request.serviceprovider;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

public class TransactionData
{
	String application_country_3_digit_ISO; // KWT, OMN or BHR
	String application_country_2_digit_ISO;
	String routting_bank_code;

	// sequence number that identify the transaction at our system and.
	// Same value need to be use for get quotation, remittance and status call.
	String request_sequence_id;

	String partner_transaction_reference; // transaction number generated from partner
	String bene_bank_remittance_reference;
	String addtional_external_reference; // Used to store additional reference from bene bank or partner side. example (SSS PRN from Vintja)
	String out_going_transaction_reference; // transaction number generated locally
	
	String remarks;
	String partner_transaction_status;

	// For internal use
	String remittance_mode;
	String delivery_mode;
	int company_code, document_code, document_finance_year, document_no;
	long remittance_transaction_id;

	BigDecimal settlement_amount;
	String settlement_currency;

	BigDecimal destination_amount;
	String destination_currency;

	String origin_country_3_digit_ISO;
	String destination_country_2_digit_ISO;
	String destination_country_3_digit_ISO;

	String source_of_fund_desc;
	String txn_collocation_type;
	String purpose_of_remittance;
	String further_instruction;

	Date coverage_start_date;
	Date coverage_end_date;

	// General purpose fields could have different interpretation as per the partner
	String flexi_field_1, flexi_field_2;

	public String getApplication_country_3_digit_ISO()
	{
		return application_country_3_digit_ISO;
	}

	public void setApplication_country_3_digit_ISO(String application_country_3_digit_ISO)
	{
		this.application_country_3_digit_ISO = application_country_3_digit_ISO;
	}

	public String getApplication_country_2_digit_ISO()
	{
		return application_country_2_digit_ISO;
	}

	public void setApplication_country_2_digit_ISO(String application_country_2_digit_ISO)
	{
		this.application_country_2_digit_ISO = application_country_2_digit_ISO;
	}

	public String getRoutting_bank_code()
	{
		return routting_bank_code;
	}

	public void setRoutting_bank_code(String routting_bank_code)
	{
		this.routting_bank_code = routting_bank_code;
	}

	public String getRequest_sequence_id()
	{
		return request_sequence_id;
	}

	public void setRequest_sequence_id(String request_sequence_id)
	{
		this.request_sequence_id = request_sequence_id;
	}

	public String getPartner_transaction_reference()
	{
		return partner_transaction_reference;
	}

	public void setPartner_transaction_reference(String partner_transaction_reference)
	{
		this.partner_transaction_reference = partner_transaction_reference;
	}

	public String getOut_going_transaction_reference()
	{
		return out_going_transaction_reference;
	}

	public String getRemarks()
	{
		return remarks;
	}

	public void setRemarks(String remarks)
	{
		this.remarks = remarks;
	}

	public String getPartner_transaction_status()
	{
		return partner_transaction_status;
	}

	public void setPartner_transaction_status(String partner_transaction_status)
	{
		this.partner_transaction_status = partner_transaction_status;
	}

	public String getBene_bank_remittance_reference()
	{
		return bene_bank_remittance_reference;
	}

	public void setBene_bank_remittance_reference(String bene_bank_remittance_reference)
	{
		this.bene_bank_remittance_reference = bene_bank_remittance_reference;
	}

	public String getAddtional_external_reference()
	{
		return addtional_external_reference;
	}

	public void setAddtional_external_reference(String addtional_external_reference)
	{
		this.addtional_external_reference = addtional_external_reference;
	}

	public void setOut_going_transaction_reference(String out_going_transaction_reference)
	{
		this.out_going_transaction_reference = out_going_transaction_reference;
	}

	public int getCompany_code()
	{
		return company_code;
	}

	public void setCompany_code(int company_code)
	{
		this.company_code = company_code;
	}

	public int getDocument_code()
	{
		return document_code;
	}

	public void setDocument_code(int document_code)
	{
		this.document_code = document_code;
	}

	public int getDocument_finance_year()
	{
		return document_finance_year;
	}

	public void setDocument_finance_year(int document_finance_year)
	{
		this.document_finance_year = document_finance_year;
	}

	public int getDocument_no()
	{
		return document_no;
	}

	public void setDocument_no(int document_no)
	{
		this.document_no = document_no;
	}

	public long getRemittance_transaction_id()
	{
		return remittance_transaction_id;
	}

	public void setRemittance_transaction_id(long remittance_transaction_id)
	{
		this.remittance_transaction_id = remittance_transaction_id;
	}

	public Date getCoverage_start_date()
	{
		return coverage_start_date;
	}

	public void setCoverage_start_date(Date coverage_start_date)
	{
		this.coverage_start_date = coverage_start_date;
	}

	public Date getCoverage_end_date()
	{
		return coverage_end_date;
	}

	public void setCoverage_end_date(Date coverage_end_date)
	{
		this.coverage_end_date = coverage_end_date;
	}

	public String getRemittance_mode()
	{
		return remittance_mode;
	}

	public void setRemittance_mode(String remittance_mode)
	{
		this.remittance_mode = remittance_mode;
	}

	public String getDelivery_mode()
	{
		return delivery_mode;
	}

	public void setDelivery_mode(String delivery_mode)
	{
		this.delivery_mode = delivery_mode;
	}

	public BigDecimal getSettlement_amount()
	{
		return settlement_amount;
	}

	public void setSettlement_amount(BigDecimal settlement_amount)
	{
		this.settlement_amount = settlement_amount;
	}

	public String getSettlement_currency()
	{
		return settlement_currency;
	}

	public void setSettlement_currency(String settlement_currency)
	{
		this.settlement_currency = settlement_currency;
	}

	public BigDecimal getDestination_amount()
	{
		return destination_amount;
	}

	public void setDestination_amount(BigDecimal destination_amount)
	{
		this.destination_amount = destination_amount;
	}

	public String getDestination_currency()
	{
		return destination_currency;
	}

	public void setDestination_currency(String destination_currency)
	{
		this.destination_currency = destination_currency;
	}

	public String getOrigin_country_3_digit_ISO()
	{
		return origin_country_3_digit_ISO;
	}

	public void setOrigin_country_3_digit_ISO(String origin_country_3_digit_ISO)
	{
		this.origin_country_3_digit_ISO = origin_country_3_digit_ISO;
	}

	public String getDestination_country_2_digit_ISO()
	{
		return destination_country_2_digit_ISO;
	}

	public void setDestination_country_2_digit_ISO(String destination_country_2_digit_ISO)
	{
		this.destination_country_2_digit_ISO = destination_country_2_digit_ISO;
	}

	public String getDestination_country_3_digit_ISO()
	{
		return destination_country_3_digit_ISO;
	}

	public void setDestination_country_3_digit_ISO(String destination_country_3_digit_ISO)
	{
		this.destination_country_3_digit_ISO = destination_country_3_digit_ISO;
	}

	public String getSource_of_fund_desc()
	{
		return source_of_fund_desc;
	}

	public void setSource_of_fund_desc(String source_of_fund_desc)
	{
		this.source_of_fund_desc = source_of_fund_desc;
	}

	public String getTxn_collocation_type()
	{
		return txn_collocation_type;
	}

	public void setTxn_collocation_type(String txn_collocation_type)
	{
		this.txn_collocation_type = txn_collocation_type;
	}

	public String getPurpose_of_remittance()
	{
		return purpose_of_remittance;
	}

	public void setPurpose_of_remittance(String purpose_of_remittance)
	{
		this.purpose_of_remittance = purpose_of_remittance;
	}

	public String getFurther_instruction()
	{
		return further_instruction;
	}

	public void setFurther_instruction(String further_instruction)
	{
		this.further_instruction = further_instruction;
	}

	public String getFlexi_field_1()
	{
		return flexi_field_1;
	}

	public void setFlexi_field_1(String flexi_field_1)
	{
		this.flexi_field_1 = flexi_field_1;
	}

	public String getFlexi_field_2()
	{
		return flexi_field_2;
	}

	public void setFlexi_field_2(String flexi_field_2)
	{
		this.flexi_field_2 = flexi_field_2;
	}

	public String validate_transactionData_input()
	{
		ArrayList<String> validation_error_list = new ArrayList<String>();
		String validation_errors = "";

		if (request_sequence_id == null)
			validation_error_list.add("Request sequence ID is empty");

		if (partner_transaction_reference == null)
			validation_error_list.add("Partner Transaction ID is empty");

		if (settlement_amount == null)
			validation_error_list.add("Settlement amount is empty");

		if (settlement_currency == null)
			validation_error_list.add("Settlement currency is empty");

		if (destination_amount != null)
			if (destination_currency == null)
				validation_error_list.add("Destination currency is empty");

		if (origin_country_3_digit_ISO == null)
			validation_error_list.add("Origin country ISO is empty");

		if (destination_country_3_digit_ISO == null)
			validation_error_list.add("Destination country ISO is empty");

		if (source_of_fund_desc == null)
			validation_error_list.add("Source of Fund is empty");

		if (txn_collocation_type == null)
			validation_error_list.add("Collection Type is empty");

		if (purpose_of_remittance == null)
			validation_error_list.add("Purpose of Remittance is empty");

		if (delivery_mode == null)
			validation_error_list.add("Delivery mode is empty");

		for (int i = 0; i < validation_error_list.size(); i++)
		{
			if (i != 0)
				validation_errors += "\n";

			validation_errors = validation_errors + (i + 1) + "- " + validation_error_list.get(i);
		}

		return validation_errors;
	}

}