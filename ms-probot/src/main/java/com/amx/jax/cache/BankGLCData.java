package com.amx.jax.cache;

import java.math.BigDecimal;
import java.util.List;

import com.amx.jax.pricer.dbmodel.ViewExGLCBAL;

public class BankGLCData {

	private List<ViewExGLCBAL> glAccountsDetails;
	private BigDecimal avgLcRate;
	private BigDecimal avgFcRate;
	private BigDecimal maxLcCurBalAmount;
	private BigDecimal maxFcCurBalAmount;

	public List<ViewExGLCBAL> getGlAccountsDetails() {
		return glAccountsDetails;
	}

	public void setGlAccountsDetails(List<ViewExGLCBAL> glAccountsDetails) {
		this.glAccountsDetails = glAccountsDetails;
	}

	public BigDecimal getAvgLcRate() {
		return avgLcRate;
	}

	public void setAvgLcRate(BigDecimal avgLcRate) {
		this.avgLcRate = avgLcRate;
	}

	public BigDecimal getAvgFcRate() {
		return avgFcRate;
	}

	public void setAvgFcRate(BigDecimal avgFcRate) {
		this.avgFcRate = avgFcRate;
	}

	public BigDecimal getMaxLcCurBalAmount() {
		return maxLcCurBalAmount;
	}

	public void setMaxLcCurBalAmount(BigDecimal maxLcCurBalAmount) {
		this.maxLcCurBalAmount = maxLcCurBalAmount;
	}

	public BigDecimal getMaxFcCurBalAmount() {
		return maxFcCurBalAmount;
	}

	public void setMaxFcCurBalAmount(BigDecimal maxFcCurBalAmount) {
		this.maxFcCurBalAmount = maxFcCurBalAmount;
	}

}
