package com.amx.jax.dbmodel;

import java.math.BigDecimal;

/**
 * @author Subodh Bhoir
 *
 */
public class MinMaxExRate implements java.io.Serializable {

	private BigDecimal fromCurrencyId;
	private BigDecimal toCurrencyId;
	private BigDecimal minExrate;
	private BigDecimal maxExrate;
	
	public MinMaxExRate() {
		super();
	}
	
	public MinMaxExRate(BigDecimal fromCurrencyId, BigDecimal toCurrencyId, BigDecimal minExrate,
			BigDecimal maxExrate) {
		super();
		this.fromCurrencyId = fromCurrencyId;
		this.toCurrencyId = toCurrencyId;
		this.minExrate = minExrate;
		this.maxExrate = maxExrate;
	}


	public BigDecimal getFromCurrencyId() {
		return fromCurrencyId;
	}

	public void setFromCurrencyId(BigDecimal fromCurrencyId) {
		this.fromCurrencyId = fromCurrencyId;
	}

	public BigDecimal getToCurrencyId() {
		return toCurrencyId;
	}

	public void setToCurrencyId(BigDecimal toCurrencyId) {
		this.toCurrencyId = toCurrencyId;
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
