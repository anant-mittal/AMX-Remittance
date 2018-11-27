package com.amx.jax.model.request.fx;



import java.math.BigDecimal;

import com.amx.jax.model.AbstractModel;

public class FcSaleOrderTransactionRequestModel extends AbstractModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7389625774939781754L;

	
	private BigDecimal sourceOfFundId;
	private BigDecimal foreignAmount;
	private BigDecimal purposeOfTrnx;
	private String currencyDenominationType;
	private BigDecimal foreignCurrencyId;
	private BigDecimal travelCountryId;
	private String startDate;
	private String endDate;
	
	public BigDecimal getSourceOfFundId() {
		return sourceOfFundId;
	}
	public void setSourceOfFundId(BigDecimal sourceOfFundId) {
		this.sourceOfFundId = sourceOfFundId;
	}
	
	public BigDecimal getForeignAmount() {
		return foreignAmount;
	}
	public void setForeignAmount(BigDecimal foreignAmount) {
		this.foreignAmount = foreignAmount;
	}
	public BigDecimal getPurposeOfTrnx() {
		return purposeOfTrnx;
	}
	public void setPurposeOfTrnx(BigDecimal purposeOfTrnx) {
		this.purposeOfTrnx = purposeOfTrnx;
	}
	public String getCurrencyDenominationType() {
		return currencyDenominationType;
	}
	public void setCurrencyDenominationType(String currencyDenominationType) {
		this.currencyDenominationType = currencyDenominationType;
	}

	public BigDecimal getForeignCurrencyId() {
		return foreignCurrencyId;
	}
	public void setForeignCurrencyId(BigDecimal foreignCurrencyId) {
		this.foreignCurrencyId = foreignCurrencyId;
	}
	
	public BigDecimal getTravelCountryId() {
		return travelCountryId;
	}
	public void setTravelCountryId(BigDecimal travelCountryId) {
		this.travelCountryId = travelCountryId;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	
}
