package com.amx.jax.model.response.remittance;

import java.util.ArrayList;
import java.util.List;

public class PaymentModeDto {

	List<PaymentModeOfPaymentDto> paymentModeDtoList = new ArrayList<>();
	ConfigDto configDto = new ConfigDto();
	
	public ConfigDto getConfigDto() {
		return configDto;
	}
	public void setConfigDto(ConfigDto configDto) {
		this.configDto = configDto;
	}
	public List<PaymentModeOfPaymentDto> getPaymentModeDtoList() {
		return paymentModeDtoList;
	}
	public void setPaymentModeDtoList(List<PaymentModeOfPaymentDto> paymentModeDtoList) {
		this.paymentModeDtoList = paymentModeDtoList;
	}
}
