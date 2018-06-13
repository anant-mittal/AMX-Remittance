package com.amx.amxlib.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Subodh Bhoir
 *
 */
public class MinMaxExRateDTO extends AbstractModel implements Serializable {

	private BigDecimal fromCurrencyId;
	private BigDecimal toCurrencyId;
	private BigDecimal minExrate;
	private BigDecimal maxExrate;

	@Override
	public String getModelType() {
		return "min-max-exrate";
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

	@Override
	public String toString() {
		return "MinMaxCurrencyDTO [fromCurrencyId=" + fromCurrencyId + ", toCurrencyId=" + toCurrencyId + ", minExrate="
				+ minExrate + ", maxExrate=" + maxExrate + "]";
	}

	
}
