package com.amx.jax.model.request;



import java.math.BigDecimal;

import com.amx.jax.model.AbstractModel;

public class FcSaleOrderTransactionRequestModel extends AbstractModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7389625774939781754L;

	
	private BigDecimal sourceOfFundId;
	private BigDecimal localAmount;
	private BigDecimal foreignAmount;
	private BigDecimal purposeOfTrnx;
	private String currencyDenominationType;
	private BigDecimal foreignCurrencyId;
	private String remarks;
	private BigDecimal deliveryCharges;
	private BigDecimal travelCountryId;
	
	private String startDate;
	private String endDate;
	private String errorMessage;
	

	private BigDecimal domXRate;
	public BigDecimal getSourceOfFundId() {
		return sourceOfFundId;
	}
	public void setSourceOfFundId(BigDecimal sourceOfFundId) {
		this.sourceOfFundId = sourceOfFundId;
	}
	public BigDecimal getLocalAmount() {
		return localAmount;
	}
	public void setLocalAmount(BigDecimal localAmount) {
		this.localAmount = localAmount;
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
	/*public FxExchangeRateBreakup getExRateBreakup() {
		return exRateBreakup;
	}
	public void setExRateBreakup(FxExchangeRateBreakup exRateBreakup) {
		this.exRateBreakup = exRateBreakup;
	}*/
	public BigDecimal getDomXRate() {
		return domXRate;
	}
	public void setDomXRate(BigDecimal domXRate) {
		this.domXRate = domXRate;
	}
	public BigDecimal getForeignCurrencyId() {
		return foreignCurrencyId;
	}
	public void setForeignCurrencyId(BigDecimal foreignCurrencyId) {
		this.foreignCurrencyId = foreignCurrencyId;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public BigDecimal getDeliveryCharges() {
		return deliveryCharges;
	}
	public void setDeliveryCharges(BigDecimal deliveryCharges) {
		this.deliveryCharges = deliveryCharges;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
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
