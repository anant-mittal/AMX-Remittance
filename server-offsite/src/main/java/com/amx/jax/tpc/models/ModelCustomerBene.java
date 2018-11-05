package com.amx.jax.tpc.models;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;

public class ModelCustomerBene {

	@ApiModelProperty(example = "1234")
	public BigDecimal id;

	@ApiModelProperty(example = "John Smith")
	public String fullName;

	@ApiModelProperty(example = "INR")
	public String currency;

	@ApiModelProperty(example = "100045****3456")
	public String account;

	@ApiModelProperty(example = "HDFC10045")
	public String ifsc;

	@ApiModelProperty(example = "HDFC")
	public String bank;

}
