package com.amx.jax.pricer.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "V_EX_DYN_ROUTING_MATRIX")
public class ViewExRoutingMatrix implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1950986379259824103L;

	@Id
	@Column(name = "UUID")
	private String uuid;

	@Column(name = "APPLICATION_COUNTRY_ID")
	private Long applicationCountryId;

	@Column(name = "BENE_COUNTRY_ID")
	private Long beneCountryId;

	@Column(name = "BENE_BANK_ID")
	private Long beneBankId;

	@Column(name = "BENE_BANK_BRANCH_ID")
	private Long beneBankBranchId;

	@Column(name = "CURRENCY_ID")
	private Long currencyId;

	@Column(name = "SERVICE_GROUP_CODE")
	private String serviceGroupCode;

	@Column(name = "COUNTRY_NAME")
	private String countryName;

	@Column(name = "BANK_IND")
	private String bankIndicator;

	@Column(name = "BANK_CODE")
	private String bankCode;

	@Column(name = "BANK_FULL_NAME")
	private String bankFullName;

	@Column(name = "ROUTING_COUNTRY_ID")
	private Long routingCountryId;

	@Column(name = "ROUTING_BANK_CODE")
	private String routingBankCode;

	@Column(name = "ROUTING_BANK_ID")
	private Long routingBankId;

	@Column(name = "BANK_BRANCH_ID")
	private Long bankBranchId;

	@Column(name = "SERVICE_MASTER_ID")
	private Long serviceMasterId;

	@Column(name = "SERVICE_DESCRIPTION")
	private String serviceDescription;

	@Column(name = "REMITTANCE_MODE_ID")
	private Long remittanceModeId;

	@Column(name = "REMITTANCE_DESCRIPTION")
	private String remittanceDescription;

	@Column(name = "DELIVERY_MODE_ID")
	private Long deliveryModeId;

	@Column(name = "DELIVERY_DESCRIPTION")
	private String deliveryDescription;
	
	@Column(name = "WEEK_FROM")
	private Long weekFrom;
	
	@Column(name = "WEEK_TO")
	private Long weekTo;
	
	@Column(name = "WEEK_HOURS_FROM")
	private Long weekHoursFrom;
	
	@Column(name = "WEEK_HOURS_TO")
	private Long weekHoursTo;
	
	@Column(name = "WEEKEND_FROM")
	private Long weekendFrom;
	
	@Column(name = "WEEKEND_TO")
	private Long weekendTo;
	
	@Column(name = "WEEKEND_HOURS_FROM")
	private Long weekendHoursFrom;
	
	@Column(name = "WEEKEND_HOURS_TO")
	private Long weekendHoursTo;
	
	@Column(name = "DELIVERY_MINUTES")
	private Long deliveryMinutes;
	
	@Column(name = "TXN_AMT_FROM")
	private BigDecimal tnxAmtFrom;
	
	@Column(name = "TXN_AMT_TO")
	private BigDecimal tnxAmtTo;
	
	@Column(name = "FROM_AMOUNT")
	private BigDecimal fromAmount;

	@Column(name = "TO_AMOUNT")
	private BigDecimal toAmount;
	
	@Column(name = "CHARGES_TYPE")
	private String chargesType;
	
	@Column(name = "CHARGES_FOR")
	private Long chargesFor;
	
	@Column(name = "CHARGE_AMOUNT")
	private BigDecimal chargeAmount;
	
	@Column(name = "COST_CURRENCY_ID")
	private Long costCurrencyId;
	
	@Column(name = "COST_AMOUNT")
	private BigDecimal costAmount;
	
	@Column(name = "NO_BENE_DEDUCT_CHARGE_AMOUNT")
	private BigDecimal noBeneDeductChargeAmount;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Long getApplicationCountryId() {
		return applicationCountryId;
	}

	public void setApplicationCountryId(Long applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}

	public Long getBeneCountryId() {
		return beneCountryId;
	}

	public void setBeneCountryId(Long beneCountryId) {
		this.beneCountryId = beneCountryId;
	}

	public Long getBeneBankId() {
		return beneBankId;
	}

	public void setBeneBankId(Long beneBankId) {
		this.beneBankId = beneBankId;
	}

	public Long getBeneBankBranchId() {
		return beneBankBranchId;
	}

	public void setBeneBankBranchId(Long beneBankBranchId) {
		this.beneBankBranchId = beneBankBranchId;
	}

	public Long getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Long currencyId) {
		this.currencyId = currencyId;
	}

	public String getServiceGroupCode() {
		return serviceGroupCode;
	}

	public void setServiceGroupCode(String serviceGroupCode) {
		this.serviceGroupCode = serviceGroupCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getBankIndicator() {
		return bankIndicator;
	}

	public void setBankIndicator(String bankIndicator) {
		this.bankIndicator = bankIndicator;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankFullName() {
		return bankFullName;
	}

	public void setBankFullName(String bankFullName) {
		this.bankFullName = bankFullName;
	}

	public Long getRoutingCountryId() {
		return routingCountryId;
	}

	public void setRoutingCountryId(Long routingCountryId) {
		this.routingCountryId = routingCountryId;
	}

	public String getRoutingBankCode() {
		return routingBankCode;
	}

	public void setRoutingBankCode(String routingBankCode) {
		this.routingBankCode = routingBankCode;
	}

	public Long getRoutingBankId() {
		return routingBankId;
	}

	public void setRoutingBankId(Long routingBankId) {
		this.routingBankId = routingBankId;
	}

	public Long getBankBranchId() {
		return bankBranchId;
	}

	public void setBankBranchId(Long bankBranchId) {
		this.bankBranchId = bankBranchId;
	}

	public Long getServiceMasterId() {
		return serviceMasterId;
	}

	public void setServiceMasterId(Long serviceMasterId) {
		this.serviceMasterId = serviceMasterId;
	}

	public String getServiceDescription() {
		return serviceDescription;
	}

	public void setServiceDescription(String serviceDescription) {
		this.serviceDescription = serviceDescription;
	}

	public Long getRemittanceModeId() {
		return remittanceModeId;
	}

	public void setRemittanceModeId(Long remittanceModeId) {
		this.remittanceModeId = remittanceModeId;
	}

	public String getRemittanceDescription() {
		return remittanceDescription;
	}

	public void setRemittanceDescription(String remittanceDescription) {
		this.remittanceDescription = remittanceDescription;
	}

	public Long getDeliveryModeId() {
		return deliveryModeId;
	}

	public void setDeliveryModeId(Long deliveryModeId) {
		this.deliveryModeId = deliveryModeId;
	}

	public String getDeliveryDescription() {
		return deliveryDescription;
	}

	public void setDeliveryDescription(String deliveryDescription) {
		this.deliveryDescription = deliveryDescription;
	}

	public Long getWeekFrom() {
		return weekFrom;
	}

	public void setWeekFrom(Long weekFrom) {
		this.weekFrom = weekFrom;
	}

	public Long getWeekTo() {
		return weekTo;
	}

	public void setWeekTo(Long weekTo) {
		this.weekTo = weekTo;
	}

	public Long getWeekHoursFrom() {
		return weekHoursFrom;
	}

	public void setWeekHoursFrom(Long weekHoursFrom) {
		this.weekHoursFrom = weekHoursFrom;
	}

	public Long getWeekHoursTo() {
		return weekHoursTo;
	}

	public void setWeekHoursTo(Long weekHoursTo) {
		this.weekHoursTo = weekHoursTo;
	}

	public Long getWeekendFrom() {
		return weekendFrom;
	}

	public void setWeekendFrom(Long weekendFrom) {
		this.weekendFrom = weekendFrom;
	}

	public Long getWeekendTo() {
		return weekendTo;
	}

	public void setWeekendTo(Long weekendTo) {
		this.weekendTo = weekendTo;
	}

	public Long getWeekendHoursFrom() {
		return weekendHoursFrom;
	}

	public void setWeekendHoursFrom(Long weekendHoursFrom) {
		this.weekendHoursFrom = weekendHoursFrom;
	}

	public Long getWeekendHoursTo() {
		return weekendHoursTo;
	}

	public void setWeekendHoursTo(Long weekendHoursTo) {
		this.weekendHoursTo = weekendHoursTo;
	}

	public Long getDeliveryMinutes() {
		return deliveryMinutes;
	}

	public void setDeliveryMinutes(Long deliveryMinutes) {
		this.deliveryMinutes = deliveryMinutes;
	}

	public BigDecimal getTnxAmtFrom() {
		return tnxAmtFrom;
	}

	public void setTnxAmtFrom(BigDecimal tnxAmtFrom) {
		this.tnxAmtFrom = tnxAmtFrom;
	}

	public BigDecimal getTnxAmtTo() {
		return tnxAmtTo;
	}

	public void setTnxAmtTo(BigDecimal tnxAmtTo) {
		this.tnxAmtTo = tnxAmtTo;
	}

	public BigDecimal getFromAmount() {
		return fromAmount;
	}

	public void setFromAmount(BigDecimal fromAmount) {
		this.fromAmount = fromAmount;
	}

	public BigDecimal getToAmount() {
		return toAmount;
	}

	public void setToAmount(BigDecimal toAmount) {
		this.toAmount = toAmount;
	}

	public String getChargesType() {
		return chargesType;
	}

	public void setChargesType(String chargesType) {
		this.chargesType = chargesType;
	}

	public Long getChargesFor() {
		return chargesFor;
	}

	public void setChargesFor(Long chargesFor) {
		this.chargesFor = chargesFor;
	}

	public BigDecimal getChargeAmount() {
		return chargeAmount;
	}

	public void setChargeAmount(BigDecimal chargeAmount) {
		this.chargeAmount = chargeAmount;
	}

	public Long getCostCurrencyId() {
		return costCurrencyId;
	}

	public void setCostCurrencyId(Long costCurrencyId) {
		this.costCurrencyId = costCurrencyId;
	}

	public BigDecimal getCostAmount() {
		return costAmount;
	}

	public void setCostAmount(BigDecimal costAmount) {
		this.costAmount = costAmount;
	}

	public BigDecimal getNoBeneDeductChargeAmount() {
		return noBeneDeductChargeAmount;
	}

	public void setNoBeneDeductChargeAmount(BigDecimal noBeneDeductChargeAmount) {
		this.noBeneDeductChargeAmount = noBeneDeductChargeAmount;
	}
	
	
	

}
