package com.amx.jax.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author Subodh Bhoir
 *
 */
@Entity
@Table(name = "JAX_ONLINE_PLACE_ORDER")
public class PlaceOrder implements Serializable {

	private static final long serialVersionUID = 1L;

	private BigDecimal onlinePlaceOrderId;
	private BigDecimal customerId;
	private BigDecimal beneficiaryRelationshipSeqId;
	private BigDecimal targetExchangeRate;
	private BigDecimal bankRuleFieldId;
	private BigDecimal srlId;
	private BigDecimal sourceOfIncomeId;
	private String isActive;
	private Date validToDate;
	private Date validFromDate;
	private BigDecimal payAmount;
	private BigDecimal receiveAmount;
	private Date createdDate;
	private String createdBy;
	private Date updatedDate;
	private String updatedBy;

	@Id
	@GeneratedValue(generator = "jax_online_place_order_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "jax_online_place_order_seq", sequenceName = "JAX_ONLINE_PLACE_ORDER_SEQ", allocationSize = 1)
	@Column(name = "ONLINE_PLACE_ORDER_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getOnlinePlaceOrderId() {
		return onlinePlaceOrderId;
	}

	public void setOnlinePlaceOrderId(BigDecimal onlinePlaceOrderId) {
		this.onlinePlaceOrderId = onlinePlaceOrderId;
	}

	@Column(name = "CUSTOMER_ID")
	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	@Column(name = "BENEFICARY_RELATIONSHIP_SEQ_ID")
	public BigDecimal getBeneficiaryRelationshipSeqId() {
		return beneficiaryRelationshipSeqId;
	}

	public void setBeneficiaryRelationshipSeqId(BigDecimal beneficiaryRelationshipSeqId) {
		this.beneficiaryRelationshipSeqId = beneficiaryRelationshipSeqId;
	}

	@Column(name = "TARGET_EXCHANGE_RATE")
	public BigDecimal getTargetExchangeRate() {
		return targetExchangeRate;
	}

	public void setTargetExchangeRate(BigDecimal targetExchangeRate) {
		this.targetExchangeRate = targetExchangeRate;
	}

	@Column(name = "ADDL_BANK_RULE_FIELD_ID")
	public BigDecimal getBankRuleFieldId() {
		return bankRuleFieldId;
	}

	public void setBankRuleFieldId(BigDecimal bankRuleFieldId) {
		this.bankRuleFieldId = bankRuleFieldId;
	}

	@Column(name = "SRL_ID")
	public BigDecimal getSrlId() {
		return srlId;
	}

	public void setSrlId(BigDecimal srlId) {
		this.srlId = srlId;
	}

	@Column(name = "SOURCE_OF_INCOME_ID")
	public BigDecimal getSourceOfIncomeId() {
		return sourceOfIncomeId;
	}

	public void setSourceOfIncomeId(BigDecimal sourceOfIncomeId) {
		this.sourceOfIncomeId = sourceOfIncomeId;
	}

	@Column(name = "ISACTIVE")
	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	@Column(name = "VALID_TO_DATE")
	public Date getValidToDate() {
		return validToDate;
	}

	public void setValidToDate(Date validToDate) {
		this.validToDate = validToDate;
	}

	@Column(name = "VALID_FROM_DATE")
	public Date getValidFromDate() {
		return validFromDate;
	}

	public void setValidFromDate(Date validFromDate) {
		this.validFromDate = validFromDate;
	}

	@Column(name = "PAY_AMOUNT")
	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	@Column(name = "RECEIVE_AMOUNT")
	public BigDecimal getReceiveAmount() {
		return receiveAmount;
	}

	public void setReceiveAmount(BigDecimal receiveAmount) {
		this.receiveAmount = receiveAmount;
	}

	@Column(name = "CREATED_DATE")
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "CREATED_BY")
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "UPDATED_DATE")
	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	@Column(name = "UPDATED_BY")
	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

}
