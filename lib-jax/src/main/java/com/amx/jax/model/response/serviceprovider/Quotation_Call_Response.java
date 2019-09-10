package com.amx.jax.model.response.serviceprovider;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Quotation_Call_Response extends ServiceProviderResponse
{
	String partner_transaction_reference; // transaction number generated from partner
	String out_going_transaction_reference; // transaction number generated locally
	Calendar offer_expiration_date;

	String settlement_currency;
	String destination_currency;

	BigDecimal credited_amount_in_destination_currency;
	BigDecimal initial_amount_in_settlement_currency;
	BigDecimal fix_charged_amount_in_settlement_currency;
	BigDecimal variable_charged_amount_in_settlement_currency;
	BigDecimal total_charged_amount_in_settlement_currency;

	BigDecimal whole_sale_fx_rate;

	public String getPartner_transaction_reference()
	{
		return partner_transaction_reference;
	}

	public void setPartner_transaction_reference(String partner_transaction_reference)
	{
		this.partner_transaction_reference = partner_transaction_reference;
	}

	public String getOut_going_transaction_reference() {
		return out_going_transaction_reference;
	}

	public void setOut_going_transaction_reference(String out_going_transaction_reference) {
		this.out_going_transaction_reference = out_going_transaction_reference;
	}

	public Calendar getOffer_expiration_date()
	{
		return offer_expiration_date;
	}

	public void setOffer_expiration_date(Calendar calendar)
	{
		this.offer_expiration_date = calendar;
	}

	public String getSettlement_currency()
	{
		return settlement_currency;
	}

	public void setSettlement_currency(String settlement_currency)
	{
		this.settlement_currency = settlement_currency;
	}

	public String getDestination_currency()
	{
		return destination_currency;
	}

	public void setDestination_currency(String destination_currency)
	{
		this.destination_currency = destination_currency;
	}

	public BigDecimal getInitial_amount_in_settlement_currency()
	{
		return initial_amount_in_settlement_currency;
	}

	public void setInitial_amount_in_settlement_currency(BigDecimal bigDecimal)
	{
		this.initial_amount_in_settlement_currency = bigDecimal;
	}

	public BigDecimal getCredited_amount_in_destination_currency()
	{
		return credited_amount_in_destination_currency;
	}

	public void setCredited_amount_in_destination_currency(BigDecimal credited_amount_in_destination_currency)
	{
		this.credited_amount_in_destination_currency = credited_amount_in_destination_currency;
	}

	public BigDecimal getTotal_charged_amount_in_settlement_currency()
	{
		return total_charged_amount_in_settlement_currency;
	}

	public BigDecimal getFix_charged_amount_in_settlement_currency()
	{
		return fix_charged_amount_in_settlement_currency;
	}

	public void setFix_charged_amount_in_settlement_currency(BigDecimal fix_charged_amount_in_settlement_currency)
	{
		this.fix_charged_amount_in_settlement_currency = fix_charged_amount_in_settlement_currency;
	}

	public BigDecimal getVariable_charged_amount_in_settlement_currency()
	{
		return variable_charged_amount_in_settlement_currency;
	}

	public void setVariable_charged_amount_in_settlement_currency(
			BigDecimal variable_charged_amount_in_settlement_currency)
	{
		this.variable_charged_amount_in_settlement_currency = variable_charged_amount_in_settlement_currency;
	}

	public void setTotal_charged_amount_in_settlement_currency(BigDecimal total_charged_amount_in_settlement_currency)
	{
		this.total_charged_amount_in_settlement_currency = total_charged_amount_in_settlement_currency;
	}

	public BigDecimal getWhole_sale_fx_rate()
	{
		return whole_sale_fx_rate;
	}

	public void setWhole_sale_fx_rate(BigDecimal whole_sale_fx_rate)
	{
		this.whole_sale_fx_rate = whole_sale_fx_rate;
	}

	@Override
	public String toString()
	{
		String[] response_desc_array;

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss zzz");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		Date date = new Date();
		String formated_date = sdf.format(date);

		if (super.getResponse_description() != null)
			response_desc_array = super.getResponse_description().split("\n");
		else
			response_desc_array = new String[] { super.getResponse_description() };

		String result = "\n--------------------------------------------------------------------------------------------------------------------\n"
				+ String.format("%-49s", " Response Code") + ": " + super.getResponse_code()
				+ "\n-------------------------------------------------------------------------------------------------------------------"
				+ String.format("%-50s", "\n Action Indicator") + ": " + super.getAction_ind()
				+ "\n-------------------------------------------------------------------------------------------------------------------"
				+ String.format("%-50s", "\n Response Description") + ": ";

		for (int i = 0; i < response_desc_array.length; i++)
		{
			if (i == 0)
				result += response_desc_array[i] + "\n";
			else
				result += String.format("%51s", " ") + response_desc_array[i] + "\n";
		}

		result += "-------------------------------------------------------------------------------------------------------------------"
				+ String.format("%-50s", "\n Time Now") + ": " + formated_date
				+ "\n-------------------------------------------------------------------------------------------------------------------"
				+ String.format("%-50s", "\n Partner Txn ID") + ": " + partner_transaction_reference
				+ "\n-------------------------------------------------------------------------------------------------------------------"
				+ String.format("%-50s", "\n Offer Expiration Date") + ": "
				+ ((offer_expiration_date != null) ? offer_expiration_date.getTime() : offer_expiration_date)
				+ "\n-------------------------------------------------------------------------------------------------------------------"
				+ String.format("%-50s", "\n Settlement Currency") + ": " + settlement_currency
				+ "\n-------------------------------------------------------------------------------------------------------------------"
				+ String.format("%-50s", "\n Destination Currency") + ": " + destination_currency
				+ "\n-------------------------------------------------------------------------------------------------------------------"
				+ String.format("%-50s", "\n Credited Amount in Destination Currency") + ": "
				+ String.format("%-16s", credited_amount_in_destination_currency)
				+ (destination_currency == null ? "" : destination_currency)
				+ "\n-------------------------------------------------------------------------------------------------------------------"
				+ String.format("%-50s", "\n Initial Amount in Settlement Currency") + ": "
				+ String.format("%-16s", initial_amount_in_settlement_currency)
				+ (settlement_currency == null ? "" : settlement_currency)
				+ "\n-------------------------------------------------------------------------------------------------------------------"
				+ String.format("%-50s", "\n HomeSend Fix Charges in Settlement Currency") + ": "
				+ String.format("%-16s", fix_charged_amount_in_settlement_currency)
				+ (settlement_currency == null ? "" : settlement_currency)
				+ "\n-------------------------------------------------------------------------------------------------------------------"
				+ String.format("%-50s", "\n HomeSend Variable Charges in Settlement Currency") + ": "
				+ String.format("%-16s", variable_charged_amount_in_settlement_currency)
				+ (settlement_currency == null ? "" : settlement_currency)
				+ "\n-------------------------------------------------------------------------------------------------------------------"
				+ String.format("%-50s", "\n HomeSend Total Amount(with charges) in Settlement Currency") + ": "
				+ String.format("%-16s", total_charged_amount_in_settlement_currency)
				+ (settlement_currency == null ? "" : settlement_currency)
				+ "\n-------------------------------------------------------------------------------------------------------------------"
				+ String.format("%-50s", "\n Total Charged Amount in Settlement Currency") + ": "
				+ String.format("%-16s", total_charged_amount_in_settlement_currency)
				+ (settlement_currency == null ? "" : settlement_currency)
				+ "\n-------------------------------------------------------------------------------------------------------------------"
				+ String.format("%-50s", "\n HomeSend Rate Applied") + ": " + whole_sale_fx_rate
				+ "\n-------------------------------------------------------------------------------------------------------------------"
				+ String.format("%-50s", "\n XML Request") + ": " + super.getRequest_XML()
				+ "\n-------------------------------------------------------------------------------------------------------------------"
				+ String.format("%-50s", "\n XML Response") + ": " + super.getResponse_XML()
				+ "\n-------------------------------------------------------------------------------------------------------------------\n";

		return result;
	}
}