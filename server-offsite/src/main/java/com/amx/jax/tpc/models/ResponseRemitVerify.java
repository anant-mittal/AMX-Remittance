package com.amx.jax.tpc.models;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;

public class ResponseRemitVerify {

	@ApiModelProperty(example = "67783643403", value = "Transaction Id", required = true)
	public BigDecimal transactionId;
}
