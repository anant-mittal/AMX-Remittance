package com.amx.jax.model.response.insurance;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.amx.jax.model.response.CurrencyMasterDTO;

public class GigInsuranceDetail {

	Boolean isOptIn;
	BigDecimal policyAmount;
	CurrencyMasterDTO currency;
	Date policyStartDate;
	Date policyEndDate;
	List<NomineeDetailDto> nomineeDetail;

	public Boolean getIsOptIn() {
		return isOptIn;
	}

	public void setIsOptIn(Boolean isOptIn) {
		this.isOptIn = isOptIn;
	}

	public BigDecimal getPolicyAmount() {
		return policyAmount;
	}

	public void setPolicyAmount(BigDecimal policyAmount) {
		this.policyAmount = policyAmount;
	}

	public Date getPolicyStartDate() {
		return policyStartDate;
	}

	public void setPolicyStartDate(Date policyStartDate) {
		this.policyStartDate = policyStartDate;
	}

	public Date getPolicyEndDate() {
		return policyEndDate;
	}

	public void setPolicyEndDate(Date policyEndDate) {
		this.policyEndDate = policyEndDate;
	}

	public List<NomineeDetailDto> getNomineeDetail() {
		return nomineeDetail;
	}

	public void setNomineeDetail(List<NomineeDetailDto> nomineeDetail) {
		this.nomineeDetail = nomineeDetail;
	}

	public CurrencyMasterDTO getCurrency() {
		return currency;
	}

	public void setCurrency(CurrencyMasterDTO currency) {
		this.currency = currency;
	}
}
