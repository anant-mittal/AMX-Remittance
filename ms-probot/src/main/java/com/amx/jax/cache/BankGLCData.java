package com.amx.jax.cache;

import java.math.BigDecimal;
import java.util.List;

import com.amx.jax.pricer.dbmodel.TreasuryFundTimeImpact;
import com.amx.jax.pricer.dbmodel.ViewExGLCBAL;
import com.amx.jax.pricer.dbmodel.ViewExGLCBalProvisional;

public class BankGLCData {

	private List<ViewExGLCBAL> glAccountsDetails;
	
	private BigDecimal avgLcRate;
	private BigDecimal avgFcRate;
	
	private BigDecimal maxLcCurBalAmount;
	private BigDecimal maxFcCurBalAmount;
	
	private BigDecimal adjustedLcCurBal;
	private BigDecimal adjustedFcCurBal;

	private ViewExGLCBalProvisional provisionalBalDetails;

	private List<ViewExGLCBAL> FundingGlAcDetails;

	private TreasuryFundTimeImpact fundedTimeImpact;

	private TreasuryFundTimeImpact outOfFundTimeImpact;

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

	public BigDecimal getAdjustedLcCurBal() {
		return adjustedLcCurBal;
	}

	public void setAdjustedLcCurBal(BigDecimal adjustedLcCurBal) {
		this.adjustedLcCurBal = adjustedLcCurBal;
	}

	public BigDecimal getAdjustedFcCurBal() {
		return adjustedFcCurBal;
	}

	public void setAdjustedFcCurBal(BigDecimal adjustedFcCurBal) {
		this.adjustedFcCurBal = adjustedFcCurBal;
	}

	public ViewExGLCBalProvisional getProvisionalBalDetails() {
		return provisionalBalDetails;
	}

	public void setProvisionalBalDetails(ViewExGLCBalProvisional provisionalBalDetails) {
		this.provisionalBalDetails = provisionalBalDetails;
	}

	public List<ViewExGLCBAL> getFundingGlAcDetails() {
		return FundingGlAcDetails;
	}

	public void setFundingGlAcDetails(List<ViewExGLCBAL> fundingGlAcDetails) {
		FundingGlAcDetails = fundingGlAcDetails;
	}

	public TreasuryFundTimeImpact getFundedTimeImpact() {
		return fundedTimeImpact;
	}

	public void setFundedTimeImpact(TreasuryFundTimeImpact fundedTimeImpact) {
		this.fundedTimeImpact = fundedTimeImpact;
	}

	public TreasuryFundTimeImpact getOutOfFundTimeImpact() {
		return outOfFundTimeImpact;
	}

	public void setOutOfFundTimeImpact(TreasuryFundTimeImpact outOfFundTimeImpact) {
		this.outOfFundTimeImpact = outOfFundTimeImpact;
	}
	
	
	

}
