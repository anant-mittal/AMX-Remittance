package com.amx.jax.tpc.models;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;

public class ModelSourceOfFund {

	@ApiModelProperty(example = "1234")
	public BigDecimal id;

	@ApiModelProperty(example = "INVEST_LOAN")
	public String code;

	@ApiModelProperty(example = "Invest in Loan")
	public String description;

}
