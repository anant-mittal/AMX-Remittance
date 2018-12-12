package com.amx.jax.model.response.fx;

import java.math.BigDecimal;

public class FcSaleCurrencyAmountModel {
	
	private BigDecimal amount;
	private String currencyQuote;
	
	public FcSaleCurrencyAmountModel() {
		super();
	}
	
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public String getCurrencyQuote() {
		return currencyQuote;
	}
	public void setCurrencyQuote(String currencyQuote) {
		this.currencyQuote = currencyQuote;
	}
		
}
