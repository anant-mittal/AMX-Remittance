package com.amx.jax.model.request.device;

import com.amx.jax.model.response.IDeviceStateData;
import com.amx.jax.model.response.customer.CustomerContactDto;
import com.amx.jax.model.response.customer.CustomerDto;
import com.amx.jax.model.response.customer.CustomerIdProofDto;
import com.amx.jax.model.response.customer.CustomerIncomeRangeDto;

public class SignaturePadCustomerRegStateInfo implements IDeviceStateData {

	private static final long serialVersionUID = -5695388111152939970L;
	CustomerContactDto customerContactDto;
	CustomerDto customerDto;
	CustomerIdProofDto customerIdProofDto;
	CustomerIncomeRangeDto customerIncomeRangeDto;

	public CustomerContactDto getCustomerContactDto() {
		return customerContactDto;
	}

	public void setCustomerContactDto(CustomerContactDto customerContactDto) {
		this.customerContactDto = customerContactDto;
	}

	public CustomerDto getCustomerDto() {
		return customerDto;
	}

	public void setCustomerDto(CustomerDto customerDto) {
		this.customerDto = customerDto;
	}

	public CustomerIdProofDto getCustomerIdProofDto() {
		return customerIdProofDto;
	}

	public void setCustomerIdProofDto(CustomerIdProofDto customerIdProofDto) {
		this.customerIdProofDto = customerIdProofDto;
	}

	public CustomerIncomeRangeDto getCustomerIncomeRangeDto() {
		return customerIncomeRangeDto;
	}

	public void setCustomerIncomeRangeDto(CustomerIncomeRangeDto customerIncomeRangeDto) {
		this.customerIncomeRangeDto = customerIncomeRangeDto;
	}

}
