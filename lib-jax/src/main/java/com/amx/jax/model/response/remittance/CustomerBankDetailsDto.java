package com.amx.jax.model.response.remittance;

import java.util.List;

public class CustomerBankDetailsDto {
	
	private LocalBankDetailsDto localBankDetailsDto;
	private List<String> customerNames;
	
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
	
}
