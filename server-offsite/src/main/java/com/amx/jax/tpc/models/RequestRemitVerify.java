package com.amx.jax.tpc.models;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;

public class RequestRemitVerify {

	@ApiModelProperty(example = "67783643403", value = "Application Id recieved in RemitConfirm Response", required = true)
	public BigDecimal applicationId;

	@ApiModelProperty(example = "345678", value = "OTP recieved by Customer on his mobile", required = true)
	public BigDecimal mOtp;
}
