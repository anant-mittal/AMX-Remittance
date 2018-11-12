package com.amx.jax.proto.tpc.models;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;

public class PurposeOfTrnxDTO {

	@ApiModelProperty(example = "1234")
	public BigDecimal id;

	@ApiModelProperty(example = "SALARY")
	public String code;

	@ApiModelProperty(example = "Salary Income")
	public String description;

}
