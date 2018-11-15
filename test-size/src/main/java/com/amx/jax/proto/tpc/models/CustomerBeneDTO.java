package com.amx.jax.proto.tpc.models;

import java.math.BigDecimal;

import com.amx.jax.swagger.ApiMockModelProperty;

public class CustomerBeneDTO {

	@ApiMockModelProperty(example = "1234")
	public BigDecimal id;

	@ApiMockModelProperty(example = "John Smith")
	public String fullName;

	@ApiMockModelProperty(example = "INR")
	public String currency;

	@ApiMockModelProperty(example = "100045****3456")
	public String account;

	@ApiMockModelProperty(example = "HDFC10045")
	public String ifsc;

	@ApiMockModelProperty(example = "HDFC")
	public String bank;

}
