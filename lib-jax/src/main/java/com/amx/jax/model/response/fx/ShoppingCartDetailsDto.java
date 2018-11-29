package com.amx.jax.model.response.fx;

/**
 * Author	: Rabil
 * Purpose	: shopping cart details.
 */
import java.math.BigDecimal;
import java.util.Date;

import com.amx.jax.AbstractModel;
import com.amx.jax.dict.AmxEnums.DenominationType;

public class ShoppingCartDetailsDto extends AbstractModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5509435387736576360L;
	private BigDecimal applicationId;
	private BigDecimal companyId;
	private BigDecimal documentFinanceYear;
	private BigDecimal documentId;
	private String isActive;
	
	private BigDecimal remittanceApplicationId;
	private BigDecimal foreignTranxAmount;
	private BigDecimal localTranxAmount;
	private BigDecimal localCommisionAmount;
	private BigDecimal localChargeAmount;
	private BigDecimal localDeliveryAmount;
	private BigDecimal localNetTranxAmount;
	private String applicationType;
	private BigDecimal customerId;
	private BigDecimal exchangeRateApplied;
	private BigDecimal documentNo;
	private BigDecimal foreignCurrency;
	private BigDecimal localCurrency;
	private String spldeal;
	private String applicationTypeDesc;
	private String customerSignature;
	private String sourceofincome;
	private String BeneCityName;
	private String BeneStateName;
	private String BeneDistrictName;
	private String instruction;
	private String sourceOfIncomeDesc;
	private String remittanceDescription;
	private String deliveryDescription;
	private String foreignCurrencyDesc;
	private BigDecimal amtbCouponEncashed;
	private DenominationType denominationType;
	private String denominationTypeDesc;
	private BigDecimal applicationCountryId;
	private String pgPaymentId;
	private BigDecimal pgPaymentSeqDtlId;
	private BigDecimal deliveryDetSeqId;
	private Date travelStartDate;
	private Date travelEndDate;
	private BigDecimal travelCountryId;
	private String travelCountryName;
	private String purposeOftrnxId;
	private String purposeOftrnxDesc;
	private String quoteName;
	private BigDecimal forXRate;
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
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public BigDecimal getRemittanceApplicationId() {
		return remittanceApplicationId;
	}
	public void setRemittanceApplicationId(BigDecimal remittanceApplicationId) {
		this.remittanceApplicationId = remittanceApplicationId;
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
	public BigDecimal getLocalCommisionAmount() {
		return localCommisionAmount;
	}
	public void setLocalCommisionAmount(BigDecimal localCommisionAmount) {
		this.localCommisionAmount = localCommisionAmount;
	}
	public BigDecimal getLocalChargeAmount() {
		return localChargeAmount;
	}
	public void setLocalChargeAmount(BigDecimal localChargeAmount) {
		this.localChargeAmount = localChargeAmount;
	}
	public BigDecimal getLocalDeliveryAmount() {
		return localDeliveryAmount;
	}
	public void setLocalDeliveryAmount(BigDecimal localDeliveryAmount) {
		this.localDeliveryAmount = localDeliveryAmount;
	}
	public BigDecimal getLocalNetTranxAmount() {
		return localNetTranxAmount;
	}
	public void setLocalNetTranxAmount(BigDecimal localNetTranxAmount) {
		this.localNetTranxAmount = localNetTranxAmount;
	}
	public String getApplicationType() {
		return applicationType;
	}
	public void setApplicationType(String applicationType) {
		this.applicationType = applicationType;
	}
	public BigDecimal getCustomerId() {
		return customerId;
	}
	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
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
	public String getSpldeal() {
		return spldeal;
	}
	public void setSpldeal(String spldeal) {
		this.spldeal = spldeal;
	}
	public String getApplicationTypeDesc() {
		return applicationTypeDesc;
	}
	public void setApplicationTypeDesc(String applicationTypeDesc) {
		this.applicationTypeDesc = applicationTypeDesc;
	}
	public String getCustomerSignature() {
		return customerSignature;
	}
	public void setCustomerSignature(String customerSignature) {
		this.customerSignature = customerSignature;
	}
	public String getSourceofincome() {
		return sourceofincome;
	}
	public void setSourceofincome(String sourceofincome) {
		this.sourceofincome = sourceofincome;
	}
	public String getBeneCityName() {
		return BeneCityName;
	}
	public void setBeneCityName(String beneCityName) {
		BeneCityName = beneCityName;
	}
	public String getBeneStateName() {
		return BeneStateName;
	}
	public void setBeneStateName(String beneStateName) {
		BeneStateName = beneStateName;
	}
	public String getBeneDistrictName() {
		return BeneDistrictName;
	}
	public void setBeneDistrictName(String beneDistrictName) {
		BeneDistrictName = beneDistrictName;
	}
	public String getInstruction() {
		return instruction;
	}
	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}
	public String getSourceOfIncomeDesc() {
		return sourceOfIncomeDesc;
	}
	public void setSourceOfIncomeDesc(String sourceOfIncomeDesc) {
		this.sourceOfIncomeDesc = sourceOfIncomeDesc;
	}
	public String getRemittanceDescription() {
		return remittanceDescription;
	}
	public void setRemittanceDescription(String remittanceDescription) {
		this.remittanceDescription = remittanceDescription;
	}
	public String getDeliveryDescription() {
		return deliveryDescription;
	}
	public void setDeliveryDescription(String deliveryDescription) {
		this.deliveryDescription = deliveryDescription;
	}
	public String getForeignCurrencyDesc() {
		return foreignCurrencyDesc;
	}
	public void setForeignCurrencyDesc(String foreignCurrencyDesc) {
		this.foreignCurrencyDesc = foreignCurrencyDesc;
	}
	public BigDecimal getAmtbCouponEncashed() {
		return amtbCouponEncashed;
	}
	public void setAmtbCouponEncashed(BigDecimal amtbCouponEncashed) {
		this.amtbCouponEncashed = amtbCouponEncashed;
	}
	public DenominationType getDenominationType() {
		return denominationType;
	}
	public void setDenominationType(DenominationType denominationType) {
		this.denominationType = denominationType;
	}
	public String getDenominationTypeDesc() {
		return denominationTypeDesc;
	}
	public void setDenominationTypeDesc(String denominationTypeDesc) {
		this.denominationTypeDesc = denominationTypeDesc;
	}
	public BigDecimal getApplicationCountryId() {
		return applicationCountryId;
	}
	public void setApplicationCountryId(BigDecimal applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}
	public String getPgPaymentId() {
		return pgPaymentId;
	}
	public void setPgPaymentId(String pgPaymentId) {
		this.pgPaymentId = pgPaymentId;
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
	public String getPurposeOftrnxId() {
		return purposeOftrnxId;
	}
	public void setPurposeOftrnxId(String purposeOftrnxId) {
		this.purposeOftrnxId = purposeOftrnxId;
	}
	public String getPurposeOftrnxDesc() {
		return purposeOftrnxDesc;
	}
	public void setPurposeOftrnxDesc(String purposeOftrnxDesc) {
		this.purposeOftrnxDesc = purposeOftrnxDesc;
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
