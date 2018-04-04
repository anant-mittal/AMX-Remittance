package com.amx.amxlib.model;

import java.math.BigDecimal;

public class BeneAccountModel extends AbstractModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BigDecimal beneficaryCountryId;
	private BigDecimal bankId;
	private BigDecimal bankBranchId;
	private String bankAccountNumber;
	private BigDecimal currencyId;
	private BigDecimal servicegropupId;// cash or bank
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
		this.servicegropupId = servicegropupId;
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

	public BigDecimal getServicegropupId() {
		return servicegropupId;
	}

	public void setServicegropupId(BigDecimal servicegropupId) {
		this.servicegropupId = servicegropupId;
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

}
