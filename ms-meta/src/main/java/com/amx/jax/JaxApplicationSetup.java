package com.amx.jax;

import java.math.BigDecimal;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.amx.jax.scope.TenantScoped;

@Component
@TenantScoped
public class JaxApplicationSetup {

	String currencyQuote;
	BigDecimal currencyId;
	BigDecimal applicationCountryId;
	String onlineFeedBackEmail;

	public String getCurrencyQuote() {
		return currencyQuote;
	}

	public void setCurrencyQuote(String currencyQuote) {
		this.currencyQuote = currencyQuote;
	}

	public BigDecimal getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(BigDecimal currencyId) {
		this.currencyId = currencyId;
	}

	public String getOnlineFeedBackEmail() {
		return onlineFeedBackEmail;
	}

	public void setOnlineFeedBackEmail(String onlineFeedBackEmail) {
		this.onlineFeedBackEmail = onlineFeedBackEmail;
	}

	public BigDecimal getApplicationCountryId() {
		return applicationCountryId;
	}

	public void setApplicationCountryId(BigDecimal applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}

}
