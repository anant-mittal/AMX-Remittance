package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class TrnxRoutingDetails implements Serializable, Comparable<TrnxRoutingDetails> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7230551793081078761L;

	private BigDecimal routingBankId;
	private BigDecimal serviceMasterId;
	
	private String serviceGroupCode;
	private String bankIndicator;
	private String bankCode;
	private String bankFullName;
	private BigDecimal routingCountryId;
	private String routingBankCode;
	private BigDecimal bankBranchId;
	private String serviceDescription;
	private BigDecimal remittanceModeId;
	private String remittanceDescription;
	private BigDecimal deliveryModeId;
	private String deliveryDescription;
	private BigDecimal ProcessingCountryId;
	
	private String chargesType;
	private BigDecimal chargesFor;
	private BigDecimal chargeAmount;
	private BigDecimal costCurrencyId;
	private BigDecimal costAmount;
	private BigDecimal noBeneDeductChargeAmount;

	private EstimatedDeliveryDetails estimatedDeliveryDetails;

	public String getServiceGroupCode() {
		return serviceGroupCode;
	}

	public void setServiceGroupCode(String serviceGroupCode) {
		this.serviceGroupCode = serviceGroupCode;
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

	public BigDecimal getProcessingCountryId() {
		return ProcessingCountryId;
	}

	public void setProcessingCountryId(BigDecimal processingCountryId) {
		ProcessingCountryId = processingCountryId;
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

	public EstimatedDeliveryDetails getEstimatedDeliveryDetails() {
		return estimatedDeliveryDetails;
	}

	public void setEstimatedDeliveryDetails(EstimatedDeliveryDetails estimatedDeliveryDetails) {
		this.estimatedDeliveryDetails = estimatedDeliveryDetails;
	}

	@Override
	public int compareTo(TrnxRoutingDetails that) {

		if (this.estimatedDeliveryDetails == null)
			if (that == null || that.estimatedDeliveryDetails == null)
				return 0; // equal
			else
				return -1; // null is before other strings
		else if (that.estimatedDeliveryDetails == null)
			return 1; // all other strings are after null
		else
			return this.estimatedDeliveryDetails.compareTo(that.estimatedDeliveryDetails);

	}

}
