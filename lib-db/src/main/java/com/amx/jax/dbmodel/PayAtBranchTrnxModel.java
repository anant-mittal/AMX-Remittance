package com.amx.jax.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="JAX_VW_WIRE_TRANSFER")
public class PayAtBranchTrnxModel implements Serializable{
private static final long serialVersionUID = 7309549091432024935L;
	
	@Id
	@Column(name="ID_NO")
	private BigDecimal transactionId;
	
	@Column(name="CONFIRM_DATE")
	private Date confirmDate;
	
	@Column(name="WT_STATUS")
	private String wireTransferStatus;
	
	@Column(name="WT_BANK_ACCOUNTNO")
	private String bankAccountNo;
	
	@Column(name="WT_BANK_NAME")
	private String bankName;
	
	@Column(name="LOCAL_NET_TRANX_AMOUNT")
	private BigDecimal netAmount;
	
	@Column(name="FOREIGN_TRANX_AMOUNT")
	private BigDecimal foreignTransactionAmount;
	
	@Column(name="CUSTOMER_ID")
	private BigDecimal customerId;
	
	@Column(name="IDENTITY_INT")
	private String identityInt;
	
	@Column(name="EXCHANGE_RATE_APPLIED")
	private BigDecimal exchangeRate;
	
	@Column(name="CUSTOMER_NAME")
	private String customerName;
	
	@Column(name="DOCUMENT_DATE")
	private Date documentDate;
	
	@Column(name="APPL_ISACTIVE")
	private String applIsActive;
	
	
	
	@Column(name="TRANSACTION_DOCUMENT_NO")
	private BigDecimal transactionDocumentNo;
	
	@Column(name="IDENTITY_TYPE_ID")
	private BigDecimal identityTypeId;
	
	@Column(name="FOREIGN_CURRENCY_DESC")
	private String foreignCurrencyDescription;
	
	@Column(name="STATUS_DESCRIPTION")
	private String statusDescription;
	
	public String getStatusDescription() {
		return statusDescription;
	}

	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}

	public String getForeignCurrencyDescription() {
		return foreignCurrencyDescription;
	}

	public void setForeignCurrencyDescription(String foreignCurrencyDescription) {
		this.foreignCurrencyDescription = foreignCurrencyDescription;
	}

	public BigDecimal getIdentityTypeId() {
		return identityTypeId;
	}

	public void setIdentityTypeId(BigDecimal identityTypeId) {
		this.identityTypeId = identityTypeId;
	}

	public String getApplIsActive() {
		return applIsActive;
	}

	public void setApplIsActive(String applIsActive) {
		this.applIsActive = applIsActive;
	}


	public BigDecimal getTransactionDocumentNo() {
		return transactionDocumentNo;
	}

	public void setTransactionDocumentNo(BigDecimal transactionDocumentNo) {
		this.transactionDocumentNo = transactionDocumentNo;
	}

	public Date getDocumentDate() {
		return documentDate;
	}

	public void setDocumentDate(Date documentDate) {
		this.documentDate = documentDate;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	public String getIdentityInt() {
		return identityInt;
	}

	public void setIdentityInt(String identityInt) {
		this.identityInt = identityInt;
	}

	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public BigDecimal getForeignTransactionAmount() {
		return foreignTransactionAmount;
	}

	public void setForeignTransactionAmount(BigDecimal foreignTransactionAmount) {
		this.foreignTransactionAmount = foreignTransactionAmount;
	}

	public BigDecimal getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(BigDecimal transactionId) {
		this.transactionId = transactionId;
	}

	public Date getConfirmDate() {
		return confirmDate;
	}

	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}

	public String getWireTransferStatus() {
		return wireTransferStatus;
	}

	public void setWireTransferStatus(String wireTransferStatus) {
		this.wireTransferStatus = wireTransferStatus;
	}

	public String getBankAccountNo() {
		return bankAccountNo;
	}

	public void setBankAccountNo(String bankAccountNo) {
		this.bankAccountNo = bankAccountNo;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public BigDecimal getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(BigDecimal netAmount) {
		this.netAmount = netAmount;
	}

	
}
