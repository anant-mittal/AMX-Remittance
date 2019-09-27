package com.amx.jax.config;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.amx.jax.scope.TenantScoped;
import com.amx.jax.scope.TenantValue;

@TenantScoped
@Component
public class RbaacTenantProperties {

	@TenantValue("${default.tenant}")
	private String tenant;

	@TenantValue("${default.currency.quote}")
	private String currencyQuote;

	@TenantValue("${default.currency.id}")
	private BigDecimal currencyId;

	public String getTenant() {
		return tenant;
	}

	public void setTenant(String tenant) {
		this.tenant = tenant;
	}

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

}
