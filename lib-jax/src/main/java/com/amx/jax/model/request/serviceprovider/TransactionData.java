package com.amx.jax.model.request.serviceprovider;

import java.math.BigDecimal;
import java.util.ArrayList;

public class TransactionData
{	
	String application_country_3_digit_ISO; // KWT, OMN or BHR
	String routting_bank_code;
	
	// sequence number that identify the transaction at our system and.
	// Same value need to be use for get quotation, remittance and status call.
	String request_sequence_id;

	String partner_transaction_reference; // transaction number generated from partner
	String out_going_transaction_reference; // transaction number generated locally

	String remittance_mode;
	String delivery_mode;

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

	
	public String getApplication_country_3_digit_ISO()
	{
		return application_country_3_digit_ISO;
	}

	public void setApplication_country_3_digit_ISO(String application_country_3_digit_ISO)
	{
		this.application_country_3_digit_ISO = application_country_3_digit_ISO;
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

	public void setOut_going_transaction_reference(String out_going_transaction_reference)
	{
		this.out_going_transaction_reference = out_going_transaction_reference;
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

		  validation_errors = validation_errors + (i + 1)
				      + "- "
				      + validation_error_list.get(i);
	       }

	    return validation_errors;
	 }

}