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
	private BigDecimal remittanceApplicationId;
	private BigDecimal remittanceTransactionId;
	private Date firstRateMatchDate;
    private BigDecimal currencyId;
	private BigDecimal bankId;
	private BigDecimal countryId;
	private Date notificationDate;
	private BigDecimal baseCurrencyId;
	private String baseCurrencyQuote;
	private BigDecimal foreignCurrencyId;
	private String foreignCurrencyQuote;
	
	

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

	@Column(name = "REMITTANCE_APPLICATION_ID")
    public BigDecimal getRemittanceApplicationId() {
        return remittanceApplicationId;
    }

    public void setRemittanceApplicationId(BigDecimal remittanceApplicationId) {
        this.remittanceApplicationId = remittanceApplicationId;
    }

    @Column(name = "REMITTANCE_TRANSACTION_ID")
    public BigDecimal getRemittanceTransactionId() {
        return remittanceTransactionId;
    }

    public void setRemittanceTransactionId(BigDecimal remittanceTransactionId) {
        this.remittanceTransactionId = remittanceTransactionId;
    }

    @Column(name = "FIRST_RATE_MATCH_DATE")
    public Date getFirstRateMatchDate() {
        return firstRateMatchDate;
    }

    public void setFirstRateMatchDate(Date firstRateMatchDate) {
        this.firstRateMatchDate = firstRateMatchDate;
    }

    @Column(name = "CURRENCY_ID")
    public BigDecimal getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(BigDecimal currencyId) {
        this.currencyId = currencyId;
    }

    @Column(name = "BANK_ID")
    public BigDecimal getBankId() {
        return bankId;
    }

    public void setBankId(BigDecimal bankId) {
        this.bankId = bankId;
    }

    @Column(name = "COUNTRY_ID")
    public BigDecimal getCountryId() {
        return countryId;
    }

    public void setCountryId(BigDecimal countryId) {
        this.countryId = countryId;
    }

    @Column(name = "NOTIFICATION_DATE")
    public Date getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(Date notificationDate) {
        this.notificationDate = notificationDate;
    }

    @Column(name = "BASE_CURRENCY_ID")
	public BigDecimal getBaseCurrencyId() {
		return baseCurrencyId;
	}

	public void setBaseCurrencyId(BigDecimal baseCurrencyId) {
		this.baseCurrencyId = baseCurrencyId;
	}

	@Column(name = "BASE_CURRENCY_CODE")
	public String getBaseCurrencyQuote() {
		return baseCurrencyQuote;
	}

	public void setBaseCurrencyQuote(String baseCurrencyQuote) {
		this.baseCurrencyQuote = baseCurrencyQuote;
	}

	@Column(name = "FOREIGN_CURRENCY_ID")
	public BigDecimal getForeignCurrencyId() {
		return foreignCurrencyId;
	}

	public void setForeignCurrencyId(BigDecimal foreignCurrencyId) {
		this.foreignCurrencyId = foreignCurrencyId;
	}

	@Column(name = "FOREIGN_CURRENCY_CODE")
	public String getForeignCurrencyQuote() {
		return foreignCurrencyQuote;
	}

	
	public void setForeignCurrencyQuote(String foreignCurrencyQuote) {
		this.foreignCurrencyQuote = foreignCurrencyQuote;
	}

}
