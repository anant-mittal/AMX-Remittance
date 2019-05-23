package com.amx.jax.model.response.remittance.branch;

import com.amx.jax.model.response.remittance.RemittanceTransactionResponsetModel;
import com.amx.jax.model.response.remittance.RoutingResponseDto;

public class BranchRemittanceGetExchangeRateResponse extends RemittanceTransactionResponsetModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	RoutingResponseDto routingResponseDto;

	public RoutingResponseDto getRoutingResponseDto() {
		return routingResponseDto;
	}

	public void setRoutingResponseDto(RoutingResponseDto routingResponseDto) {
		this.routingResponseDto = routingResponseDto;
	}
}


