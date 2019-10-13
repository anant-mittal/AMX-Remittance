package com.amx.jax.dbmodel.partner;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "VW_REMIT_TRNX_SRV_DETAIL")
public class TransactionDetailsView implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BigDecimal idNo;
	private BigDecimal customerId;
	private BigDecimal bankId;
	private String flexField;
	private String flexFieldValue;
	private BigDecimal remittanceId;
	private BigDecimal deliveryId;
	private BigDecimal foreignTrnxAmount;
	private String furtherInstruction;
	private BigDecimal documentFinanceYear;
	private BigDecimal documentNo;
	private BigDecimal companyId;
	private BigDecimal documentCode;
	private BigDecimal remittanceTransactionId;
	private String partnerSessionId;
	private BigDecimal amgSessionId;
	private String settlementCurrency;
	private BigDecimal settlementAmount;
	private BigDecimal sourceofIncomeId;
	private BigDecimal remittanceTrnxAddDataId;
	private String remittanceCode;
	private String deliveryCode;
	private String bankCode;
	private String sourceOfIncomeDesc;
	private BigDecimal collDocumentFinanceYear;
	private BigDecimal collDocumentNo;
	private BigDecimal collDocumentId;
	private BigDecimal collDocumentCode;
	private BigDecimal localNetTrnxAmount;
	private BigDecimal beneficiaryRelationShipId;
	private String bankSourceOfFund;
	private String bankPurposeOfTransaction;
	private BigDecimal bankCountryId;
	private BigDecimal applicationCountryId;
	private BigDecimal beneficiaryCountryId;
	private BigDecimal foreignCurrencyId;
	private BigDecimal localCurrencyId;
	private String bankReference;
	
	public TransactionDetailsView() {
		super();
	}
	
	@Id
	@Column(name = "ID_NO")
	public BigDecimal getIdNo() {
		return idNo;
	}
	public void setIdNo(BigDecimal idNo) {
		this.idNo = idNo;
	}
	
	@Column(name = "CUSTOMER_ID")
	public BigDecimal getCustomerId() {
		return customerId;
	}
	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	@Column(name = "BANK_ID")
	public BigDecimal getBankId() {
		return bankId;
	}
	public void setBankId(BigDecimal bankId) {
		this.bankId = bankId;
	}
	
	@Column(name = "FLEX_FIELD")
	public String getFlexField() {
		return flexField;
	}
	public void setFlexField(String flexField) {
		this.flexField = flexField;
	}

	@Column(name = "FLEX_FIELD_VALUE")
	public String getFlexFieldValue() {
		return flexFieldValue;
	}
	public void setFlexFieldValue(String flexFieldValue) {
		this.flexFieldValue = flexFieldValue;
	}

	@Column(name = "REMITTANCE_MODE_ID")
	public BigDecimal getRemittanceId() {
		return remittanceId;
	}
	public void setRemittanceId(BigDecimal remittanceId) {
		this.remittanceId = remittanceId;
	}

	@Column(name = "DELIVERY_MODE_ID")
	public BigDecimal getDeliveryId() {
		return deliveryId;
	}
	public void setDeliveryId(BigDecimal deliveryId) {
		this.deliveryId = deliveryId;
	}

	@Column(name = "FOREIGN_TRANX_AMOUNT")
	public BigDecimal getForeignTrnxAmount() {
		return foreignTrnxAmount;
	}
	public void setForeignTrnxAmount(BigDecimal foreignTrnxAmount) {
		this.foreignTrnxAmount = foreignTrnxAmount;
	}

	@Column(name = "INSTRUCTION")
	public String getFurtherInstruction() {
		return furtherInstruction;
	}
	public void setFurtherInstruction(String furtherInstruction) {
		this.furtherInstruction = furtherInstruction;
	}

	@Column(name = "DOCUMENT_FINANCE_YEAR")
	public BigDecimal getDocumentFinanceYear() {
		return documentFinanceYear;
	}
	public void setDocumentFinanceYear(BigDecimal documentFinanceYear) {
		this.documentFinanceYear = documentFinanceYear;
	}

	@Column(name = "DOCUMENT_NO")
	public BigDecimal getDocumentNo() {
		return documentNo;
	}
	public void setDocumentNo(BigDecimal documentNo) {
		this.documentNo = documentNo;
	}

	@Column(name = "COMPANY_ID")
	public BigDecimal getCompanyId() {
		return companyId;
	}
	public void setCompanyId(BigDecimal companyId) {
		this.companyId = companyId;
	}

	@Column(name = "DOCUMENT_CODE")
	public BigDecimal getDocumentCode() {
		return documentCode;
	}
	public void setDocumentCode(BigDecimal documentCode) {
		this.documentCode = documentCode;
	}

	@Column(name = "REMITTANCE_TRANSACTION_ID")
	public BigDecimal getRemittanceTransactionId() {
		return remittanceTransactionId;
	}
	public void setRemittanceTransactionId(BigDecimal remittanceTransactionId) {
		this.remittanceTransactionId = remittanceTransactionId;
	}

	@Column(name = "PARTNER_SESSION_ID")
	public String getPartnerSessionId() {
		return partnerSessionId;
	}
	public void setPartnerSessionId(String partnerSessionId) {
		this.partnerSessionId = partnerSessionId;
	}

	@Column(name = "AMG_SESSION_ID")
	public BigDecimal getAmgSessionId() {
		return amgSessionId;
	}
	public void setAmgSessionId(BigDecimal amgSessionId) {
		this.amgSessionId = amgSessionId;
	}

	@Column(name = "SETTLEMENT_CURRENCY")
	public String getSettlementCurrency() {
		return settlementCurrency;
	}
	public void setSettlementCurrency(String settlementCurrency) {
		this.settlementCurrency = settlementCurrency;
	}

	@Column(name = "SETTLEMENT_AMOUNT")
	public BigDecimal getSettlementAmount() {
		return settlementAmount;
	}
	public void setSettlementAmount(BigDecimal settlementAmount) {
		this.settlementAmount = settlementAmount;
	}

	@Column(name = "SOURCE_OF_INCOME_ID")
	public BigDecimal getSourceofIncomeId() {
		return sourceofIncomeId;
	}
	public void setSourceofIncomeId(BigDecimal sourceofIncomeId) {
		this.sourceofIncomeId = sourceofIncomeId;
	}

	@Column(name = "REMITTANCE_TRANX_ADD_DATA_ID")
	public BigDecimal getRemittanceTrnxAddDataId() {
		return remittanceTrnxAddDataId;
	}
	public void setRemittanceTrnxAddDataId(BigDecimal remittanceTrnxAddDataId) {
		this.remittanceTrnxAddDataId = remittanceTrnxAddDataId;
	}

	@Column(name = "REMITTANCE_CODE")
	public String getRemittanceCode() {
		return remittanceCode;
	}
	public void setRemittanceCode(String remittanceCode) {
		this.remittanceCode = remittanceCode;
	}

	@Column(name = "DELIVERY_CODE")
	public String getDeliveryCode() {
		return deliveryCode;
	}
	public void setDeliveryCode(String deliveryCode) {
		this.deliveryCode = deliveryCode;
	}

	@Column(name = "BANK_CODE")
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	@Column(name = "SOURCE_OF_INCOME_DESC")
	public String getSourceOfIncomeDesc() {
		return sourceOfIncomeDesc;
	}
	public void setSourceOfIncomeDesc(String sourceOfIncomeDesc) {
		this.sourceOfIncomeDesc = sourceOfIncomeDesc;
	}
	
	@Column(name = "COLLECTION_DOC_FINANCE_YEAR")
	public BigDecimal getCollDocumentFinanceYear() {
		return collDocumentFinanceYear;
	}
	public void setCollDocumentFinanceYear(BigDecimal collDocumentFinanceYear) {
		this.collDocumentFinanceYear = collDocumentFinanceYear;
	}

	@Column(name = "COLLECTION_DOC_NO")
	public BigDecimal getCollDocumentNo() {
		return collDocumentNo;
	}
	public void setCollDocumentNo(BigDecimal collDocumentNo) {
		this.collDocumentNo = collDocumentNo;
	}

	@Column(name = "COLLECTION_DOC_ID")
	public BigDecimal getCollDocumentId() {
		return collDocumentId;
	}
	public void setCollDocumentId(BigDecimal collDocumentId) {
		this.collDocumentId = collDocumentId;
	}

	@Column(name = "LOCAL_NET_TRANX_AMOUNT")
	public BigDecimal getLocalNetTrnxAmount() {
		return localNetTrnxAmount;
	}
	public void setLocalNetTrnxAmount(BigDecimal localNetTrnxAmount) {
		this.localNetTrnxAmount = localNetTrnxAmount;
	}
	
	@Column(name = "COLLECTION_DOC_CODE")
	public BigDecimal getCollDocumentCode() {
		return collDocumentCode;
	}
	public void setCollDocumentCode(BigDecimal collDocumentCode) {
		this.collDocumentCode = collDocumentCode;
	}
	
	@Column(name = "BENEFICIARY_RELATIONSHIP_ID")
	public BigDecimal getBeneficiaryRelationShipId() {
		return beneficiaryRelationShipId;
	}
	public void setBeneficiaryRelationShipId(BigDecimal beneficiaryRelationShipId) {
		this.beneficiaryRelationShipId = beneficiaryRelationShipId;
	}

	@Column(name = "BANK_SOURCE_OF_FUND")
	public String getBankSourceOfFund() {
		return bankSourceOfFund;
	}
	public void setBankSourceOfFund(String bankSourceOfFund) {
		this.bankSourceOfFund = bankSourceOfFund;
	}

	@Column(name = "PURPOSE_OF_TRANX_BANK_CODE")
	public String getBankPurposeOfTransaction() {
		return bankPurposeOfTransaction;
	}
	public void setBankPurposeOfTransaction(String bankPurposeOfTransaction) {
		this.bankPurposeOfTransaction = bankPurposeOfTransaction;
	}

	@Column(name = "BANK_COUNTRY_ID")
	public BigDecimal getBankCountryId() {
		return bankCountryId;
	}
	public void setBankCountryId(BigDecimal bankCountryId) {
		this.bankCountryId = bankCountryId;
	}

	@Column(name = "APPLICATION_COUNTRY_ID")
	public BigDecimal getApplicationCountryId() {
		return applicationCountryId;
	}
	public void setApplicationCountryId(BigDecimal applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}

	@Column(name = "BENEFICIARY_BANK_COUNTRY_ID")
	public BigDecimal getBeneficiaryCountryId() {
		return beneficiaryCountryId;
	}
	public void setBeneficiaryCountryId(BigDecimal beneficiaryCountryId) {
		this.beneficiaryCountryId = beneficiaryCountryId;
	}

	@Column(name = "FOREIGN_CURRENCY_ID")
	public BigDecimal getForeignCurrencyId() {
		return foreignCurrencyId;
	}
	public void setForeignCurrencyId(BigDecimal foreignCurrencyId) {
		this.foreignCurrencyId = foreignCurrencyId;
	}

	@Column(name = "LOCAL_CURRENCY_ID")
	public BigDecimal getLocalCurrencyId() {
		return localCurrencyId;
	}
	public void setLocalCurrencyId(BigDecimal localCurrencyId) {
		this.localCurrencyId = localCurrencyId;
	}

	@Column(name = "BANK_REFERENCE")
	public String getBankReference() {
		return bankReference;
	}
	public void setBankReference(String bankReference) {
		this.bankReference = bankReference;
	}
	
}
