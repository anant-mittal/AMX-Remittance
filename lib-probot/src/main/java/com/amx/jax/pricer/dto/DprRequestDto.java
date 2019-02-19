package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.amx.jax.dict.UserClient.Channel;

public class DprRequestDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3112085486390742452L;

	private BigDecimal customerId;

	@NotNull(message = "Local Country Id Can not be Null or Empty")
	private BigDecimal localCountryId;

	@NotNull(message = "Foreign Country Id Can not be Null or Empty")
	private BigDecimal foreignCountryId;

	private BigDecimal localAmount;
	private BigDecimal foreignAmount;

	@NotNull(message = "Local Currency Id Can not be Null or Empty")
	private BigDecimal localCurrencyId;

	@NotNull(message = "Foreign Currency Id Can not be Null or Empty")
	private BigDecimal foreignCurrencyId;

	@NotNull(message = "Country Branch Id Can not be Null or Empty")
	private BigDecimal countryBranchId;

	@NotNull(message = "Channel Can not be Null or Empty")
	private Channel channel;

	@NotNull(message = "Beneficiary Id Can not be Null or Empty")
	private BigDecimal beneficiaryId;

	@NotNull(message = "Beneficiary Bank Id Can not be Null or Empty")
	private BigDecimal beneficiaryBankId;
	
	@NotNull(message = "Beneficiary Branch Id Can not be Null or Empty")
	private BigDecimal beneficiaryBranchId;

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	public BigDecimal getLocalCountryId() {
		return localCountryId;
	}

	public void setLocalCountryId(BigDecimal localCountryId) {
		this.localCountryId = localCountryId;
	}

	public BigDecimal getForeignCountryId() {
		return foreignCountryId;
	}

	public void setForeignCountryId(BigDecimal foreignCountryId) {
		this.foreignCountryId = foreignCountryId;
	}

	public BigDecimal getLocalAmount() {
		return localAmount;
	}

	public void setLocalAmount(BigDecimal localAmount) {
		this.localAmount = localAmount;
	}

	public BigDecimal getForeignAmount() {
		return foreignAmount;
	}

	public void setForeignAmount(BigDecimal foreignAmount) {
		this.foreignAmount = foreignAmount;
	}

	public BigDecimal getLocalCurrencyId() {
		return localCurrencyId;
	}

	public void setLocalCurrencyId(BigDecimal localCurrencyId) {
		this.localCurrencyId = localCurrencyId;
	}

	public BigDecimal getForeignCurrencyId() {
		return foreignCurrencyId;
	}

	public void setForeignCurrencyId(BigDecimal foreignCurrencyId) {
		this.foreignCurrencyId = foreignCurrencyId;
	}

	public BigDecimal getCountryBranchId() {
		return countryBranchId;
	}

	public void setCountryBranchId(BigDecimal countryBranchId) {
		this.countryBranchId = countryBranchId;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public BigDecimal getBeneficiaryId() {
		return beneficiaryId;
	}

	public void setBeneficiaryId(BigDecimal beneficiaryId) {
		this.beneficiaryId = beneficiaryId;
	}

	public BigDecimal getBeneficiaryBankId() {
		return beneficiaryBankId;
	}

	public void setBeneficiaryBankId(BigDecimal beneficiaryBankId) {
		this.beneficiaryBankId = beneficiaryBankId;
	}

	public BigDecimal getBeneficiaryBranchId() {
		return beneficiaryBranchId;
	}

	public void setBeneficiaryBranchId(BigDecimal beneficiaryBranchId) {
		this.beneficiaryBranchId = beneficiaryBranchId;
	}
	
	

}
