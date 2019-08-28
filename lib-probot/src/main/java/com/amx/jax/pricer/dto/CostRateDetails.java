package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class CostRateDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1767958113413294740L;

	private BigDecimal avgGlcCostRate;
	private BigDecimal avgGlcCostRateInv;

	private BigDecimal markup;

	private BigDecimal adjustedCostRate;
	private BigDecimal adjustedCostRateInv;

	public BigDecimal getAvgGlcCostRate() {
		return avgGlcCostRate;
	}

	public void setAvgGlcCostRate(BigDecimal avgGlcCostRate) {
		this.avgGlcCostRate = avgGlcCostRate;
	}

	public BigDecimal getAvgGlcCostRateInv() {
		return avgGlcCostRateInv;
	}

	public void setAvgGlcCostRateInv(BigDecimal avgGlcCostRateInv) {
		this.avgGlcCostRateInv = avgGlcCostRateInv;
	}

	public BigDecimal getMarkup() {
		return markup;
	}

	public void setMarkup(BigDecimal markup) {
		this.markup = markup;
	}

	public BigDecimal getAdjustedCostRate() {
		return adjustedCostRate;
	}

	public void setAdjustedCostRate(BigDecimal adjustedCostRate) {
		this.adjustedCostRate = adjustedCostRate;
	}

	public BigDecimal getAdjustedCostRateInv() {
		return adjustedCostRateInv;
	}

	public void setAdjustedCostRateInv(BigDecimal adjustedCostRateInv) {
		this.adjustedCostRateInv = adjustedCostRateInv;
	}

}
