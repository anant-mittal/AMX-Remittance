package com.amx.jax.dbmodel.fx;


import java.io.Serializable;
import java.math.BigDecimal;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="JAX_FX_ORDER_TRANSACTION")
public class FxOrderTransactionModel implements Serializable {

	private static final long serialVersionUID = -7649308603857511243L;
	

	@Id
	@Column(name = "IDNO")
	private BigDecimal idno;
	
	@Column(name = "CUSTOMER_REFERENCE")
	private BigDecimal customerReference;
	
	@Column(name = "DOCUMENT_NO")
	private BigDecimal documentNumber;
	

	@Column(name = "DOCUMENT_DATE")
	private String documentDate;
	
	@Column(name = "DOCUMENT_FINANCE_YEAR")
	private BigDecimal documentFinanceYear;
	
	@Column(name = "FOREIGN_CURCOD")
	private String foreignCurrencyCode;
	
	@Column(name = "FOREIGN_TRANX_AMOUNT")
	private BigDecimal foreignTransactionAmount;
	
	@Column(name = "QUOTE_NAME")
	private String currencyQuoteName;
	

	
	@Column(name = "TRANSACTION_TYPE_DESC")
	private String transactionTypeDesc;
	
	@Column(name = "COLLECTION_DOCUMENT_NO")
	private BigDecimal collectionDocumentNo;
		
	@Column(name = "COLLECTION_DOC_CODE")
	private BigDecimal collectionDocumentCode;
	
	@Column(name = "COLLECTION_DOC_FINANCE_YEAR")
	private BigDecimal  collectionDocumentFinYear;
	
	@Column(name="COUNTRY_BRANCH_NAME")
	private String branchDesc;
	
	@Column(name="CUSTOMER_ID")
	private BigDecimal customerId;
	
	@Column(name="LOCAL_TRNX_AMOUNT")
	private BigDecimal localTrnxAmount;
	
	@Column(name="SOURCE_OF_INCOME")
	private BigDecimal sourceOfIncomeId;
	
	
	@Column(name = "PURPOSE_OF_TRANSACTION")
	private String purposeOfTrnx;
	
	@Column(name="DOCUMENT_CODE")
	private BigDecimal documentCode;
	
	@Column(name="DELIVERY_DET_SEQ_ID")
	private BigDecimal deliveryDetSeqId;
	
	@Column(name="PAYG_TRNX_DTLS_ID")
	private BigDecimal pagDetSeqId;
	
	@Column(name="ORDER_STATUS")
	private String orderStatus;
	
	@Column(name="DELIVERY_DATE")
	private String deliveryDate;
	
	@Column(name="DELIVERY_TIME")
	private String deliveryTime;
	
	@Column(name="CREATED_DATE")
	private String createdDate;
	
	
	@Column(name="CUSTOMER_NAME")
	private String customerName;
	
	@Column(name="DELIVERY_CHARGES")
	private BigDecimal deliveryCharges;
	
	@Column(name="SOURCE_OF_INCOME_DESC")
	private String sourceOfIncomeDesc;
	


	@Column(name="TRAVEL_COUNTRY_NAME")
	private String travelCountryName;
	
	@Column(name="TRAVEL_DATE_RANGE")
	private String travelDateRange;
	
	@Column(name="LOCAL_CURRENCY_ID")
	private BigDecimal localCurrencyId;
	
	@Column(name="LOCAL_CURR_QUOTE_NAME")
	private String localCurrQuoteName;
	
	@Column(name="TRANSACTION_ACTUAL_RATE")
	private BigDecimal exchangeRate;
	
	@Column(name="TRANSACTION_REF_NO")
	private String  transactionReferenceNo;
	
	@Column(name="ORDER_STATUS_CODE")
	private String orderStatusCode;
	

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

	public String getDocumentDate() {
		return documentDate;
	}

	public void setDocumentDate(String documentDate) {
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

	public BigDecimal getDeliveryDetSeqId() {
		return deliveryDetSeqId;
	}

	public void setDeliveryDetSeqId(BigDecimal deliveryDetSeqId) {
		this.deliveryDetSeqId = deliveryDetSeqId;
	}

	public BigDecimal getPagDetSeqId() {
		return pagDetSeqId;
	}

	public void setPagDetSeqId(BigDecimal pagDetSeqId) {
		this.pagDetSeqId = pagDetSeqId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public BigDecimal getDeliveryCharges() {
		return deliveryCharges;
	}

	public void setDeliveryCharges(BigDecimal deliveryCharges) {
		this.deliveryCharges = deliveryCharges;
	}

	public String getSourceOfIncomeDesc() {
		return sourceOfIncomeDesc;
	}

	public void setSourceOfIncomeDesc(String sourceOfIncomeDesc) {
		this.sourceOfIncomeDesc = sourceOfIncomeDesc;
	}

	public String getTravelCountryName() {
		return travelCountryName;
	}

	public void setTravelCountryName(String travelCountryName) {
		this.travelCountryName = travelCountryName;
	}

	public String getTravelDateRange() {
		return travelDateRange;
	}

	public void setTravelDateRange(String travelDateRange) {
		this.travelDateRange = travelDateRange;
	}

	public BigDecimal getLocalCurrencyId() {
		return localCurrencyId;
	}

	public void setLocalCurrencyId(BigDecimal localCurrencyId) {
		this.localCurrencyId = localCurrencyId;
	}

	public String getLocalCurrQuoteName() {
		return localCurrQuoteName;
	}

	public void setLocalCurrQuoteName(String localCurrQuoteName) {
		this.localCurrQuoteName = localCurrQuoteName;
	}

	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getOrderStatusCode() {
		return orderStatusCode;
	}

	public void setOrderStatusCode(String orderStatusCode) {
		this.orderStatusCode = orderStatusCode;
	}

}





