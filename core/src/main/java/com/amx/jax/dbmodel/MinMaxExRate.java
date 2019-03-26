package com.amx.jax.dbmodel;

import java.math.BigDecimal;

import com.amx.jax.model.response.CurrencyMasterDTO;



/**
 * @author Subodh Bhoir
 *
 */
public class MinMaxExRate implements java.io.Serializable {

	private CurrencyMasterDTO fromCurrency;
	private CurrencyMasterDTO toCurrency;
	private BigDecimal minExrate;
	private BigDecimal maxExrate;
	
	public MinMaxExRate() {
		super();
	}
	
	public MinMaxExRate(CurrencyMasterDTO fromCurrency, CurrencyMasterDTO toCurrency, BigDecimal minExrate,
			BigDecimal maxExrate) {
		super();
		this.fromCurrency = fromCurrency;
		this.toCurrency = toCurrency;
		this.minExrate = minExrate;
		this.maxExrate = maxExrate;
	}



	public CurrencyMasterDTO getFromCurrency() {
		return fromCurrency;
	}

	public void setFromCurrency(CurrencyMasterDTO fromCurrency) {
		this.fromCurrency = fromCurrency;
	}

	public CurrencyMasterDTO getToCurrency() {
		return toCurrency;
	}

	public void setToCurrency(CurrencyMasterDTO toCurrency) {
		this.toCurrency = toCurrency;
	}

	public BigDecimal getMinExrate() {
		return minExrate;
	}

	public void setMinExrate(BigDecimal minExrate) {
		this.minExrate = minExrate;
	}

	public BigDecimal getMaxExrate() {
		return maxExrate;
	}

	public void setMaxExrate(BigDecimal maxExrate) {
		this.maxExrate = maxExrate;
	}

}
