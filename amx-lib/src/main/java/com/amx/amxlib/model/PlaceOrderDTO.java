package com.amx.amxlib.model;

import java.math.BigDecimal;
import java.util.Date;

public class PlaceOrderDTO extends AbstractModel implements Cloneable {

	private static final long serialVersionUID = 1L;
	private BigDecimal placeOrderId;
	private BigDecimal customerId;
	private BigDecimal beneficiaryMasterSeqId;
	private BigDecimal civilId;
	private BigDecimal baseCurrencyId;
	private String baseCurrencyCode;
	private BigDecimal foreignCurrencyId;
	private String foreignCurrencyCode;
	private BigDecimal exchangeRateId;
	private BigDecimal exchangeRate;
	private BigDecimal targetRate;
	private BigDecimal purposeId;
	private BigDecimal sourceOfIncomeId;
	private String isActive;
	private Date validToDate;
	private Date validFromDate;
	private BigDecimal payAmount;
	private BigDecimal receiveAmount;

	public BigDecimal getPlaceOrderId() {
		return placeOrderId;
	}

	public void setPlaceOrderId(BigDecimal placeOrderId) {
		this.placeOrderId = placeOrderId;
	}

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	public BigDecimal getBeneficiaryMasterSeqId() {
		return beneficiaryMasterSeqId;
	}

	public void setBeneficiaryMasterSeqId(BigDecimal beneficiaryMasterSeqId) {
		this.beneficiaryMasterSeqId = beneficiaryMasterSeqId;
	}

	public BigDecimal getCivilId() {
		return civilId;
	}

	public void setCivilId(BigDecimal civilId) {
		this.civilId = civilId;
	}

	public BigDecimal getBaseCurrencyId() {
		return baseCurrencyId;
	}

	public void setBaseCurrencyId(BigDecimal baseCurrencyId) {
		this.baseCurrencyId = baseCurrencyId;
	}

	public String getBaseCurrencyCode() {
		return baseCurrencyCode;
	}

	public void setBaseCurrencyCode(String baseCurrencyCode) {
		this.baseCurrencyCode = baseCurrencyCode;
	}

	public BigDecimal getForeignCurrencyId() {
		return foreignCurrencyId;
	}

	public void setForeignCurrencyId(BigDecimal foreignCurrencyId) {
		this.foreignCurrencyId = foreignCurrencyId;
	}

	public String getForeignCurrencyCode() {
		return foreignCurrencyCode;
	}

	public void setForeignCurrencyCode(String foreignCurrencyCode) {
		this.foreignCurrencyCode = foreignCurrencyCode;
	}

	public BigDecimal getExchangeRateId() {
		return exchangeRateId;
	}

	public void setExchangeRateId(BigDecimal exchangeRateId) {
		this.exchangeRateId = exchangeRateId;
	}

	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public BigDecimal getTargetRate() {
		return targetRate;
	}

	public void setTargetRate(BigDecimal targetRate) {
		this.targetRate = targetRate;
	}

	public BigDecimal getPurposeId() {
		return purposeId;
	}

	public void setPurposeId(BigDecimal purposeId) {
		this.purposeId = purposeId;
	}

	public BigDecimal getSourceOfIncomeId() {
		return sourceOfIncomeId;
	}

	public void setSourceOfIncomeId(BigDecimal sourceOfIncomeId) {
		this.sourceOfIncomeId = sourceOfIncomeId;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public Date getValidToDate() {
		return validToDate;
	}

	public void setValidToDate(Date validToDate) {
		this.validToDate = validToDate;
	}

	public Date getValidFromDate() {
		return validFromDate;
	}

	public void setValidFromDate(Date validFromDate) {
		this.validFromDate = validFromDate;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public BigDecimal getReceiveAmount() {
		return receiveAmount;
	}

	public void setReceiveAmount(BigDecimal receiveAmount) {
		this.receiveAmount = receiveAmount;
	}
	
	@Override
	public String getModelType() {
		return "place-order-dto";
	}

	@Override
	public String toString() {
		return "PlaceOrderDTO [placeOrderId=" + placeOrderId + ", customerId=" + customerId
				+ ", beneficiaryMasterSeqId=" + beneficiaryMasterSeqId + ", civilId=" + civilId + ", baseCurrencyId="
				+ baseCurrencyId + ", baseCurrencyCode=" + baseCurrencyCode + ", foreignCurrencyId=" + foreignCurrencyId
				+ ", foreignCurrencyCode=" + foreignCurrencyCode + ", exchangeRateId=" + exchangeRateId
				+ ", exchangeRate=" + exchangeRate + ", targetRate=" + targetRate + ", purposeId=" + purposeId
				+ ", sourceOfIncomeId=" + sourceOfIncomeId + ", isActive=" + isActive + ", validToDate=" + validToDate
				+ ", validFromDate=" + validFromDate + ", payAmount=" + payAmount + ", receiveAmount=" + receiveAmount
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((placeOrderId == null) ? 0 : placeOrderId.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlaceOrderDTO other = (PlaceOrderDTO) obj;
		if (placeOrderId == null) {
			if (other.placeOrderId != null)
				return false;
		} else if (!placeOrderId.equals(other.placeOrderId))
			return false;
		return true;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
