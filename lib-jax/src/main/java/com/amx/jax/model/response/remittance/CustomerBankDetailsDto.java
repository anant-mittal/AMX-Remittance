package com.amx.jax.model.response.remittance;

import java.math.BigDecimal;
import java.util.List;

public class CustomerBankDetailsDto {
	
	private LocalBankDetailsDto localBankDetailsDto;
	private List<String> customerNames;
	private List<BigDecimal> relationId;
	
	public LocalBankDetailsDto getLocalBankDetailsDto() {
		return localBankDetailsDto;
	}
	public void setLocalBankDetailsDto(LocalBankDetailsDto localBankDetailsDto) {
		this.localBankDetailsDto = localBankDetailsDto;
	}
	
	public List<String> getCustomerNames() {
		return customerNames;
	}
	public void setCustomerNames(List<String> customerNames) {
		this.customerNames = customerNames;
	}
	public List<BigDecimal> getRelationId() {
		return relationId;
	}
	public void setRelationId(List<BigDecimal> relationId) {
		this.relationId = relationId;
	}
	
}
