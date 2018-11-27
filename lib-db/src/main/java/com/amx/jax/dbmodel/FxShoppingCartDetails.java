package com.amx.jax.dbmodel;

/**
 * Author: Rabil
 * Purpost : to get the record for FC Sale and Remittance Application
 * Date		: 11/11/2018
 */

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Clob;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.amx.jax.dict.AmxEnums.DenominationType;
@Entity
@Table(name="JAX_FX_VW_APPLICATION_DETAILS")
public class FxShoppingCartDetails implements Serializable {
	
	
	private static final long serialVersionUID = 2315791709068216697L;
	

	@Id
	@Column(name="APPLICATION_DETAILS_ID")
	private BigDecimal applicationId;
	@Column(name="COMPANY_ID")
	private BigDecimal companyId; 
	@Column(name="CUSTOMER_ID")
	private BigDecimal customerId;
	@Column(name="APPLICATION_COUNTRY_ID")
	private BigDecimal applicationCountryId;
	@Column(name="DOCUMENT_FINANCE_YEAR")
	private BigDecimal documentFinanceYear; 
	@Column(name="ISACTIVE")
	private String isActive; 
	@Column(name="FOREIGN_TRANX_AMOUNT")
	private BigDecimal foreignTranxAmount; 
	@Column(name="LOCAL_TRNX_AMOUNT")
	private BigDecimal localTranxAmount;
	@Column(name="LOCAL_NET_AMOUNT")        
	private BigDecimal localNextTranxAmount;
	@Column(name="APPLICATION_TYPE")
	private String applicationType; 
	@Column(name="EXCHANGE_RATE_APPLIED")
	private BigDecimal exchangeRateApplied;
	@Column(name="SOURCE_OF_INCOME_ID")
	private BigDecimal sourceofincome;
	@Column(name="SOURCE_OF_INCOME_DESC")
	private String sourceOfIncomeDesc;
	@Column(name="DOCUMENT_NO")
	private BigDecimal documentNo;
	@Column(name="FOREIGN_CURRENCY_ID")
	private BigDecimal foreignCurrency;
	@Column(name="LOCAL_CURRENCY_ID")
	private BigDecimal localCurrency;
	@Column(name="APPLICATION_TYPE_DESC")
	private String applicationTypeDesc;
	@Column(name="DENOMINATION_TYPE")
	private String  denominationTypeDesc;
	@Column(name="PAYG_TRNX_DTLS_ID")
	private BigDecimal pgPaymentSeqDtlId;
	@Column(name="DELIVERY_DET_SEQ_ID")
	private BigDecimal deliveryDetSeqId;
	@Column(name="TRAVEL_START_DATE")
	private Date travelStartDate;
	@Column(name="TRAVEL_END_DATE")
	private Date travelEndDate;
	@Column(name="TRAVEL_COUNTRY_ID")
	private BigDecimal travelCountryId;
	@Column(name="TRAVEL_COUNTRY_NAME")
	private String travelCountryName;
	@Column(name="PURPOSE_ID")
	private BigDecimal purposeOftrnxId;
	@Column(name="PURPOSE_OF_TRANSACTION")
	private String purposeOftrnxDesc;
	@Column(name="FOREIGN_CURRENCY_DESC")
	private String foreignCurrencyDesc;
	@Column(name="QUOTE_NAME")
	private String quoteName;
	@Column(name="FOXRATE")
	private BigDecimal forXRate;
	
	public FxShoppingCartDetails() {
		
	}
	public BigDecimal getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(BigDecimal applicationId) {
		this.applicationId = applicationId;
	}
	
	


	public BigDecimal getCompanyId() {
		return companyId;
	}
	public void setCompanyId(BigDecimal companyId) {
		this.companyId = companyId;
	}
	

	public BigDecimal getCustomerId() {
		return customerId;
	}
	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
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

	
	
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public BigDecimal getForeignTranxAmount() {
		return foreignTranxAmount;
	}
	public void setForeignTranxAmount(BigDecimal foreignTranxAmount) {
		this.foreignTranxAmount = foreignTranxAmount;
	}

	
	public BigDecimal getLocalTranxAmount() {
		return localTranxAmount;
	}
	public void setLocalTranxAmount(BigDecimal localTranxAmount) {
		this.localTranxAmount = localTranxAmount;
	}

	
	public BigDecimal getLocalNextTranxAmount() {
		return localNextTranxAmount;
	}
	public void setLocalNextTranxAmount(BigDecimal localNextTranxAmount) {
		this.localNextTranxAmount = localNextTranxAmount;
	}


