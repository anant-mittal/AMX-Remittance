package com.amx.amxlib.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.amx.jax.AbstractModel;
import com.amx.jax.model.response.CurrencyMasterDTO;

/**
 * @author Subodh Bhoir
 *
 */
public class MinMaxExRateDTO extends AbstractModel implements Serializable {

	private CurrencyMasterDTO fromCurrency;
	private CurrencyMasterDTO toCurrency;
	private BigDecimal minExrate;
	private BigDecimal maxExrate;

	@Override
	public String getModelType() {
		return "min-max-exrate";
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

	@Override
	public String toString() {
		return "MinMaxCurrencyDTO [fromCurrency=" + fromCurrency.getCurrencyId() + ", toCurrency=" + toCurrency.getCurrencyId() + ", minExrate="
				+ minExrate + ", maxExrate=" + maxExrate + "]";
	}

	
}
