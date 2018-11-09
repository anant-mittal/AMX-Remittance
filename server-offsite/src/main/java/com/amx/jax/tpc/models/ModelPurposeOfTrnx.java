package com.amx.jax.tpc.models;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;

public class ModelPurposeOfTrnx {

	@ApiModelProperty(example = "1234")
	public BigDecimal id;

	@ApiModelProperty(example = "SALARY")
	public String code;

	@ApiModelProperty(example = "Salary Income")
	public String description;

}
