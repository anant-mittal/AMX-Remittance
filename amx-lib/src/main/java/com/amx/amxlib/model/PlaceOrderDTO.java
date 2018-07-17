package com.amx.amxlib.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

/**
 * @author Subodh Bhoir
 *
 */
public class PlaceOrderDTO extends AbstractModel implements Cloneable {

	private static final long serialVersionUID = 1L;
	@NotNull
	private BigDecimal placeOrderId;
	
	private BigDecimal customerId;
	
	@NotNull
	private BigDecimal beneficiaryRelationshipSeqId;
	
	@NotNull
	private BigDecimal targetExchangeRate;
	
	@NotNull
	private BigDecimal bankRuleFieldId;
	
	@NotNull
	private BigDecimal srlId;
	
	@NotNull
	private BigDecimal sourceOfIncomeId;
	
	@NotNull
	private String isActive;

	private Date createdDate;
	
	@NotNull
	private Date validToDate;
	
	@NotNull
	private Date validFromDate;
	
	@NotNull
	private BigDecimal payAmount;
	
	@NotNull
	private BigDecimal receiveAmount;
	
	private BigDecimal baseCurrencyId;
	private String baseCurrencyQuote;
	private BigDecimal foreignCurrencyId;
	private String foreignCurrencyQuote;

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

	public BigDecimal getBeneficiaryRelationshipSeqId() {
		return beneficiaryRelationshipSeqId;
	}

	public void setBeneficiaryRelationshipSeqId(BigDecimal beneficiaryRelationshipSeqId) {
		this.beneficiaryRelationshipSeqId = beneficiaryRelationshipSeqId;
	}

	public BigDecimal getTargetExchangeRate() {
		return targetExchangeRate;
	}

	public void setTargetExchangeRate(BigDecimal targetExchangeRate) {
		this.targetExchangeRate = targetExchangeRate;
	}

	public BigDecimal getBankRuleFieldId() {
		return bankRuleFieldId;
	}

	public void setBankRuleFieldId(BigDecimal bankRuleFieldId) {
		this.bankRuleFieldId = bankRuleFieldId;
	}

	public BigDecimal getSrlId() {
		return srlId;
	}

	public void setSrlId(BigDecimal srlId) {
		this.srlId = srlId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public BigDecimal getBaseCurrencyId() {
		return baseCurrencyId;
	}

	public void setBaseCurrencyId(BigDecimal baseCurrencyId) {
		this.baseCurrencyId = baseCurrencyId;
	}

	public String getBaseCurrencyQuote() {
		return baseCurrencyQuote;
	}

	public void setBaseCurrencyQuote(String baseCurrencyQuote) {
		this.baseCurrencyQuote = baseCurrencyQuote;
	}

	public BigDecimal getForeignCurrencyId() {
		return foreignCurrencyId;
	}

	public void setForeignCurrencyId(BigDecimal foreignCurrencyId) {
		this.foreignCurrencyId = foreignCurrencyId;
	}

	public String getForeignCurrencyQuote() {
		return foreignCurrencyQuote;
	}

	public void setForeignCurrencyQuote(String foreignCurrencyQuote) {
		this.foreignCurrencyQuote = foreignCurrencyQuote;
	}

	@Override
	public String getModelType() {
		return "place-order-dto";
	}


	@Override
	public String toString() {
		return "PlaceOrderDTO [placeOrderId=" + placeOrderId + ", customerId=" + customerId
				+ ", beneficiaryRelationshipSeqId=" + beneficiaryRelationshipSeqId + ", targetExchangeRate="
				+ targetExchangeRate + ", bankRuleFieldId=" + bankRuleFieldId + ", srlId=" + srlId
				+ ", sourceOfIncomeId=" + sourceOfIncomeId + ", isActive=" + isActive + ", createdDate=" + createdDate
				+ ", validToDate=" + validToDate + ", validFromDate=" + validFromDate + ", payAmount=" + payAmount
				+ ", receiveAmount=" + receiveAmount + ", baseCurrencyId=" + baseCurrencyId + ", baseCurrencyQuote="
				+ baseCurrencyQuote + ", foreignCurrencyId=" + foreignCurrencyId + ", foreignCurrencyQuote="
				+ foreignCurrencyQuote + "]";
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
