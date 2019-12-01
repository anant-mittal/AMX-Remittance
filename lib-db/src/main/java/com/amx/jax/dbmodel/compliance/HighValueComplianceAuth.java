package com.amx.jax.dbmodel.compliance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "VW_EX_COMPLIANCE_AUTHORIZE")
public class HighValueComplianceAuth implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_NO")
	private BigDecimal idNo;

	@Column(name = "APPLICATION_COUNTRY_ID")
	private BigDecimal applicationCountryId;

	@Column(name = "DOCUMENT_FINANCE_YEAR")
	private BigDecimal documentFinanceYear;

	@Column(name = "DOCUMENT_ID")
	private BigDecimal documentId;

	@Column(name = "LOCAL_NET_TRANX_AMOUNT")
	private BigDecimal localTransactionAmount;

	@Column(name = "DOCUMENT_NO")
	private BigDecimal documentNo;

	@Column(name = "DOCUMENT_DATE")
	private Date documentDate;

	@Column(name = "CUSTOMER_ID")
	private BigDecimal customerId;

	@Column(name = "CUSTOMER_REFERENCE")
	private BigDecimal customerReference;

	@Column(name = "CUSTOMER_NAME")
	private String customerName;

	@Column(name = "FOREIGN_TRANX_AMOUNT")
	private BigDecimal foreignTrnxAmount;

	@Column(name = "FOREIGN_CURRENCY_ID")
	private BigDecimal foreignCurrencyId;

	@Column(name = "FOREIGN_CURRENCY_DESC")
	private String foreignCurrencyDesc;

	@Column(name = "BANK_CODE")
	private String bankCode;

	@Column(name = "BRANCH_NAME")
	private String branchName;

	@Column(name = "HIGH_VALUE_TRANX")
	private String hvtLocal;

	@Column(name = "FC_HIGH_VALUE_TRANX")
	private String hvtFc;

	@Column(name = "SUSPICIOUS_TRNX")
	private String suspiciousTrnx;

	public BigDecimal getIdNo() {
		return idNo;
	}

	public void setIdNo(BigDecimal idNo) {
		this.idNo = idNo;
	}

	public BigDecimal getApplicationCountryId() {
		return applicationCountryId;
	}

	public void setApplicationCountryId(BigDecimal applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}

	public BigDecimal getDocumentFinanceYear() {
		return documentFinanceYear;
	}

	public void setDocumentFinanceYear(BigDecimal documentFinanceYear) {
		this.documentFinanceYear = documentFinanceYear;
	}

	public BigDecimal getDocumentId() {
		return documentId;
	}

	public void setDocumentId(BigDecimal documentId) {
		this.documentId = documentId;
	}

	public BigDecimal getDocumentNo() {
		return documentNo;
	}

	public void setDocumentNo(BigDecimal documentNo) {
		this.documentNo = documentNo;
	}

	public Date getDocumentDate() {
		return documentDate;
	}

	public void setDocumentDate(Date documentDate) {
		this.documentDate = documentDate;
	}

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	public BigDecimal getCustomerReference() {
		return customerReference;
	}

	public void setCustomerReference(BigDecimal customerReference) {
		this.customerReference = customerReference;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public BigDecimal getForeignTrnxAmount() {
		return foreignTrnxAmount;
	}

	public void setForeignTrnxAmount(BigDecimal foreignTrnxAmount) {
		this.foreignTrnxAmount = foreignTrnxAmount;
	}

	public BigDecimal getForeignCurrencyId() {
		return foreignCurrencyId;
	}

	public void setForeignCurrencyId(BigDecimal foreignCurrencyId) {
		this.foreignCurrencyId = foreignCurrencyId;
	}

	public BigDecimal getLocalTransactionAmount() {
		return localTransactionAmount;
	}

	public void setLocalTransactionAmount(BigDecimal localTransactionAmount) {
		this.localTransactionAmount = localTransactionAmount;
	}

	public String getForeignCurrencyDesc() {
		return foreignCurrencyDesc;
	}

	public void setForeignCurrencyDesc(String foreignCurrencyDesc) {
		this.foreignCurrencyDesc = foreignCurrencyDesc;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getHvtLocal() {
		return hvtLocal;
	}

	public void setHvtLocal(String hvtLocal) {
		this.hvtLocal = hvtLocal;
	}

	public String getHvtFc() {
		return hvtFc;
	}

	public void setHvtFc(String hvtFc) {
		this.hvtFc = hvtFc;
	}

	public String getSuspiciousTrnx() {
		return suspiciousTrnx;
	}

	public void setSuspiciousTrnx(String suspiciousTrnx) {
		this.suspiciousTrnx = suspiciousTrnx;
	}

}
