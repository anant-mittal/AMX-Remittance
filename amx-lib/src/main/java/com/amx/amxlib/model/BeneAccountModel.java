package com.amx.amxlib.model;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.amx.jax.model.AbstractModel;

public class BeneAccountModel extends AbstractModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@NotNull(message="Beneficary Country Id may not be null")
	private BigDecimal beneficaryCountryId;
	@NotNull(message="Bank Id may not be null")
	private BigDecimal bankId; //agent master
	private BigDecimal bankBranchId;
	private String bankAccountNumber;
	@Pattern(regexp = "^[A-Za-z0-9]+$", message = "Invalid ibanNumber, only alphanumeric allowed")
	@Size(min=1, max=60)
	private String ibanNumber;
	@NotNull(message="Currency Id may not be null")
	private BigDecimal currencyId;
	@NotNull(message="Service Group Id may not be null")
	private BigDecimal serviceGroupId;// cash or bank
	private BigDecimal serviceProviderId; // service provider
	private BigDecimal serviceProviderBranchId; // agent branch
	private String swiftCode;
	private BigDecimal bankAccountTypeId;

	public BeneAccountModel() {
		super();
	}

	public BeneAccountModel(BigDecimal beneficaryCountryId, BigDecimal bankId, BigDecimal bankBranchId,
			String bankAccountNumber, BigDecimal currencyId, BigDecimal servicegropupId, BigDecimal serviceProviderId,
			BigDecimal serviceProviderBranchId, String swiftCode, BigDecimal bankAccountTypeId) {
		super();
		this.beneficaryCountryId = beneficaryCountryId;
		this.bankId = bankId;
		this.bankBranchId = bankBranchId;
		this.bankAccountNumber = bankAccountNumber;
		this.currencyId = currencyId;
		this.serviceGroupId = servicegropupId;
		this.serviceProviderId = serviceProviderId;
		this.serviceProviderBranchId = serviceProviderBranchId;
		this.swiftCode = swiftCode;
		this.bankAccountTypeId = bankAccountTypeId;
	}

	@Override
	public String getModelType() {
		return "bene-account-details";
	}

	public BigDecimal getBeneficaryCountryId() {
		return beneficaryCountryId;
	}

	public void setBeneficaryCountryId(BigDecimal beneficaryCountryId) {
		this.beneficaryCountryId = beneficaryCountryId;
	}

	public BigDecimal getBankId() {
		return bankId;
	}

	public void setBankId(BigDecimal bankId) {
		this.bankId = bankId;
	}

	public BigDecimal getBankBranchId() {
		return bankBranchId;
	}

	public void setBankBranchId(BigDecimal bankBranchId) {
		this.bankBranchId = bankBranchId;
	}

	public String getBankAccountNumber() {
		return bankAccountNumber;
	}

	public void setBankAccountNumber(String bankAccountNumber) {
		this.bankAccountNumber = bankAccountNumber;
	}

	public BigDecimal getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(BigDecimal currencyId) {
		this.currencyId = currencyId;
	}

	public BigDecimal getServiceProviderId() {
		return serviceProviderId;
	}

	public void setServiceProviderId(BigDecimal serviceProviderId) {
		this.serviceProviderId = serviceProviderId;
	}

	public BigDecimal getServiceProviderBranchId() {
		return serviceProviderBranchId;
	}

	public void setServiceProviderBranchId(BigDecimal serviceProviderBranchId) {
		this.serviceProviderBranchId = serviceProviderBranchId;
	}

	public String getSwiftCode() {
		return swiftCode;
	}

	public void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
	}

	public BigDecimal getBankAccountTypeId() {
		return bankAccountTypeId;
	}

	public void setBankAccountTypeId(BigDecimal bankAccountTypeId) {
		this.bankAccountTypeId = bankAccountTypeId;
	}

	@Override
	public String toString() {
		return "BeneAccountModel [beneficaryCountryId=" + beneficaryCountryId + ", bankId=" + bankId + ", bankBranchId="
				+ bankBranchId + ", bankAccountNumber=" + bankAccountNumber + ", currencyId=" + currencyId
				+ ", servicegropupId=" + serviceGroupId + ", serviceProviderId=" + serviceProviderId
				+ ", serviceProviderBranchId=" + serviceProviderBranchId + ", swiftCode=" + swiftCode
				+ ", bankAccountTypeId=" + bankAccountTypeId + "]";
	}

	public BigDecimal getServiceGroupId() {
		return serviceGroupId;
	}

	public void setServiceGroupId(BigDecimal serviceGroupId) {
		this.serviceGroupId = serviceGroupId;
	}
	
	public BigDecimal fetchBankBranchId() {
		if (this.bankBranchId != null) {
			return bankBranchId;
		} else {
			return serviceProviderBranchId;
		}
	}

	public String getIbanNumber() {
		return ibanNumber;
	}

	public void setIbanNumber(String ibanNumber) {
		this.ibanNumber = ibanNumber;
	}

}
