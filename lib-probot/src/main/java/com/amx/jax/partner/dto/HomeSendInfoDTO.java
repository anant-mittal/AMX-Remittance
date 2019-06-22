package com.amx.jax.partner.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;

public class HomeSendInfoDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Calendar offer_expiration_date;

	String partner_transaction_reference;
	String settlement_currency;
	String destination_currency;
	String action_ind;
	String response_code;
	String response_description;
	String technical_details;
	String request_XML;
	String response_XML;

	boolean isBeneficiaryDeduct;

	BigDecimal credited_amount_in_destination_currency;
	BigDecimal initial_amount_in_settlement_currency;
	BigDecimal fix_charged_amount_in_settlement_currency;
	BigDecimal variable_charged_amount_in_settlement_currency;
	BigDecimal total_charged_amount_in_settlement_currency;
	BigDecimal whole_sale_fx_rate;

	public Calendar getOffer_expiration_date() {
		return offer_expiration_date;
	}

	public void setOffer_expiration_date(Calendar offer_expiration_date) {
		this.offer_expiration_date = offer_expiration_date;
	}

	public String getPartner_transaction_reference() {
		return partner_transaction_reference;
	}

	public void setPartner_transaction_reference(String partner_transaction_reference) {
		this.partner_transaction_reference = partner_transaction_reference;
	}

	public String getSettlement_currency() {
		return settlement_currency;
	}

	public void setSettlement_currency(String settlement_currency) {
		this.settlement_currency = settlement_currency;
	}

	public String getDestination_currency() {
		return destination_currency;
	}

	public void setDestination_currency(String destination_currency) {
		this.destination_currency = destination_currency;
	}

	public String getAction_ind() {
		return action_ind;
	}

	public void setAction_ind(String action_ind) {
		this.action_ind = action_ind;
	}

	public String getResponse_code() {
		return response_code;
	}

	public void setResponse_code(String response_code) {
		this.response_code = response_code;
	}

	public String getResponse_description() {
		return response_description;
	}

	public void setResponse_description(String response_description) {
		this.response_description = response_description;
	}

	public String getTechnical_details() {
		return technical_details;
	}

	public void setTechnical_details(String technical_details) {
		this.technical_details = technical_details;
	}

	public String getRequest_XML() {
		return request_XML;
	}

	public void setRequest_XML(String request_XML) {
		this.request_XML = request_XML;
	}

	public String getResponse_XML() {
		return response_XML;
	}

	public void setResponse_XML(String response_XML) {
		this.response_XML = response_XML;
	}

	public BigDecimal getCredited_amount_in_destination_currency() {
		return credited_amount_in_destination_currency;
	}

	public void setCredited_amount_in_destination_currency(BigDecimal credited_amount_in_destination_currency) {
		this.credited_amount_in_destination_currency = credited_amount_in_destination_currency;
	}

	public BigDecimal getInitial_amount_in_settlement_currency() {
		return initial_amount_in_settlement_currency;
	}

	public void setInitial_amount_in_settlement_currency(BigDecimal initial_amount_in_settlement_currency) {
		this.initial_amount_in_settlement_currency = initial_amount_in_settlement_currency;
	}

	public BigDecimal getFix_charged_amount_in_settlement_currency() {
		return fix_charged_amount_in_settlement_currency;
	}

	public void setFix_charged_amount_in_settlement_currency(BigDecimal fix_charged_amount_in_settlement_currency) {
		this.fix_charged_amount_in_settlement_currency = fix_charged_amount_in_settlement_currency;
	}

	public BigDecimal getVariable_charged_amount_in_settlement_currency() {
		return variable_charged_amount_in_settlement_currency;
	}

	public void setVariable_charged_amount_in_settlement_currency(
			BigDecimal variable_charged_amount_in_settlement_currency) {
		this.variable_charged_amount_in_settlement_currency = variable_charged_amount_in_settlement_currency;
	}

	public BigDecimal getTotal_charged_amount_in_settlement_currency() {
		return total_charged_amount_in_settlement_currency;
	}

	public void setTotal_charged_amount_in_settlement_currency(BigDecimal total_charged_amount_in_settlement_currency) {
		this.total_charged_amount_in_settlement_currency = total_charged_amount_in_settlement_currency;
	}

	public BigDecimal getWhole_sale_fx_rate() {
		return whole_sale_fx_rate;
	}

	public void setWhole_sale_fx_rate(BigDecimal whole_sale_fx_rate) {
		this.whole_sale_fx_rate = whole_sale_fx_rate;
	}

	public boolean isBeneficiaryDeduct() {
		return isBeneficiaryDeduct;
	}

	public void setBeneficiaryDeduct(boolean isBeneficiaryDeduct) {
		this.isBeneficiaryDeduct = isBeneficiaryDeduct;
	}

}
