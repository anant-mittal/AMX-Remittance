package com.amx.jax.model.request.partner;

import java.math.BigDecimal;

public class PaymentLimitDTO {

	private BigDecimal bankId;
	private String currencyQuote;
	private BigDecimal beneficiaryRelationshipId;
	
	public BigDecimal getBankId() {
		return bankId;
	}
	public void setBankId(BigDecimal bankId) {
		this.bankId = bankId;
	}
	
	public String getCurrencyQuote() {
		return currencyQuote;
	}
	public void setCurrencyQuote(String currencyQuote) {
		this.currencyQuote = currencyQuote;
	}
	
	public BigDecimal getBeneficiaryRelationshipId() {
		return beneficiaryRelationshipId;
	}
	public void setBeneficiaryRelationshipId(BigDecimal beneficiaryRelationshipId) {
		this.beneficiaryRelationshipId = beneficiaryRelationshipId;
	}
		
}
