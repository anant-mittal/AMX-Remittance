package com.amx.jax.dbmodel;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "JAX_VW_EX_TRANSACTION_INQUIRY")
public class CustomerRemittanceTransactionView implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "IDNO")
	private BigDecimal idno;
	
	@Column(name = "CUSTOMER_REFERENCE")
	private BigDecimal customerReference;
	
	@Column(name = "DOCUMENT_NO")
	private BigDecimal documentNumber;
	
	@Column(name = "BENEFICIARY_ACCOUNT_NO")
	private String beneficaryAccountNumber;
	
	@Column(name = "BENEFICIARY_BANK")
	private String beneficaryBankName;
	
	@Column(name = "BENEFICIARY_BRANCH")
	private String beneficaryBranchName;
	
	@Column(name = "BENEFICIARY_NAME")
	private String beneficaryName;
	
	@Column(name = "CORRESPONDING_BANK")
	private String beneficaryCorespondingBankName;
	
	//@Temporal(TemporalType.DATE)
	@Column(name = "DOCUMENT_DATE")
	private Date documentDate;
	
	@Column(name = "DOCUMENT_FINANCE_YEAR")
	private BigDecimal documentFinanceYear;
	
	@Column(name = "FOREIGN_CURCOD")
	private String foreignCurrencyCode;
	
	@Column(name = "FOREIGN_TRANX_AMOUNT")
	private BigDecimal foreignTransactionAmount;
	
	@Column(name = "QUOTE_NAME")
	private String currencyQuoteName;
	
	@Column(name = "SERVICE_DESCRIPTION")
	private String serviceDescription;
	
	@Column(name = "TRANSACTION_STATUS_DESC")
	private String transactionStatusDesc;
	
	@Column(name = "TRANSACTION_TYPE_DESC")
	private String transactionTypeDesc;
	
	@Column(name = "COLLECTION_DOCUMENT_NO")
	private BigDecimal collectionDocumentNo;
		
	@Column(name = "COLLECTION_DOC_CODE")
	private BigDecimal collectionDocumentCode;
	
	@Column(name = "COLLECTION_DOC_FINANCE_YEAR")
	private BigDecimal  collectionDocumentFinYear;
	
	@Column(name="BRANCH_DESC")
	private String branchDesc;
	
	@Column(name="CUSTOMER_ID")
	private BigDecimal customerId;
	
	@Column(name="BENEFICARY_RELATIONSHIP_SEQ_ID")
	private BigDecimal beneficiaryRelationSeqId;
	
	
	@Column(name="LOCAL_TRANX_AMOUNT")
	private BigDecimal localTrnxAmount;
	
	@Column(name="SOURCE_OF_INCOME_ID")
	private BigDecimal sourceOfIncomeId;
	
	
	@Column(name = "PURPOSE_OF_TRNX_ID")
	private String purposeOfTrnx;
	
	@Column(name="DOCUMENT_CODE")
	private BigDecimal documentCode;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	
	

	public CustomerRemittanceTransactionView() {
	}

	public CustomerRemittanceTransactionView(BigDecimal idno, BigDecimal customerReference, BigDecimal documentNumber, String beneficaryAccountNumber, String beneficaryBankName, String beneficaryBranchName, String beneficaryName, String beneficaryCorespondingBankName, Date documentDate,
			BigDecimal documentFinanceYear, String foreignCurrencyCode, BigDecimal foreignTransactionAmount, String currencyQuoteName, String serviceDescription, String transactionStatusDesc, String transactionTypeDesc,BigDecimal collectionDocumentNo,BigDecimal collectionDocumentCode,BigDecimal  collectionDocumentFinYear) {
		super();
		this.idno = idno;
		this.customerReference = customerReference;
		this.documentNumber = documentNumber;
		this.beneficaryAccountNumber = beneficaryAccountNumber;
		this.beneficaryBankName = beneficaryBankName;
		this.beneficaryBranchName = beneficaryBranchName;
		this.beneficaryName = beneficaryName;
		this.beneficaryCorespondingBankName = beneficaryCorespondingBankName;
		this.documentDate = documentDate;
		this.documentFinanceYear = documentFinanceYear;
		this.foreignCurrencyCode = foreignCurrencyCode;
		this.foreignTransactionAmount = foreignTransactionAmount;
		this.currencyQuoteName = currencyQuoteName;
		this.serviceDescription = serviceDescription;
		this.transactionStatusDesc = transactionStatusDesc;
		this.transactionTypeDesc = transactionTypeDesc;
		this.collectionDocumentNo=collectionDocumentNo;
		this.collectionDocumentCode=collectionDocumentCode;
		this.collectionDocumentFinYear=collectionDocumentFinYear;
	}

	public BigDecimal getIdno() {
		return idno;
	}
	public void setIdno(BigDecimal idno) {
		this.idno = idno;
	}

	public BigDecimal getCustomerReference() {
		return customerReference;
	}
	public void setCustomerReference(BigDecimal customerReference) {
		this.customerReference = customerReference;
	}

	public BigDecimal getDocumentNumber() {
		return documentNumber;
	}
	public void setDocumentNumber(BigDecimal documentNumber) {
		this.documentNumber = documentNumber;
	}

	public String getBeneficaryAccountNumber() {
		return beneficaryAccountNumber;
	}
	public void setBeneficaryAccountNumber(String beneficaryAccountNumber) {
		this.beneficaryAccountNumber = beneficaryAccountNumber;
	}

	public String getBeneficaryBankName() {
		return beneficaryBankName;
	}
	public void setBeneficaryBankName(String beneficaryBankName) {
		this.beneficaryBankName = beneficaryBankName;
	}

	public String getBeneficaryBranchName() {
		return beneficaryBranchName;
	}
	public void setBeneficaryBranchName(String beneficaryBranchName) {
		this.beneficaryBranchName = beneficaryBranchName;
	}

	public String getBeneficaryName() {
		return beneficaryName;
	}
	public void setBeneficaryName(String beneficaryName) {
		this.beneficaryName = beneficaryName;
	}

	public String getBeneficaryCorespondingBankName() {
		return beneficaryCorespondingBankName;
	}
	public void setBeneficaryCorespondingBankName(String beneficaryCorespondingBankName) {
		this.beneficaryCorespondingBankName = beneficaryCorespondingBankName;
	}

	public Date getDocumentDate() {
		return documentDate;
	}
	public void setDocumentDate(Date documentDate) {
		this.documentDate = documentDate;
	}

	public BigDecimal getDocumentFinanceYear() {
		return documentFinanceYear;
	}
	public void setDocumentFinanceYear(BigDecimal documentFinanceYear) {
		this.documentFinanceYear = documentFinanceYear;
	}

	public String getForeignCurrencyCode() {
		return foreignCurrencyCode;
	}
	public void setForeignCurrencyCode(String foreignCurrencyCode) {
		this.foreignCurrencyCode = foreignCurrencyCode;
	}

	public BigDecimal getForeignTransactionAmount() {
		return foreignTransactionAmount;
	}
	public void setForeignTransactionAmount(BigDecimal foreignTransactionAmount) {
		this.foreignTransactionAmount = foreignTransactionAmount;
	}

	public String getCurrencyQuoteName() {
		return currencyQuoteName;
	}
	public void setCurrencyQuoteName(String currencyQuoteName) {
		this.currencyQuoteName = currencyQuoteName;
	}

	public String getServiceDescription() {
		return serviceDescription;
	}
	public void setServiceDescription(String serviceDescription) {
		this.serviceDescription = serviceDescription;
	}

	public String getTransactionStatusDesc() {
		return transactionStatusDesc;
	}
	public void setTransactionStatusDesc(String transactionStatusDesc) {
		this.transactionStatusDesc = transactionStatusDesc;
	}

	public String getTransactionTypeDesc() {
		return transactionTypeDesc;
	}
	public void setTransactionTypeDesc(String transactionTypeDesc) {
		this.transactionTypeDesc = transactionTypeDesc;
	}

	public BigDecimal getCollectionDocumentNo() {
		return collectionDocumentNo;
	}
	public void setCollectionDocumentNo(BigDecimal collectionDocumentNo) {
		this.collectionDocumentNo = collectionDocumentNo;
	}
	
	public BigDecimal getCollectionDocumentCode() {
		return collectionDocumentCode;
	}
	public void setCollectionDocumentCode(BigDecimal collectionDocumentCode) {
		this.collectionDocumentCode = collectionDocumentCode;
	}

	public BigDecimal getCollectionDocumentFinYear() {
		return collectionDocumentFinYear;
	}
	public void setCollectionDocumentFinYear(BigDecimal collectionDocumentFinYear) {
		this.collectionDocumentFinYear = collectionDocumentFinYear;
	}

	public String getBranchDesc() {
		return branchDesc;
	}

	public void setBranchDesc(String branchDesc) {
		this.branchDesc = branchDesc;
	}

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	public BigDecimal getBeneficiaryRelationSeqId() {
		return beneficiaryRelationSeqId;
	}

	public void setBeneficiaryRelationSeqId(BigDecimal beneficiaryRelationSeqId) {
		this.beneficiaryRelationSeqId = beneficiaryRelationSeqId;
	}

	public BigDecimal getLocalTrnxAmount() {
		return localTrnxAmount;
	}

	public void setLocalTrnxAmount(BigDecimal localTrnxAmount) {
		this.localTrnxAmount = localTrnxAmount;
	}

	public BigDecimal getSourceOfIncomeId() {
		return sourceOfIncomeId;
	}

	public void setSourceOfIncomeId(BigDecimal sourceOfIncomeId) {
		this.sourceOfIncomeId = sourceOfIncomeId;
	}

	public String getPurposeOfTrnx() {
		return purposeOfTrnx;
	}

	public void setPurposeOfTrnx(String purposeOfTrnx) {
		this.purposeOfTrnx = purposeOfTrnx;
	}

	public BigDecimal getDocumentCode() {
		return documentCode;
	}

	public void setDocumentCode(BigDecimal documentCode) {
		this.documentCode = documentCode;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
}
