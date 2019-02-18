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
	private static final long serialVersionUID = 1049139832593601078L;

	@Id
	@Column(name = "UUID")
	private String uuid;

	@Column(name = "APPLICATION_COUNTRY_ID")
	private BigDecimal applicationCountryId;

	@Column(name = "BENE_COUNTRY_ID")
	private BigDecimal beneCountryId;

	@Column(name = "BENE_BANK_ID")
	private BigDecimal beneBankId;

	@Column(name = "BENE_BANK_BRANCH_ID")
	private BigDecimal beneBankBranchId;

	@Column(name = "CURRENCY_ID")
	private BigDecimal currencyId;

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
	private BigDecimal routingCountryId;

	@Column(name = "ROUTING_BANK_CODE")
	private String routingBankCode;

	@Column(name = "ROUTING_BANK_ID")
	private BigDecimal routingBankId;

	@Column(name = "BANK_BRANCH_ID")
	private BigDecimal bankBranchId;

	@Column(name = "SERVICE_MASTER_ID")
	private BigDecimal serviceMasterId;

	@Column(name = "SERVICE_DESCRIPTION")
	private String serviceDescription;

	@Column(name = "REMITTANCE_MODE_ID")
	private BigDecimal remittanceModeId;

	@Column(name = "REMITTANCE_DESCRIPTION")
	private String remittanceDescription;

	@Column(name = "DELIVERY_MODE_ID")
	private BigDecimal deliveryModeId;

	@Column(name = "DELIVERY_DESCRIPTION")
	private String deliveryDescription;

	@Column(name = "WEEK_FROM")
	private BigDecimal weekFrom;

	@Column(name = "WEEK_TO")
	private BigDecimal weekTo;

	@Column(name = "WEEK_HOURS_FROM")
	private BigDecimal weekHoursFrom;

	@Column(name = "WEEK_HOURS_TO")
	private BigDecimal weekHoursTo;

	@Column(name = "WEEKEND_FROM")
	private BigDecimal weekendFrom;

	@Column(name = "WEEKEND_TO")
	private BigDecimal weekendTo;

	@Column(name = "WEEKEND_HOURS_FROM")
	private BigDecimal weekendHoursFrom;

	@Column(name = "WEEKEND_HOURS_TO")
	private BigDecimal weekendHoursTo;

	@Column(name = "DELIVERY_MINUTES")
	private BigDecimal delievryMinutes;

	@Column(name = "TXN_AMT_FROM")
	private BigDecimal txnAmtFrom;

	@Column(name = "TXN_AMT_TO")
	private BigDecimal txnAmtTo;

	@Column(name = "FROM_AMOUNT")
	private BigDecimal fromAmount;

	@Column(name = "TO_AMOUNT")
	private BigDecimal toAmount;

	@Column(name = "CHARGES_TYPE")
	private String chargesType;

	@Column(name = "CHARGES_FOR")
	private BigDecimal chargesFor;

	@Column(name = "CHARGE_AMOUNT")
	private BigDecimal chargeAmount;

	@Column(name = "COST_CURRENCY_ID")
	private BigDecimal costCurrencyId;

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

	public BigDecimal getApplicationCountryId() {
		return applicationCountryId;
	}

	public void setApplicationCountryId(BigDecimal applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}

	public BigDecimal getBeneCountryId() {
		return beneCountryId;
	}

	public void setBeneCountryId(BigDecimal beneCountryId) {
		this.beneCountryId = beneCountryId;
	}

	public BigDecimal getBeneBankId() {
		return beneBankId;
	}

	public void setBeneBankId(BigDecimal beneBankId) {
		this.beneBankId = beneBankId;
	}

	public BigDecimal getBeneBankBranchId() {
		return beneBankBranchId;
	}

	public void setBeneBankBranchId(BigDecimal beneBankBranchId) {
		this.beneBankBranchId = beneBankBranchId;
	}

	public BigDecimal getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(BigDecimal currencyId) {
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

	public BigDecimal getRoutingCountryId() {
		return routingCountryId;
	}

	public void setRoutingCountryId(BigDecimal routingCountryId) {
		this.routingCountryId = routingCountryId;
	}

	public String getRoutingBankCode() {
		return routingBankCode;
	}

	public void setRoutingBankCode(String routingBankCode) {
		this.routingBankCode = routingBankCode;
	}

	public BigDecimal getRoutingBankId() {
		return routingBankId;
	}

	public void setRoutingBankId(BigDecimal routingBankId) {
		this.routingBankId = routingBankId;
	}

	public BigDecimal getBankBranchId() {
		return bankBranchId;
	}

	public void setBankBranchId(BigDecimal bankBranchId) {
		this.bankBranchId = bankBranchId;
	}

	public BigDecimal getServiceMasterId() {
		return serviceMasterId;
	}

	public void setServiceMasterId(BigDecimal serviceMasterId) {
		this.serviceMasterId = serviceMasterId;
	}

	public String getServiceDescription() {
		return serviceDescription;
	}

	public void setServiceDescription(String serviceDescription) {
		this.serviceDescription = serviceDescription;
	}

	public BigDecimal getRemittanceModeId() {
		return remittanceModeId;
	}

	public void setRemittanceModeId(BigDecimal remittanceModeId) {
		this.remittanceModeId = remittanceModeId;
	}

	public String getRemittanceDescription() {
		return remittanceDescription;
	}

	public void setRemittanceDescription(String remittanceDescription) {
		this.remittanceDescription = remittanceDescription;
	}

	public BigDecimal getDeliveryModeId() {
		return deliveryModeId;
	}

	public void setDeliveryModeId(BigDecimal deliveryModeId) {
		this.deliveryModeId = deliveryModeId;
	}

	public String getDeliveryDescription() {
		return deliveryDescription;
	}

	public void setDeliveryDescription(String deliveryDescription) {
		this.deliveryDescription = deliveryDescription;
	}

	public BigDecimal getWeekFrom() {
		return weekFrom;
	}

	public void setWeekFrom(BigDecimal weekFrom) {
		this.weekFrom = weekFrom;
	}

	public BigDecimal getWeekTo() {
		return weekTo;
	}

	public void setWeekTo(BigDecimal weekTo) {
		this.weekTo = weekTo;
	}

	public BigDecimal getWeekHoursFrom() {
		return weekHoursFrom;
	}

	public void setWeekHoursFrom(BigDecimal weekHoursFrom) {
		this.weekHoursFrom = weekHoursFrom;
	}

	public BigDecimal getWeekHoursTo() {
		return weekHoursTo;
	}

	public void setWeekHoursTo(BigDecimal weekHoursTo) {
		this.weekHoursTo = weekHoursTo;
	}

	public BigDecimal getWeekendFrom() {
		return weekendFrom;
	}

	public void setWeekendFrom(BigDecimal weekendFrom) {
		this.weekendFrom = weekendFrom;
	}

	public BigDecimal getWeekendTo() {
		return weekendTo;
	}

	public void setWeekendTo(BigDecimal weekendTo) {
		this.weekendTo = weekendTo;
	}

	public BigDecimal getWeekendHoursFrom() {
		return weekendHoursFrom;
	}

	public void setWeekendHoursFrom(BigDecimal weekendHoursFrom) {
		this.weekendHoursFrom = weekendHoursFrom;
	}

	public BigDecimal getWeekendHoursTo() {
		return weekendHoursTo;
	}

	public void setWeekendHoursTo(BigDecimal weekendHoursTo) {
		this.weekendHoursTo = weekendHoursTo;
	}

	public BigDecimal getDelievryMinutes() {
		return delievryMinutes;
	}

	public void setDelievryMinutes(BigDecimal delievryMinutes) {
		this.delievryMinutes = delievryMinutes;
	}

	public BigDecimal getTxnAmtFrom() {
		return txnAmtFrom;
	}

	public void setTxnAmtFrom(BigDecimal txnAmtFrom) {
		this.txnAmtFrom = txnAmtFrom;
	}

	public BigDecimal getTxnAmtTo() {
		return txnAmtTo;
	}

	public void setTxnAmtTo(BigDecimal txnAmtTo) {
		this.txnAmtTo = txnAmtTo;
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

	public BigDecimal getChargesFor() {
		return chargesFor;
	}

	public void setChargesFor(BigDecimal chargesFor) {
		this.chargesFor = chargesFor;
	}

	public BigDecimal getChargeAmount() {
		return chargeAmount;
	}

	public void setChargeAmount(BigDecimal chargeAmount) {
		this.chargeAmount = chargeAmount;
	}

	public BigDecimal getCostCurrencyId() {
		return costCurrencyId;
	}

	public void setCostCurrencyId(BigDecimal costCurrencyId) {
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
