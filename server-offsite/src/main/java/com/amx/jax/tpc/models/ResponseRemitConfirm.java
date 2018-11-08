package com.amx.jax.tpc.models;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;

public class ResponseRemitConfirm extends ResponseRemitInquiry {

	@ApiModelProperty(value = "This is confirmation of rate customer will get and will have validity of 10 min", example = "67783643403", required = true, position = 0)
	public BigDecimal applicationId;

	@ApiModelProperty(example = "ZXY", value = "Prefix for OTP recieved by customer on his mobile", required = true)
	public String mOtpPrefix;

}