	public String getApplicationType() {
		return applicationType;
	}
	public void setApplicationType(String applicationType) {
		this.applicationType = applicationType;
	}
	
	
	
	public BigDecimal getExchangeRateApplied() {
		return exchangeRateApplied;
	}
	public void setExchangeRateApplied(BigDecimal exchangeRateApplied) {
		this.exchangeRateApplied = exchangeRateApplied;
	}

	
	public BigDecimal getDocumentNo() {
		return documentNo;
	}
	public void setDocumentNo(BigDecimal documentNo) {
		this.documentNo = documentNo;
	}
	

	public BigDecimal getForeignCurrency() {
		return foreignCurrency;
	}
	public void setForeignCurrency(BigDecimal foreignCurrency) {
		this.foreignCurrency = foreignCurrency;
	}
	

	public BigDecimal getLocalCurrency() {
		return localCurrency;
	}
	public void setLocalCurrency(BigDecimal localCurrency) {
		this.localCurrency = localCurrency;
	}


	public String getApplicationTypeDesc() {
		return applicationTypeDesc;
	}
	public void setApplicationTypeDesc(String applicationTypeDesc) {
		this.applicationTypeDesc = applicationTypeDesc;
	}

	
	public BigDecimal getSourceofincome() {
		return sourceofincome;
	}
	public void setSourceofincome(BigDecimal sourceofincome) {
		this.sourceofincome = sourceofincome;
	}



	public String getSourceOfIncomeDesc() {
		return sourceOfIncomeDesc;
	}
	public void setSourceOfIncomeDesc(String sourceOfIncomeDesc) {
		this.sourceOfIncomeDesc = sourceOfIncomeDesc;
	}



	public String getForeignCurrencyDesc() {
		return foreignCurrencyDesc;
	}
	public void setForeignCurrencyDesc(String foreignCurrencyDesc) {
		this.foreignCurrencyDesc = foreignCurrencyDesc;
	}


	


	


	public BigDecimal getPgPaymentSeqDtlId() {
		return pgPaymentSeqDtlId;
	}


	public void setPgPaymentSeqDtlId(BigDecimal pgPaymentSeqDtlId) {
		this.pgPaymentSeqDtlId = pgPaymentSeqDtlId;
	}


	public BigDecimal getDeliveryDetSeqId() {
		return deliveryDetSeqId;
	}


	public void setDeliveryDetSeqId(BigDecimal deliveryDetSeqId) {
		this.deliveryDetSeqId = deliveryDetSeqId;
	}

	
	

	
	public Date getTravelStartDate() {
		return travelStartDate;
	}


	public void setTravelStartDate(Date travelStartDate) {
		this.travelStartDate = travelStartDate;
	}


	public Date getTravelEndDate() {
		return travelEndDate;
	}


	public void setTravelEndDate(Date travelEndDate) {
		this.travelEndDate = travelEndDate;
	}


	public BigDecimal getTravelCountryId() {
		return travelCountryId;
	}


	public void setTravelCountryId(BigDecimal travelCountryId) {
		this.travelCountryId = travelCountryId;
	}



	public String getTravelCountryName() {
		return travelCountryName;
	}


	public void setTravelCountryName(String travelCountryName) {
		this.travelCountryName = travelCountryName;
	}


	
	public String getPurposeOftrnxDesc() {
		return purposeOftrnxDesc;
	}


	public void setPurposeOftrnxDesc(String purposeOftrnxDesc) {
		this.purposeOftrnxDesc = purposeOftrnxDesc;
	}
	public BigDecimal getPurposeOftrnxId() {
		return purposeOftrnxId;
	}
	public void setPurposeOftrnxId(BigDecimal purposeOftrnxId) {
		this.purposeOftrnxId = purposeOftrnxId;
	}
	public String getDenominationTypeDesc() {
		return denominationTypeDesc;
	}
	public void setDenominationTypeDesc(String denominationTypeDesc) {
		this.denominationTypeDesc = denominationTypeDesc;
	}
	public String getQuoteName() {
		return quoteName;
	}
	public void setQuoteName(String quoteName) {
		this.quoteName = quoteName;
	}
	public BigDecimal getForXRate() {
		return forXRate;
	}
	public void setForXRate(BigDecimal forXRate) {
		this.forXRate = forXRate;
	}


	

	
	

}
